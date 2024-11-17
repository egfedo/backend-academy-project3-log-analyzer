package backend.academy.egfedo.filter;

import backend.academy.egfedo.data.LogRecord;
import java.util.List;
import java.util.Objects;

public class CompositeLogRecordFilter implements LogRecordFilter {

    private final List<LogRecordFilter> filters;

    public CompositeLogRecordFilter(List<LogRecordFilter> filters) {
        this.filters = Objects.requireNonNull(filters);
    }

    @Override
    public boolean test(LogRecord logRecord) {
        for (LogRecordFilter filter : filters) {
            if (!filter.test(logRecord)) {
                return false;
            }
        }
        return true;
    }
}
