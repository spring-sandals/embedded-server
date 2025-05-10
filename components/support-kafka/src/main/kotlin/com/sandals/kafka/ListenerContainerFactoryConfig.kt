import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

fun listenerContainerFactory(configs: Map<String, String>): ConcurrentKafkaListenerContainerFactory<String, String> {
    val containerFactory = ConcurrentKafkaListenerContainerFactory<String, String>()
    containerFactory.setConsumerFactory(DefaultKafkaConsumerFactory(configs))
    return containerFactory
}