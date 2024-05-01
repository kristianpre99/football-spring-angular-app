package it.kristianp.footballbackendwebapp.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;


@Data
@Component
@Validated
@Configuration
@ConfigurationProperties(prefix = "football-app")
public class FootballAppConfigProperties {

    @NotNull
    private String transfermarktBaseRestApiUrl;
    private boolean batchReaderQueryLimit;

}