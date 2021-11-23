package pastereads.api;

import java.net.URI;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.hateoas.Link;
import io.reactivex.Single;

import static io.micronaut.http.HttpHeaders.*;

@Controller
public class PasteReadsController implements PasteReadsApi {

    public static final URI USER_API_PATH = URI.create("/v1/user/");
    @Value("${application.http.cache.max-age:1228}")
    private String maxAge;

    @Override
    public Single<HttpResponse<Paste>> getPaste(String apiKey, String uuid, HttpRequest<?> request) {
        Paste paste = getPasteMetadata(uuid);
        if (paste.getETag().equals(request.getHeaders().get(IF_NONE_MATCH))) {
            return Single.just(HttpResponse.<Paste>notModified()
                    .header(LOCATION, request.getUri().toString()));
        }
        // We only get the content if the ETag differs
        paste.setContent(getPasteContent(uuid));
        paste.link(Link.SELF, Link.of(request.getUri()));
        paste.link("getUser", Link.of(USER_API_PATH.resolve(paste.getUserId())));
        return Single.just(
            HttpResponse.ok(paste).header(CACHE_CONTROL, "public, max-age=" + maxAge)
                    .header(ETAG, paste.getETag()));
    }

    private Paste getPasteMetadata(String uuid) {
        var paste = new Paste();
        paste.setUuid(uuid);
        paste.setAlias("coreModel");
        paste.setTitle("Hello World in Java!");
        paste.setUserId("13");
        return paste;
    }

    private String getPasteContent(String uuid) {
        return "psvm(String[] args) { Hello world in Java }";
    }
}
