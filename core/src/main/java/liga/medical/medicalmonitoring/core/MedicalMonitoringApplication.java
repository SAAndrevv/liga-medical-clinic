package liga.medical.medicalmonitoring.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"liga.medical.medicalmonitoring", "liga.medical.commonmodule"})
@EnableJpaRepositories("liga.medical.commonmodule.service.repository")
@EntityScan("liga.medical.commonmodule.dto.entity")
public class MedicalMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalMonitoringApplication.class, args);
    }

}