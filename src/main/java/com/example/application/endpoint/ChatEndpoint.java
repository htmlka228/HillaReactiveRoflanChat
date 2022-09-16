package com.example.application.endpoint;

import com.example.application.dto.MessageDTO;
import com.example.application.security.SecurityService;
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
        messageDTO.setTime(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        messageDTO.setUserName(securityService.getAuthenticatedUserUsername()); //TODO it should get User model fully
        chatSink.emitNext(
                messageDTO,
                (signalType, emitResult) -> emitResult.equals(Sinks.EmitResult.FAIL_NON_SERIALIZED)
        );
    }
}
