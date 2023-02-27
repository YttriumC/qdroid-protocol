package cf.vbnm.amoeba.qdroid

import cf.vbnm.amoeba.Amoeba
import cf.vbnm.amoeba.core.CoreProperty
import cf.vbnm.amoeba.core.spi.Starter
import cf.vbnm.amoeba.qdroid.config.JpaTest
import cf.vbnm.amoeba.qdroid.config.WebSocketConfig
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@ComponentScan(value = ["cf.vbnm.amoeba.qdroid.config", "cf.vbnm.amoeba.qdroid.controller", "cf.vbnm.amoeba.qdroid.bot"])
@Import(WebSocketConfig::class)
@Configuration
open class QDroidStarter : Starter<QDroidStarter> {
    override fun initProperty(coreProperty: CoreProperty) {

    }

    override fun getObject(): QDroidStarter {
        return this
    }

    override fun getObjectType(): Class<*> {
        return this.javaClass
    }

    override fun getJpaConfigObject(): Class<*> {
        return JpaTest::class.java
    }

    override fun getEntityPackages(): Array<String> {
        return arrayOf("cf.vbnm.amoeba.qdroid.jpa")
    }
}

fun main() {
    Amoeba.runApplication()
}