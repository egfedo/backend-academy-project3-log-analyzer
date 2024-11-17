package backend.academy.egfedo.filter;

import backend.academy.egfedo.data.LogRecord;
import backend.academy.egfedo.data.LogRecordField;
import backend.academy.egfedo.data.Request;
import backend.academy.egfedo.format.LogReportFormatter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class LogRecordFieldFilterTest {

    private static Stream<Arguments> logRecordProvider() {
        LogRecord record1 = new LogRecord(
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
        );

        return Stream.of(
            Arguments.of(
                record1,
                LogRecordField.ADDR,
                "217.168.17.5",
                true
            ),
            Arguments.of(
                record1,
                LogRecordField.ADDR,
                "^217",
                true
            ),
            Arguments.of(
                record1,
                LogRecordField.ADDR,
                "69",
                false
            ),
            Arguments.of(
                record1,
                LogRecordField.USER,
                "-",
                true
            ),
            Arguments.of(
                record1,
                LogRecordField.USER,
                "dja",
                false
            ),
            Arguments.of(
                record1,
                LogRecordField.METHOD,
                "GET",
                true
            ),
            Arguments.of(
                record1,
                LogRecordField.METHOD,
                "G..",
                true
            ),
            Arguments.of(
                record1,
                LogRecordField.METHOD,
                "dja",
                false
            ),
            Arguments.of(
                record1,
                LogRecordField.RESOURCE,
                "/\\w*/\\w*/\\w*",
                true
            ),
            Arguments.of(
                record1,
                LogRecordField.RESOURCE,
                "/\\w*/\\w*/\\w*/\\w*",
                false
            ),
            Arguments.of(
                record1,
                LogRecordField.STATUS,
                "^2",
                true
            ),
            Arguments.of(
                record1,
                LogRecordField.STATUS,
                "^5",
                false
            )
        );
    }

    @ParameterizedTest
    @MethodSource("logRecordProvider")
    void fieldFilter_shouldTestCorrectly(LogRecord logRecord, LogRecordField field,
        String pattern, boolean expected) {

    }
}
