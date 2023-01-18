package com.dreamx.tasks.handler;

import com.dreamx.tasks.model.User;
import com.dreamx.tasks.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class UserHandler {
    private final static String IDEMPOTENCE_HEADER = "X-Idempotency-Key";
    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Mono<ServerResponse> create(ServerRequest request) {

        String key = Optional.ofNullable(request.headers().firstHeader(IDEMPOTENCE_HEADER))
                .orElseThrow(() -> new IllegalArgumentException("Idempotency key is required"));

        return request.bodyToMono(User.class)
                .flatMap(user -> userService.createUser(key, user))
                .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(user));
    }

    public Mono<ServerResponse> getUserById(ServerRequest request) {
        String userId = request.pathVariable("userId");
        validateUserId(userId);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.findById(Long.valueOf(userId)), User.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User Id cannot be empty");
        }
    }


}
