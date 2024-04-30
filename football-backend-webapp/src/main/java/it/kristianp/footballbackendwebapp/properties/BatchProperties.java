package it.kristianp.footballbackendwebapp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.batch.job.reader.query")
public class BatchProperties {

    private boolean limited;
}
