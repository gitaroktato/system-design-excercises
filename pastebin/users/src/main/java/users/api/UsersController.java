package users.api;

import java.net.URI;
import java.time.LocalDate;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.hateoas.Link;
import io.reactivex.Single;

@Controller
public class UsersController implements UsersApi {

    @Override
    public Single<HttpResponse<User>> getUser(String apiKey, String id, HttpRequest<?> request) {
        var user = new User();
        user.id("113");
        user.creationDate(LocalDate.now());
        user.name("John Smith");
        user.lastLogin(LocalDate.now());
        user.link(Link.SELF, Link.of(request.getUri()));
        return Single.just(HttpResponse.ok(user));
    }

    @Override
    public Single<HttpResponse<Void>> createUser(String apiKey, User body, HttpRequest<?> request) {
        return Single.just(HttpResponse.created(URI.create(request.getUri() + "/1")));
    }

}
