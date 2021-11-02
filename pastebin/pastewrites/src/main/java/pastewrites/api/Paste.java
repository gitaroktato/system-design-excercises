package pastewrites.api;

import java.time.LocalDate;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateoas.AbstractResource;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Paste
 */
@Validated
@Produces(MediaType.APPLICATION_HAL_JSON)
@Schema(allOf = {})
public class Paste extends AbstractResource<Paste> {

    @JsonProperty("content")
    private String content = null;

    @JsonProperty("title")
    private String title = null;

    @JsonProperty("alias")
    private String alias = null;

    @JsonProperty("userId")
    private String userId = null;

    @JsonProperty("expiry")
    private LocalDate expiry = null;

    public Paste content(String content) {
        this.content = content;
        return this;
    }

    /**
     * The textual content of the paste.
     * @return content
     **/
    @Schema(required = true, description = "The textual content of the paste.")
    @NotNull

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Paste title(String title) {
        this.title = title;
        return this;
    }

    /**
     * An optional title for the paste.
     * @return title
     **/
    @Schema(required = true, description = "An optional title for the paste.")
    @NotNull

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Paste alias(String alias) {
        this.alias = alias;
        return this;
    }

    /**
     * The alias of the paste that becomes part of the URL.
     * @return alias
     **/
    @Schema(description = "The alias of the paste that becomes part of the URL.")

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Paste userId(String userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Get userId
     * @return userId
     **/
    @Schema(description = "")

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Paste expiry(LocalDate expiry) {
        this.expiry = expiry;
        return this;
    }

    /**
     * Get expiry
     * @return expiry
     **/
    @Schema(description = "")

    @Valid
    public LocalDate getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Paste paste = (Paste) o;
        return Objects.equals(this.content, paste.content) &&
                Objects.equals(this.title, paste.title) &&
                Objects.equals(this.alias, paste.alias) &&
                Objects.equals(this.userId, paste.userId) &&
                Objects.equals(this.expiry, paste.expiry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, title, alias, userId, expiry);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Paste {\n");

        sb.append("    content: ").append(toIndentedString(content)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    expiry: ").append(toIndentedString(expiry)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
