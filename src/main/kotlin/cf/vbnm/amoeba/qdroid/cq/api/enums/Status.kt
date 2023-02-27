package cf.vbnm.amoeba.qdroid.cq.api.enums

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = Status.Deserializer::class)
enum class Status(val value: String, val desc: String) {
    OK("ok", "api 调用成功"),
    ASYNC("async", "api 调用已经提交异步处理, 此时 retcode 为 1, 具体 api 调用是否成功无法得知"),
    FAILED("failed", "api 调用失败");

    override fun toString(): String {
        return value
    }

    companion object {
        fun parseValue(value: String): Status {
            Status.values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            throw IllegalArgumentException("No such Status: $value")
        }
    }

    class Deserializer : JsonDeserializer<Status>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): Status {
            return Status.parseValue(p.text)
        }
    }
}