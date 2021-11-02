package pastewrites.api;

import java.net.URI;
import java.util.UUID;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.reactivex.Single;

@Controller
public class PasteWritesController implements PasteWritesApi {

    @Override
    public Single<HttpResponse<Void>> createPaste(String apiKey, Paste body, HttpRequest<?> request) {
        return Single.just(HttpResponse.created(
            URI.create(request.getUri() + "/" + UUID.randomUUID())));
    }

}
