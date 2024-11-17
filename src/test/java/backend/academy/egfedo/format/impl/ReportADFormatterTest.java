package backend.academy.egfedo.format.impl;

import backend.academy.egfedo.data.LogReport;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReportADFormatterTest {

    ReportADFormatter formatter = new ReportADFormatter();

    private static Stream<Arguments> logReportProvider() {
        return Stream.of(
            Arguments.of(
                new LogReport(
                    49,
                    403,
                    4309,
                    200,
                    4543,
                    List.of(
                        Pair.of("res1", 42L),
                        Pair.of("res2", 20L)
                    ),
                    Map.of(
                        200, 19L,
                        404, 20L,
                        500, 10L
                    )
                ),
                List.of("filename1"),
                null, null,
                """
                    ==== Общая информация

                    [cols="^1,>.^1"]
                    |===
                    ^|Метрика ^|Значение

                    |Файл(-ы)
                    |`filename1`
                    |Начальное время
                    |-
                    |Конечное время
                    |-
                    |Количество запросов
                    |49
                    |Средний размер ответа
                    |403b
                    |Медиана размера ответа
                    |200b
                    |95p размера ответа
                    |4309b
                    |Макс. размер ответа
                    |4543b
                    |===

                    ==== Запрашиваемые ресурсы

                    [cols="^1,>.^1"]
                    |===
                    ^|Ресурс ^|Количество

                    |`res1`
                    |42
                    |`res2`
                    |20
                    |===

                    ==== Коды ответа

                    [cols="^1,^.^1,>.^1"]
                    |===
                    ^|Код ^|Имя ^|Количество

                    |404
                    |Not Found
                    |20
                    |200
                    |OK
                    |19
                    |500
                    |Internal Server Error
                    |10
                    |===

                    """
            )
        );
    }

    @ParameterizedTest
    @MethodSource("logReportProvider")
    void reportFormatter_correctlyFormatsTheReport(LogReport report, List<String> fileNames,
        LocalDateTime from, LocalDateTime to, String expected) {

        String result = formatter.format(report, fileNames, from, to);
        assertThat(result).isEqualTo(expected);
    }

}
