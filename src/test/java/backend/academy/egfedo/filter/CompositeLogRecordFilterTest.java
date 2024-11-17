package backend.academy.egfedo.filter;

import backend.academy.egfedo.data.LogRecord;
import backend.academy.egfedo.data.LogRecordField;
import backend.academy.egfedo.data.Request;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CompositeLogRecordFilterTest {


    private static Stream<Arguments> logFilterCasesProvider() {
        List<LogRecord> defaultList = List.of(
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
            ),
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
            ),
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
        );
        return Stream.of(
            Arguments.of(
                List.of(),
                defaultList,
                defaultList
            ),
            Arguments.of(
                List.<LogRecordFilter>of(
                    (LogRecord el) -> el.timeLocal().isBefore(LocalDateTime.of(2016, 1, 1, 0, 0))
                ),
                defaultList,
                List.of(
                    defaultList.get(2)
                )
            ),
            Arguments.of(
                List.<LogRecordFilter>of(
                    (LogRecord el) -> el.timeLocal().isAfter(LocalDateTime.of(2016, 1, 1, 0, 0)),
                    new LogRecordFieldFilter(LogRecordField.METHOD, "DEL")
                ),
                defaultList,
                List.of(
                    defaultList.get(0)
                )
            ),
            Arguments.of(
                List.<LogRecordFilter>of(
                    (LogRecord el) -> el.timeLocal().isAfter(LocalDateTime.of(2016, 1, 1, 0, 0)),
                    (LogRecord el) -> el.timeLocal().isBefore(LocalDateTime.of(2017, 1, 1, 0, 0))
                ),
                defaultList,
                List.of()
            )
        );
    }


    @ParameterizedTest
    @MethodSource("logFilterCasesProvider")
    void logFilter_filtersCorrectly(List<LogRecordFilter> logRecordFilters,
        List<LogRecord> logRecords, List<LogRecord> expectedRecords) {

        CompositeLogRecordFilter filter = new CompositeLogRecordFilter(logRecordFilters);

        var filteredRecords = logRecords.stream().filter(filter::test).toList();

        assertThat(filteredRecords).isEqualTo(expectedRecords);
    }

}
