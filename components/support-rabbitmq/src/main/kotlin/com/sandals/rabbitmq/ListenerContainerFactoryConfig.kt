import org.springframework.amqp.core.AcknowledgeMode
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory

fun listenerContainerFactory(
    connectionFactory: CachingConnectionFactory,
    acknowledgeMode: AcknowledgeMode,
    prefetch: Int
): SimpleRabbitListenerContainerFactory {
    val containerFactory = SimpleRabbitListenerContainerFactory()
    containerFactory.setConnectionFactory(connectionFactory)
    containerFactory.setAcknowledgeMode(acknowledgeMode)
    containerFactory.setPrefetchCount(prefetch)
    return containerFactory
}