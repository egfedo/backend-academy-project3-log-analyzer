package backend.academy.egfedo.format;

import backend.academy.egfedo.data.LogReport;
import java.time.LocalDateTime;
import java.util.List;

public interface LogReportFormatter {

    String format(LogReport logReport, List<String> fileNames, LocalDateTime from, LocalDateTime to);

}
