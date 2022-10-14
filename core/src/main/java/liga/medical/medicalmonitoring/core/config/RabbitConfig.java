package liga.medical.medicalmonitoring.core.config;

import liga.medical.medicalmonitoring.dto.rabbit.RabbitType;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitConfig {

    @Value("${sprint.rabbitmq.host:localhost}")
    private String host;
    @Value("${sprint.rabbitmq.port:5672}")
    private int port;

    @Value("${rabbit.queue.common-monitoring-queue}")
    private String commonMonitoringQueue;
    @Value("${rabbit.queue.daily-queue}")
    private String dailyQueue;
    @Value("${rabbit.queue.alert-queue}")
    private String alertQueue;
    @Value("${rabbit.queue.error-queue}")
    private String errorQueue;

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new
                CachingConnectionFactory(host);
        connectionFactory.setPort(port);

        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        rabbitTemplate.setExchange("queue-exchange");
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(producerJackson2MessageConverter());
        return factory;
    }

    @Bean("common-monitoring-queue")
    public Queue commonMonitoringQueue() {
        return new Queue(commonMonitoringQueue);
    }

    @Bean("daily-queue")
    public Queue dailyQueue() {
        return new Queue(dailyQueue);
    }

    @Bean("alert-queue")
    public Queue alertQueue() {
        return new Queue(alertQueue);
    }

    @Bean
    public Queue errorQueue() {
        return new Queue(errorQueue);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("queue-exchange");
    }

    @Bean
    public Binding bindingDailyQueue() {
        return BindingBuilder.bind(dailyQueue()).to(directExchange()).with(RabbitType.DAILY.getType());
    }

    @Bean Binding bindingAlertQueue() {
        return BindingBuilder.bind(alertQueue()).to(directExchange()).with(RabbitType.ALERT.getType());
    }

    @Bean Binding bindingErrorQueue() {
        return BindingBuilder.bind(errorQueue()).to(directExchange()).with(RabbitType.ERROR.getType());
    }

}
