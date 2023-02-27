package cf.vbnm.amoeba.qdroid.cq.api.enums

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = Retcode.Deserializer::class)
enum class Retcode(val value: Int, val desc: String) {
    SUCCESS(0, "api 调用成功"),
    ASYNC(1, "api 调用已经提交异步处理, 此时 retcode 为 1, 具体 api 调用是否成功无法得知"),
    FAILED(2, "api 调用失败");

    override fun toString(): String {
        return value.toString()
    }

    companion object {
        fun parseValue(value: String): Retcode {
            val code = value.toInt()
            Retcode.values().forEach {
                if (it.value == code) {
                    return it
                }
            }
            return FAILED
        }

        fun parseValue(value: Int): Retcode {
            Retcode.values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            return FAILED
        }
    }

    class Deserializer : JsonDeserializer<Retcode>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): Retcode {
            return parseValue(p.text)
        }
    }
}