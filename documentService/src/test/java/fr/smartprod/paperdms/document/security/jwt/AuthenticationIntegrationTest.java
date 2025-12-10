package fr.smartprod.paperdms.document.security.jwt;

import fr.smartprod.paperdms.document.config.SecurityConfiguration;
import fr.smartprod.paperdms.document.config.SecurityJwtConfiguration;
import fr.smartprod.paperdms.document.config.WebConfigurer;
import fr.smartprod.paperdms.document.management.SecurityMetersService;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import tech.jhipster.config.JHipsterProperties;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        JHipsterProperties.class,
        WebConfigurer.class,
        SecurityConfiguration.class,
        SecurityJwtConfiguration.class,
        SecurityMetersService.class,
        JwtAuthenticationTestUtils.class,
    }
)
public @interface AuthenticationIntegrationTest {}
