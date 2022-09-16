package com.example.application.endpoint;

import com.example.application.model.Message;
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

    private Many<Message> chatSink;
    private Flux<Message> chat;

    public ChatEndpoint() {
        chatSink = Sinks.many().multicast().directBestEffort();
        chat = chatSink.asFlux().replay(10).autoConnect();
    }

    public @Nonnull Flux<@Nonnull Message> join() {
        return chat;
    }

    public void send(Message message) {
        message.setTime(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        message.setUserName(securityService.getAuthenticatedUserUsername()); //TODO it should get User model fully
        chatSink.emitNext(
                message,
                (signalType, emitResult) -> emitResult.equals(Sinks.EmitResult.FAIL_NON_SERIALIZED)
        );
    }
}
