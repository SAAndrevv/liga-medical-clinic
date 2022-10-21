package liga.medical.medicalmonitoring.core.service;

import liga.medical.medicalmonitoring.dto.rabbit.RabbitMessageDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@EnableRabbit
@Component
public class RabbitListenerServiceImpl implements RabbitListenerService{

    @NonNull
    private AmqpTemplate template;

    @Override
    @RabbitListener(queues = "common_monitoring")
    public void processCommonMonitoringQueue(RabbitMessageDto message) {
        System.out.println("Get message: " + message);
        template.convertAndSend(message.getType(), message);
    }

}
