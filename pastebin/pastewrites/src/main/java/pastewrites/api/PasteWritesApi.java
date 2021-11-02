package pastewrites.api;

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
                title = "Paste Writes API",
                version = "v1"
        )
)
public interface PasteWritesApi {

    @Operation(summary = "Create a new paste", operationId = "paste", description = "" )
    @ApiResponse(responseCode = "201", description = "Paste successfully created",
        headers = @io.swagger.v3.oas.annotations.headers.Header(name = "location",
            schema = @Schema(type = "string", format = "URL", description = "The created Paste's URL", example = "/v1/paste/{uuid}")))
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Error")
    @ApiResponse(responseCode = "503", description = "Service Temporarily Unavailable")
    @Post(value = "/v1/paste", consumes = {MediaType.APPLICATION_HAL_JSON, MediaType.APPLICATION_JSON})
    default Single<HttpResponse<Void>> createPaste(@NotNull @Parameter(description = "API key for traffic shaping") @Header(value = "ApiKey") String apiKey
        ,@Parameter(description = "") @Valid @Body Paste body, HttpRequest<?> request
    ) {
        return Single.fromCallable(() -> {
            throw new UnsupportedOperationException();
        });
    }

}
