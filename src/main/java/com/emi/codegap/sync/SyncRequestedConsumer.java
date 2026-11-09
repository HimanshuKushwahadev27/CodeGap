package com.emi.codegap.sync;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.emi.codegap.service.PlatformSyncService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SyncRequestedConsumer {

    private final PlatformSyncService platformSyncService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${codegap.kafka.sync-completed-topic:sync-completed}")
    private String syncCompletedTopic;

    @KafkaListener(
        topics = "${codegap.kafka.sync-request-topic:sync-requests}",
        containerFactory = "syncKafkaListenerContainerFactory"
    )
    public void consume(SyncRequestedEvent event) {
        try {
            int count = platformSyncService.sync(event.accountId(), event.platform(), event.handle());
            kafkaTemplate.send(
                syncCompletedTopic,
                event.accountId().toString(),
                new SyncCompletedEvent(event.accountId(), event.platform(), count)
            );
        } catch (RuntimeException exception) {
            platformSyncService.markFailed(event.accountId());
            throw exception;
        }
    }
}
