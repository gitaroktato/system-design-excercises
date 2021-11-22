package users.api;

import java.time.LocalDate;
import java.util.Objects;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateoas.AbstractResource;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * User
 */
@Validated
@Generated(value = "io.swagger.codegen.v3.generators.java.MicronautCodegen",
        date = "2021-08-06T08:24:32.099Z[GMT]")
@Schema(allOf = {})
@Produces(MediaType.APPLICATION_HAL_JSON)
public class User extends AbstractResource<User> {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("creationDate")
  private LocalDate creationDate = null;

  @JsonProperty("lastLogin")
  private LocalDate lastLogin = null;

  public User id(String id) {
    this.id = id;
    return this;
  }

  /**
   * User's synthetic ID
   * @return id
  **/
  @Schema(description = "User's synthetic ID")

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public User name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  **/
  @Schema(required = true, description = "")
  @NotNull

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public User email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  **/
  @Schema(description = "")

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public User creationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
    return this;
  }

  /**
   * Get creationDate
   * @return creationDate
  **/
  @Schema(description = "")

  @Valid
  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public User lastLogin(LocalDate lastLogin) {
    this.lastLogin = lastLogin;
    return this;
  }

  /**
   * Get lastLogin
   * @return lastLogin
  **/
  @Schema(description = "")

  @Valid
  public LocalDate getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(LocalDate lastLogin) {
    this.lastLogin = lastLogin;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(this.id, user.id) &&
        Objects.equals(this.name, user.name) &&
        Objects.equals(this.email, user.email) &&
        Objects.equals(this.creationDate, user.creationDate) &&
        Objects.equals(this.lastLogin, user.lastLogin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, email, creationDate, lastLogin);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    lastLogin: ").append(toIndentedString(lastLogin)).append("\n");
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

  public String getETag() {
    return Integer.toHexString(hashCode());
  }
}
