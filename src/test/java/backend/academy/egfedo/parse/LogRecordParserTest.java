package backend.academy.egfedo.parse;

import backend.academy.egfedo.data.LogRecord;
import backend.academy.egfedo.data.LogRecordField;
import backend.academy.egfedo.data.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LogRecordParserTest {


    private static Stream<Arguments> correctLinesProvider() {
        return Stream.of(
            Arguments.of(
                "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\"" +
                    " 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"",
                new LogRecord(
                    "93.180.71.3",
                    "-",
                    LocalDateTime.of(2015, 5, 17, 8, 5, 32),
                    new Request(
                        "GET",
                        "/downloads/product_1",
                        "HTTP/1.1"
                    ),
                    304,
                    0,
                    "-",
                    "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)"
                )
            ),
            Arguments.of(
                "217.168.17.5 - - [17/May/2015:08:05:12 +0000] \"GET /downloads/product_2 HTTP/1.1\"" +
                    " 200 3316 \"-\" \"-\"",
                new LogRecord(
                    "217.168.17.5",
                    "-",
                    LocalDateTime.of(2015, 5, 17, 8, 5, 12),
                    new Request(
                        "GET",
                        "/downloads/product_2",
                        "HTTP/1.1"
                    ),
                    200,
                    3316,
                    "-",
                    "-"
                )
            ),
            Arguments.of(
                "241.50.144.141 - - [15/Nov/2024:15:08:41 +0000] \"POST /workforce_Innovative.htm HTTP/1.1\"" +
                    " 200 1985 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_5_10 rv:2.0) Gecko/1942-21-08 Firefox/35.0\"",
                new LogRecord(
                    "241.50.144.141",
                    "-",
                    LocalDateTime.of(2024, 11, 15, 15, 8, 41),
                    new Request(
                        "POST",
                        "/workforce_Innovative.htm",
                        "HTTP/1.1"
                    ),
                    200,
                    1985,
                    "-",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_5_10 rv:2.0) Gecko/1942-21-08 Firefox/35.0"
                )
            ),
            Arguments.of(
                "1.206.107.209 - - [15/Nov/2024:15:08:41 +0000] \"DELETE " +
                    "/function_Assimilated-internet%20solution/complexity/even-keeled.htm HTTP/1.1\"" +
                    " 200 1804 \"-\" \"Mozilla/5.0 (Windows 98; Win 9x 4.90) AppleWebKit/5332 (KHTML, like Gecko)" +
                    " Chrome/38.0.851.0 Mobile Safari/5332\"",
                new LogRecord(
                    "1.206.107.209",
                    "-",
                    LocalDateTime.of(2024, 11, 15, 15, 8, 41),
                    new Request(
                        "DELETE",
                        "/function_Assimilated-internet%20solution/complexity/even-keeled.htm",
                        "HTTP/1.1"
                    ),
                    200,
                    1804,
                    "-",
                    "Mozilla/5.0 (Windows 98; Win 9x 4.90) AppleWebKit/5332 (KHTML, like Gecko) Chrome/38.0.851.0 Mobile Safari/5332"
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource("correctLinesProvider")
    void whenParsingLine_shouldProvideCorrectOutput(String line, LogRecord expected) {
        LogRecordParser parser = new LogRecordParser();

        LogRecord parsed = parser.parse(line);

        assertThat(parsed).isEqualTo(expected);
    }

    private static Stream<Arguments> incorrectLinesProvider() {
        return Stream.of(
            Arguments.of(
                "1.206.107.209 - "
            ),
            Arguments.of("93.180.71.3 - - [17/May/2015:08:05:32 +0000 \"GET /downloads/product_1 HTTP/1.1\"" +
                " 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\""
            ),
            Arguments.of("93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\"" +
                " 3044 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\""
            )
        );
    }

    @ParameterizedTest
    @MethodSource("incorrectLinesProvider")
    void whenIncorrectLine_shouldThrowException(String line) {
        LogRecordParser parser = new LogRecordParser();

        assertThrows(IllegalArgumentException.class, () -> parser.parse(line));
    }
}
