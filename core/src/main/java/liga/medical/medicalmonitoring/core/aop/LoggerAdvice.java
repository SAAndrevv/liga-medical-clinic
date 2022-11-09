package liga.medical.medicalmonitoring.core.aop;

import liga.medical.commonmodule.dto.enums.RabbitMessageType;
import liga.medical.commonmodule.dto.enums.SystemType;
import liga.medical.commonmodule.dto.logger.LogDto;
import liga.medical.commonmodule.dto.rabbit.RabbitMessageDto;
import liga.medical.commonmodule.service.LoggerService;
import liga.medical.commonmodule.utils.ListUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class LoggerAdvice {

    private final LoggerService loggerService;

    @Autowired
    private Tracer tracer;

    @Pointcut("@annotation(liga.medical.commonmodule.service.annotation.DBLog) && args(messageDto,..)")
    public void loggablePointcut(RabbitMessageDto messageDto) { }

    @Around("loggablePointcut(messageDto)")
    public Object applicationLogger(ProceedingJoinPoint pjp, RabbitMessageDto messageDto) {
        String className = pjp.getTarget().getClass().getName();
        String method = pjp.getSignature().getName();
        List<?> args = ListUtils.toWildCardList(pjp.getArgs());

        LogDto logDto = new LogDto(
                getTraceId(),
                SystemType.MEDICAL_MONITORING.name(),
                String.format("%s.%s(%s)",
                        className,
                        method,
                        (!args.isEmpty()) ? ListUtils.stringListWithoutBrackets(args) : " "
                ));

        String beforeLog = String.format("Before method calls: %s %s(%s)",
                className,
                method,
                (!args.isEmpty()) ? ListUtils.stringListWithoutBrackets(args) : " "
        );
        log.debug(beforeLog);

        Object obj = null;
        try {
            obj = pjp.proceed();
        } catch (Throwable e) {
            loggerService.saveExceptionLog(logDto);

            String afterLog = String.format("After a failed call: %s %s(%s). Exception %s",
                    className,
                    method,
                    (!args.isEmpty()) ? ListUtils.stringListWithoutBrackets(args) : " ",
                    e.toString()
            );
            log.debug(afterLog);
        }

        if (RabbitMessageType.valueOf(messageDto.getType()) == RabbitMessageType.ERROR) {
            loggerService.saveExceptionLog(logDto);
        } else {
            loggerService.saveDebugLog(logDto);
        }

        String afterLog = String.format("After a successful call: %s %s(%s)",
                className,
                method,
                (!args.isEmpty()) ? ListUtils.stringListWithoutBrackets(args) : " "
        );
        log.debug(afterLog);

        return obj;
    }

    private String getTraceId() {
        Span span = tracer.currentSpan();
        if (span != null) {
            return span.context().traceId();
        }

        return "";
    }

}
