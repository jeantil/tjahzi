package pl.tkowalcz.tjahzi.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import pl.tkowalcz.tjahzi.log4j2.infra.IntegrationTest;

import static org.hamcrest.CoreMatchers.*;
import static pl.tkowalcz.tjahzi.log4j2.infra.LokiAssert.assertThat;

class LokiPropertiesFileTest extends IntegrationTest {

    @Test
    void shouldSendData() {
        // Given
        loadConfig("appender-test-with-log-label-set.properties");
        Logger logger = LogManager.getLogger(LokiPropertiesFileTest.class);

        String expectedLogLine = "Hello World";

        // When
        logger.info(expectedLogLine);

        // Then
        assertThat(loki)
                .returns(response -> response
                        .body("data.result.size()", equalTo(1))
                        .body("data.result[0].stream.server", equalTo("127.0.0.1"))
                        .body("data.result[0].stream.source", equalTo("log4j"))
                        .body(
                                "data.result.values",
                                hasItems(
                                        hasItems(
                                                hasItems(
                                                        containsString("INFO LokiPropertiesFileTest - " + expectedLogLine)
                                                )
                                        )
                                )
                        )
                );
    }
}
