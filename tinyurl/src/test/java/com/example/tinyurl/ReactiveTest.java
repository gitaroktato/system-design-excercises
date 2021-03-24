package com.example.tinyurl;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ReactiveTest {

    static class User {
        String username;
        String firstname;
        String lastname;

        public User(String username) {
            this.username = username;
        }

        public User(String username, String firstname, String lastname) {
            this.username = username;
            this.firstname = firstname;
            this.lastname = lastname;
        }

        @Override
        public String toString() {
            return "User{" +
                    "username='" + username + '\'' +
                    ", firstname='" + firstname + '\'' +
                    ", lastname='" + lastname + '\'' +
                    '}';
        }

        public String getUsername() {
            return username;
        }

        public String getFirstname() {
            return firstname;
        }

        public String getLastname() {
            return lastname;
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

    @Test
    public void testCapitalization() {
        var mono = Mono.just(new User("bob", "one", "two"));
        mono.map(u -> {
            u.username = u.username.toUpperCase();
            return u;
        }).map(u -> {
            u.firstname = u.firstname.toUpperCase();
            return u;
        }).map(u -> {
            u.lastname = u.lastname.toUpperCase();
            return u;
        }).subscribe(System.out::println);
    }

    @Test
    public void testAsyncCapitalization() {
        var mono = Mono.just(new User("bob", "one", "two"));
        mono.flatMap(this::asyncCapitalizeUser)
            .subscribe(System.out::println);
    }

    Mono<User> asyncCapitalizeUser(User u) {
        return Mono.just(
                new User(u.getUsername().toUpperCase(),
                u.getFirstname().toUpperCase(),
                u.getLastname().toUpperCase()));
    }

    @Test
    public void mergeFluxWithInterleave() {
        var flux1 = Flux.just("a", "b", "c");
        var flux2 = Flux.just("d", "e", "f");
        Flux.merge(flux1, flux2).subscribe(System.out::println);
    }

    @Test
    public void testBackpressure() throws InterruptedException {
        var fluxUser = Flux.just(new User("Skyler"),
                new User("Jessee"));
        var fluxLogged = fluxUser.doOnSubscribe(u -> System.out.println("Starring:"))
                .doOnNext(u -> System.out.println(u.getUsername()))
                .doOnComplete(() -> System.out.println("The end!"));
        StepVerifier.create(fluxLogged)
                .expectNextCount(2)
                .verifyComplete();
    }


    // TODO
    // https://stackoverflow.com/questions/59029446/java-reactor-stepverifier-withvirtualtime-loop-repeatedly-check-with-expectnoe
    // https://www.codota.com/code/java/methods/reactor.test.StepVerifier$Step/expectErrorSatisfies
    // https://stackoverflow.com/questions/45866364/reactor-stepverifier-withvirtualtime-blocks-indefinitely

}