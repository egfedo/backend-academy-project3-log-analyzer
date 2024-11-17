package backend.academy.egfedo.format.impl;

import backend.academy.egfedo.data.LogReport;
import backend.academy.egfedo.util.HTTPStatusCodes;
import backend.academy.egfedo.util.ReportConfig;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ReportMDFormatter extends AbstractReportFormatter {

    public String formatNumber(long num) {
        return num >= 0 ? String.valueOf(num) : "-";
    }

    @SuppressFBWarnings(value = "VA_FORMAT_STRING_USES_NEWLINE", justification = "Makes it less readable")
    @Override
    public String format(LogReport logReport, List<String> fileNames, LocalDateTime from, LocalDateTime to) {
        StringBuilder sb = new StringBuilder();
        sb.append("#### Общая информация\n\n");
        sb.append("| Метрика | Значение |\n");
        sb.append("| :---: | ---: |\n");
        sb.append("| Файл(-ы) | `");
        sb.append(String.join("`<br>`", fileNames));
        sb.append("` |\n");
        sb.append(String.format("| Начальное время | %s |\n", Objects.nonNull(from) ? from.toString() : "-"));
        sb.append(String.format("| Конечное время | %s |\n", Objects.nonNull(to) ? to.toString() : "-"));
        sb.append(String.format("| Количество запросов | %d |\n", logReport.reqCount()));
        sb.append(String.format("| Средний размер ответа | %sb |\n", formatNumber(logReport.averageResSize())));
        sb.append(String.format("| Медиана размера ответа | %sb |\n", formatNumber(logReport.medianResSize())));

        sb.append(String.format("| 95p размера ответа | %sb |\n", formatNumber(logReport.percentileResSize())));
        sb.append(String.format("| Макс. размер ответа | %sb |\n\n", formatNumber(logReport.maxResSize())));


        sb.append("""
            #### Запрашиваемые ресурсы

            | Ресурс | Количество |
            | :---: | ---: |
            """);

        for (var entry : logReport.topResources()) {
            sb.append(String.format("| `%s` | %d |\n", entry.getLeft(), entry.getRight()));
        }
        sb.append('\n');

        sb.append("""
            #### Коды ответа

            | Код | Имя | Количество |
            | :---: | :---: | ---: |
            """);

        var sortedCodes = statusCodesToSortedList(logReport.statusCodes());

        for (int i = 0; i < sortedCodes.size() && i < ReportConfig.TOP_N_CODES; i++) {
            var entry = sortedCodes.get(i);
            var codeName = HTTPStatusCodes.getCodeName(entry.getKey());
            sb.append(String.format("| %d | %s | %d |\n", entry.getKey(), codeName, entry.getValue()));
        }
        sb.append('\n');

        return sb.toString();
    }
}
