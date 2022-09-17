package com.example.application.endpoint;

import com.example.application.dto.MessageDTO;
import com.example.application.model.Message;
import com.example.application.security.SecurityService;
import com.example.application.service.MessageService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import dev.hilla.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Endpoint
@AnonymousAllowed
@Slf4j
public class ChatEndpoint {
    @Autowired
    private SecurityService securityService;

    @Autowired
    private MessageService messageService;

    private Many<MessageDTO> chatSink;
    private Flux<MessageDTO> chat;

    public ChatEndpoint() {
        chatSink = Sinks.many().multicast().directBestEffort();
        chat = chatSink.asFlux().replay(10).autoConnect();
    }

    public @Nonnull Flux<@Nonnull MessageDTO> join() {
        return chat;
    }

    public void send(MessageDTO messageDTO) {
        ZonedDateTime currentTime = ZonedDateTime.now();
        messageDTO.setTime(currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        messageDTO.setUserName(securityService.getAuthenticatedUserUsername()); //TODO it should get User model fully

        messageService.save(
                Message.builder()
                        .message(messageDTO.getText())
                        .sender(securityService.getAuthenticatedUser().orElse(null)) //TODO Redesign this one
                        .sentTime(currentTime)
                        .build()
        ).block();

        chatSink.emitNext(
                messageDTO,
                (signalType, emitResult) -> emitResult.equals(Sinks.EmitResult.FAIL_NON_SERIALIZED)
        );
    }
}
