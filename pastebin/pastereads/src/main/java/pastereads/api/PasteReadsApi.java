package pastereads.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@OpenAPIDefinition(
        info = @Info(
                title = "Paste Reads API",
                version = "v1"
        )
)
public interface PasteReadsApi {

    @Operation(summary = "Retrieve paste by unique ID", operationId = "getPaste", description = "")
    @ApiResponse(responseCode = "200", description = "Paste retrieved successfully",
            links = {
                    @Link(name = "self", operationId = "getPaste",
                            parameters = @LinkParameter(name = "uuid", expression = "$request.path.uuid")),
                    @Link(name = "getUser", operationRef = "#/paths/v1/user/{id}/get",
                            parameters = @LinkParameter(name = "userId", expression = "$response.body#/userId"),
                            description = "The `userId` value returned in the paste can be used as the `id` parameter in `GET /v1/user/{id}/get`.")
            })
    @ApiResponse(responseCode = "404", description = "Paste not found")
    @Get(value = "/v1/paste/{uuid}", produces = MediaType.APPLICATION_HAL_JSON)
    default Single<HttpResponse<Paste>> getPaste(@NotNull @Parameter(description = "API key for traffic shaping") @Header(value = "ApiKey") String apiKey
            , @Parameter(description = "Paste's unique ID") @PathVariable("uuid") String uuid, HttpRequest<?> request
    ) {
        return Single.fromCallable(() -> {
            throw new UnsupportedOperationException();
        });
    }
}
