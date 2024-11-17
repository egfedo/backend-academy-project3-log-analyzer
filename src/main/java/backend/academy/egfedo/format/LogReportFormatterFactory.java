package backend.academy.egfedo.format;

import backend.academy.egfedo.data.FormatType;
import backend.academy.egfedo.format.impl.ReportADFormatter;
import backend.academy.egfedo.format.impl.ReportMDFormatter;

public class LogReportFormatterFactory {

    public LogReportFormatter getFormatter(FormatType formatType) {
        return switch (formatType) {
            case MARKDOWN -> new ReportMDFormatter();
            case ADOC -> new ReportADFormatter();
        };
    }
}
