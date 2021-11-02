package users.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@OpenAPIDefinition(
        info = @Info(
                title = "Users API",
                version = "v1"
        )
)
public interface UsersApi {

    @Operation(summary = "Retrieve user by synthetic ID", operationId = "getUser", description = "")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully",
            links = @Link(name = "self", operationId = "getUser",
                    parameters = @LinkParameter(name = "id", expression = "$request.path.id")))
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal Error")
    @ApiResponse(responseCode = "503", description = "Service Temporarily Unavailable")
    @Get(value = "/v1/user/{id}", produces = MediaType.APPLICATION_HAL_JSON)
    default Single<HttpResponse<User>> getUser(@NotNull @Parameter(description = "API key for traffic shaping") @Header(value = "ApiKey") String apiKey
            , @Parameter(description = "User's synthetic ID") @PathVariable("id") String id, HttpRequest<?> request
    ) {
        return Single.fromCallable(() -> {
            throw new UnsupportedOperationException();
        });
    }

    @Operation(summary = "Create a new user", operationId = "user", description = "")
    @ApiResponse(responseCode = "201", description = "User successfully created",
        headers = @io.swagger.v3.oas.annotations.headers.Header(name = "location",
                schema = @Schema(type = "string", format = "URL", description = "The newly created User's URL", example = "/v1/user/{id}")))
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Error")
    @ApiResponse(responseCode = "503", description = "Service Temporarily Unavailable")
    @Post(value = "/v1/user", consumes = {MediaType.APPLICATION_HAL_JSON, MediaType.APPLICATION_JSON})
    default Single<HttpResponse<Void>> createUser(@NotNull @Parameter(description = "API key for traffic shaping") @Header(value = "ApiKey") String apiKey
            , @Parameter(description = "") @Valid @Body User body, HttpRequest<?> request
    ) {
        return Single.fromCallable(() -> {
            throw new UnsupportedOperationException();
        });
    }

}
