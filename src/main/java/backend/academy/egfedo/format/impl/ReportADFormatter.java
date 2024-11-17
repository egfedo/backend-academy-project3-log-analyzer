package backend.academy.egfedo.format.impl;

import backend.academy.egfedo.data.LogReport;
import backend.academy.egfedo.util.HTTPStatusCodes;
import backend.academy.egfedo.util.ReportConfig;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ReportADFormatter extends AbstractReportFormatter {

    public String formatNumber(long num) {
        return num >= 0 ? String.valueOf(num) : "-";
    }

    @SuppressWarnings("MultipleStringLiterals")
    @SuppressFBWarnings(value = "VA_FORMAT_STRING_USES_NEWLINE", justification = "Makes it less readable")
    @Override
    public String format(LogReport logReport, List<String> fileNames, LocalDateTime from, LocalDateTime to) {
        StringBuilder sb = new StringBuilder();
        sb.append("==== Общая информация\n\n");
        sb.append("""
            [cols="^1,>.^1"]
            |===
            ^|Метрика ^|Значение

            """);

        sb.append("|Файл(-ы)\n|`");
        sb.append(String.join("`\n`", fileNames));
        sb.append("`\n");
        sb.append(String.format("|Начальное время\n|%s\n", Objects.nonNull(from) ? from.toString() : "-"));
        sb.append(String.format("|Конечное время\n|%s\n", Objects.nonNull(to) ? to.toString() : "-"));
        sb.append(String.format("|Количество запросов\n|%d\n", logReport.reqCount()));
        sb.append(String.format("|Средний размер ответа\n|%sb\n", formatNumber(logReport.averageResSize())));
        sb.append(String.format("|Медиана размера ответа\n|%sb\n", formatNumber(logReport.medianResSize())));

        sb.append(String.format("|95p размера ответа\n|%sb\n", formatNumber(logReport.percentileResSize())));
        sb.append(String.format("|Макс. размер ответа\n|%sb\n", formatNumber(logReport.maxResSize())));
        sb.append("|===\n\n");

        sb.append("""
            ==== Запрашиваемые ресурсы

            [cols="^1,>.^1"]
            |===
            ^|Ресурс ^|Количество

            """);

        for (var entry : logReport.topResources()) {
            sb.append(String.format("|`%s`\n|%d\n", entry.getLeft(), entry.getRight()));
        }
        sb.append("|===\n\n");

        sb.append("""
            ==== Коды ответа

            [cols="^1,^.^1,>.^1"]
            |===
            ^|Код ^|Имя ^|Количество

            """);

        var sortedCodes = statusCodesToSortedList(logReport.statusCodes());

        for (int i = 0; i < sortedCodes.size() && i < ReportConfig.TOP_N_CODES; i++) {
            var entry = sortedCodes.get(i);
            var codeName = HTTPStatusCodes.getCodeName(entry.getKey());
            sb.append(String.format("|%d\n|%s\n|%d\n", entry.getKey(), codeName, entry.getValue()));
        }
        sb.append("|===\n\n");

        return sb.toString();
    }
}
