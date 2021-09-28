package users.api;

import io.micronaut.http.HttpResponse;
import io.reactivex.Single;

public class UsersController implements UsersApi {

    @Override
    public Single<HttpResponse<User>> getUser(String apiKey, String id) {
        return UsersApi.super.getUser(apiKey, id);
    }

    @Override
    public Single<HttpResponse<Void>> createUser(String apiKey, User body) {
        return UsersApi.super.createUser(apiKey, body);
    }

}
