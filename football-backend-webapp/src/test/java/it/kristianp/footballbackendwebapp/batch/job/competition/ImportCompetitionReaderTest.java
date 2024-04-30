package it.kristianp.footballbackendwebapp.batch.job.competition;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

class ImportCompetitionReaderTest {


    @Test
    void normalizeUrlTest() throws URISyntaxException {
        String urlPrefix = "https://test-normalize.url.dev/";
        String urlSuffix = "/path/to/resource?page_number=1";

        String normalizedUrl = new URI(urlPrefix + urlSuffix).normalize().toString();
        String expectedUrl = "https://test-normalize.url.dev/path/to/resource?page_number=1";
        Assertions.assertNotNull(normalizedUrl);
        Assertions.assertEquals(normalizedUrl, expectedUrl);
    }

}