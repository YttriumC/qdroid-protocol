package cf.vbnm.amoeba.qdroid.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.repository.config.BootstrapMode

@EnableJpaRepositories(
    "cf.vbnm.amoeba.qdroid.bot",
    entityManagerFactoryRef = "persistenceEntityManagerFactory",
    transactionManagerRef = "persistenceTransactionManager",
    bootstrapMode = BootstrapMode.DEFERRED
)
@ComponentScan("cf.vbnm.amoeba.qdroid.jpa")
open class JpaTest {
}