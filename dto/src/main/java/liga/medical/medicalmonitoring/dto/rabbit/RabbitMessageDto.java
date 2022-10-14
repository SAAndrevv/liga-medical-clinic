package liga.medical.medicalmonitoring.dto.rabbit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMessageDto {
    private String type;
    private String message;
}
