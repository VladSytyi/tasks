package com.dreamx.tasks.handler;

import com.dreamx.tasks.model.User;
import com.dreamx.tasks.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Service
public class UserHandler {
    private final static String IDEMPOTENCE_HEADER = "X-Idempotency-Key";
    private final static Scheduler SCHEDULER = Schedulers.boundedElastic();
    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        String key = Optional.ofNullable(request.headers().firstHeader(IDEMPOTENCE_HEADER))
                .orElseThrow(() -> new IllegalArgumentException("Idempotency key is required"));

        return request.bodyToMono(User.class)
                .flatMap(user -> userService.createUser(key, user))
                .flatMap(this::successResponse)
                .subscribeOn(SCHEDULER);
    }

    public Mono<ServerResponse> getUserById(ServerRequest request) {
        Long userId = Long.valueOf(request.pathVariable("userId"));

        return userService.findById(userId)
                .flatMap(this::successResponse)
                .switchIfEmpty(ServerResponse.notFound().build())
                .subscribeOn(SCHEDULER);
    }

    private Mono<ServerResponse> successResponse(User user) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(user);
    }

}
