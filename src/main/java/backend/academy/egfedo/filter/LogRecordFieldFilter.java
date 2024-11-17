package backend.academy.egfedo.filter;

import backend.academy.egfedo.data.LogRecord;
import backend.academy.egfedo.data.LogRecordField;
import java.util.regex.Pattern;

public class LogRecordFieldFilter implements LogRecordFilter {

    private final LogRecordField field;
    private final Pattern pattern;

    public LogRecordFieldFilter(LogRecordField field, String pattern) {
        this.field = field;
        this.pattern = Pattern.compile(pattern);
    }

    public boolean test(LogRecord logRecord) {
        return pattern.matcher(logRecord.getField(field)).find();
    }
}
