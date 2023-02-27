package cf.vbnm.amoeba.qdroid.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class MessageController {

    @RequestMapping("/qdroid/test")
    fun websocketProcedure(): Any {
        return object {
            val test = "test"
        }
    }

}