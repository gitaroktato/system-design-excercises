package pastereads.api;

import static io.micronaut.http.HttpHeaders.CACHE_CONTROL;

import java.net.URI;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.hateoas.Link;
import io.reactivex.Single;

@Controller
public class PasteReadsController implements PasteReadsApi {

    public static final URI USER_API_PATH = URI.create("/v1/user/");
    @Value("${application.http.cache.max-age:1228}")
    private String maxAge;

    @Override
    public Single<HttpResponse<Paste>> getPaste(String apiKey, String uuid, HttpRequest<?> request) {
        var paste = new Paste();
        paste.setUuid(uuid);
        paste.setAlias("coreModel");
        paste.setTitle("Hello World in Java!");
        paste.setContent("psvm(String[] args) { Hello world in Java }");
        paste.setUserId("13");
        paste.link(Link.SELF, Link.of(request.getUri()));
        paste.link("getUser", Link.of(USER_API_PATH.resolve(paste.getUserId())));
        return Single.just(
            HttpResponse.ok(paste).header(CACHE_CONTROL, "public, max-age=" + maxAge));
    }
}
