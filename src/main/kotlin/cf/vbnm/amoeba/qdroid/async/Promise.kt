package cf.vbnm.amoeba.qdroid.async

import cf.vbnm.amoeba.core.log.Slf4kt
import kotlinx.coroutines.*

private val log = Slf4kt.getLogger(Promise::class.java)

@OptIn(DelicateCoroutinesApi::class)
class Promise<Rsv> private constructor(
    private val preJob: Job?,
    private val timeout: Long = 0,
    executor: (resolve: (Rsv) -> Unit, reject: (Throwable) -> Unit) -> Unit
) {

    constructor(
        timeout: Long = 0,
        executor: (resolve: (Rsv) -> Unit, reject: (Throwable) -> Unit) -> Unit,
    ) : this(null, timeout, executor)

    private var result: Rsv? = null
    private var status = Status.PENDING
    private var exception: Throwable? = null
    private var thisJob: Job

    init {
        thisJob = GlobalScope.launch {
            // wait the previous job has done
            preJob?.join()
            if (timeout > 0) {
                try {
                    withTimeout(timeout) {
                        executor({
                            result = it
                            if (status != Status.REJECTED) {
                                status = Status.FULFILLED
                            }
                        }, {
                            exception = it
                            status = Status.REJECTED
                        })
                    }
                } catch (e: Throwable) {
                    status = Status.REJECTED
                    exception = e
                }

            } else {
                try {
                    executor({
                        result = it
                        if (status != Status.REJECTED) {
                            status = Status.FULFILLED
                        }
                    }, {
                        exception = it
                        status = Status.REJECTED
                    })
                } catch (e: Throwable) {
                    status = Status.REJECTED
                    exception = e
                }
            }
        }
    }


    fun <T> then(timeout: Long = 0, resolve: (Rsv) -> T): Promise<T> {
        return Promise(thisJob, timeout) { res, rej ->
            if (status == Status.REJECTED) {
                exception?.let { rej(it) }
                return@Promise
            }

            try {
                result?.let { res(resolve(it)) }
            } catch (e: Throwable) {
                rej(e)
            }
        }
    }

    fun catch(timeout: Long = 0, reject: (Throwable) -> Unit): Promise<Unit> {
        return Promise(thisJob, timeout) { res, _ ->
            if (status != Status.REJECTED) {
                res(Unit)
                return@Promise
            }
            exception?.let { reject(it) }
            res(Unit)
        }
    }

    fun getStatus() = status
    enum class Status {
        PENDING, FULFILLED, REJECTED
    }

    companion object {
        fun newPromise(timeout: Long = 0): Promise<Unit> {
            return Promise(timeout) { resolve, _ ->
                resolve(Unit)
            }
        }

        fun <T> newPromise(timeout: Long = 0, resolve: () -> T): Promise<T> {
            return Promise(timeout) { res, _ ->
                res(resolve())
            }
        }
    }
}
