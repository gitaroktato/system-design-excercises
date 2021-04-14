package com.example.tinyurl;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.stream.IntStream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ReactiveTest {

    static class User {
        String username;

        public User(String username) {
            this.username = username;
        }
    }

    @Test
    public void testFlux() {
        var flux = Flux.just("foo", "bar");
        StepVerifier.create(flux)
                .expectNext("foo", "bar")
                .verifyComplete();
    }

    @Test
    public void testFluxOnError() {
        Flux<String> fluxWithError = Flux.concat(Flux.just("foo", "bar"),
                Flux.error(new RuntimeException()));
        StepVerifier.create(fluxWithError)
                .expectNext("foo", "bar")
                .verifyError(RuntimeException.class);

    }

    @Test
    public void testFluxWithUser() {
        var user1 = new User("swhite");
        var user2 = new User("jpinkman");
        var flux = Flux.just(user1, user2);
        StepVerifier.create(flux)
                .assertNext(user -> Assertions.assertThat(user.username)
                                        .isEqualTo("swhite"))
                .assertNext(user -> Assertions.assertThat(user.username)
                                       .isEqualTo("jpinkman"))
                .verifyComplete();
    }

    @Test
    public void testFluxJustSlow() {
        var step = StepVerifier
                .withVirtualTime(() -> Flux.interval(Duration.ofSeconds(1)).take(3600))
                .expectSubscription();
        IntStream.range(0, 3600).forEach(i -> {
            step.expectNoEvent(Duration.ofSeconds(1));
            step.expectNext(Long.valueOf(i));
        });
        step.expectComplete().verify();
    }

    // TODO
    // https://stackoverflow.com/questions/59029446/java-reactor-stepverifier-withvirtualtime-loop-repeatedly-check-with-expectnoe
    // https://www.codota.com/code/java/methods/reactor.test.StepVerifier$Step/expectErrorSatisfies
    // https://stackoverflow.com/questions/45866364/reactor-stepverifier-withvirtualtime-blocks-indefinitely

}