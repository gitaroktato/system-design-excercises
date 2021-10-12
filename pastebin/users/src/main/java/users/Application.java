package users;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
