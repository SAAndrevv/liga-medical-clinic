package liga.medical.medicalmonitoring.core.service;

import liga.medical.medicalmonitoring.dto.rabbit.RabbitMessageDto;

public interface RabbitListenerService {
    void processCommonMonitoringQueue(RabbitMessageDto message);
}
