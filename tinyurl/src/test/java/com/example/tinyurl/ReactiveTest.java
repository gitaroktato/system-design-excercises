package com.example.tinyurl;

import java.time.Duration;
import java.util.stream.IntStream;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ReactiveTest {

    static class User {
        public static final User SAUL = new User("Saul");
        public static final User SKYLER = new User("skyler");
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

    @Test
    public void testOnErrorReturn() {
        var saul = new User("Saul");
        Mono<User> bogus = Mono.error(IllegalStateException::new);
        var resolved = Mono.from(bogus)
                .onErrorReturn(IllegalStateException.class, new User("Saul"));
        StepVerifier.create(resolved)
                .assertNext(user -> Assertions.assertThat(user.getUsername()).isEqualTo("Saul"))
                .verifyComplete();
    }

    @Test
    public void testOnErrorWithFlux() {
        Flux<User> errorneousFlux = Flux.error(IllegalStateException::new);
        var resolved = Flux.from(errorneousFlux)
                .onErrorResume(t -> Flux.just(new User("Bob")));
        StepVerifier.create(resolved)
                .assertNext(u -> Assertions.assertThat(u.getUsername()).isEqualTo("Bob"))
                .verifyComplete();
    }

    @Test
    public void testCapitalize() {
        var flux = Flux.just(new User("Bob"));
        StepVerifier.create(capitalizeMany(flux))
                .assertNext(u -> Assertions.assertThat(u.getUsername()).isEqualTo("BOB"))
                .verifyComplete();
    }

    Flux<User> capitalizeMany(Flux<User> flux) {
        return flux.map(u -> {
            try {
                return capitalizeUser(u);
            } catch (GetOutOfHereException e) {
                throw Exceptions.propagate(e);
            }
        });
    }

    User capitalizeUser(User user) throws GetOutOfHereException {
        if (user.equals(User.SAUL)) {
            throw new GetOutOfHereException();
        }
        return new User(user.getUsername().toUpperCase(),
                user.getFirstname(), user.getLastname());
    }

    protected final class GetOutOfHereException extends Exception {
        private static final long serialVersionUID = 0L;
    }

    @Test
    public void testAdapt() {
        var flux = Flux.empty();
        var flowable = Flowable.fromPublisher(flux);
        var flux2 = Flux.from(flowable);
        var observable = flowable.toObservable();
        Flux.from(observable.toFlowable(BackpressureStrategy.BUFFER));

        var single = Single.fromPublisher(Mono.empty());
        Mono.from(single.toFlowable());

        var future = Mono.empty().toFuture();
        Mono.fromFuture(future);
    }

    @Test
    public void testFluxZip() {
        Flux<String> usernameFlux = Flux.just("bob");
        Flux<String> firstnameFlux = Flux.just("Bob");
        Flux<String> lastnameFlux = Flux.just("Smith");
        var fluxUser = Flux.zip(usernameFlux, firstnameFlux, lastnameFlux)
                .map(tuple -> new User(tuple.getT1(), tuple.getT2(), tuple.getT3()));
        StepVerifier.create(fluxUser)
                .assertNext(u -> Assertions.assertThat(u)
                        .isEqualToComparingFieldByField(new User("bob", "Bob", "Smith")))
                .verifyComplete();

    }

    @Test
    public void testMonoFastest() {
        var mono1 = Mono.just(new User("sam"))
                .delayElement(Duration.ofSeconds(5));
        var mono2 = Mono.just(new User("davie"));
        var fastest = Mono.firstWithSignal(mono1, mono2);
        StepVerifier.create(fastest)
                .assertNext(u -> Assertions.assertThat(u)
                        .isEqualToComparingFieldByField(new User("davie")))
                .verifyComplete();
    }

    @Test
    public void testFluxToCompletion() {
        var flux = Flux.just("a");
        var monoEmpty =  flux.then(Mono.<Void>empty());
        StepVerifier.create(monoEmpty)
                .verifyComplete();
    }

    @Test
    public void testMonoWithNull() {
        User nullUser = null;
        var monoNull = Mono.justOrEmpty(nullUser);
        StepVerifier.create(monoNull)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void testMonoWithEmptyToDefault() {
        Mono<User> empty = Mono.empty();
        var defaultUser = empty.switchIfEmpty(Mono.just(User.SKYLER));
        StepVerifier.create(defaultUser)
                .expectNext(User.SKYLER)
                .verifyComplete();
    }

    @Test
    public void collectList() {
        var flux = Flux.just("a", "b", "c");
        var monoList = flux.collectList();
        StepVerifier.create(monoList)
                .assertNext( l -> Assertions.assertThat(l).containsOnly("a", "b", "c"))
                .verifyComplete();
    }


    // TODO
    // https://stackoverflow.com/questions/59029446/java-reactor-stepverifier-withvirtualtime-loop-repeatedly-check-with-expectnoe
    // https://www.codota.com/code/java/methods/reactor.test.StepVerifier$Step/expectErrorSatisfies
    // https://stackoverflow.com/questions/45866364/reactor-stepverifier-withvirtualtime-blocks-indefinitely

}