package liga.medical.medicalmonitoring.core.service;

import liga.medical.commonmodule.dto.rabbit.RabbitMessageDto;

public interface RabbitListenerService {

    void processCommonMonitoringQueue(RabbitMessageDto message);

}
