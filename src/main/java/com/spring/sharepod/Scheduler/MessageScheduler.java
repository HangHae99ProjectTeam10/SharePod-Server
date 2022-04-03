package com.spring.sharepod.Scheduler;

import com.spring.sharepod.v1.repository.ChatMessage.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Component
@Service
@RequiredArgsConstructor
public class MessageScheduler {
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void MessageDate () {
        LocalDateTime dateAndtime = LocalDateTime.now();
        chatMessageRepository.deleteAllByModifiedAtBefore(dateAndtime.minusMonths(1));
    }
}