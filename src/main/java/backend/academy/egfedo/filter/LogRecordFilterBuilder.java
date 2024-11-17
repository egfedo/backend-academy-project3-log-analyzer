package backend.academy.egfedo.filter;

import backend.academy.egfedo.data.LogRecordField;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogRecordFilterBuilder {
    private final List<LogRecordFilter> filters = new ArrayList<>();

    public LogRecordFilter build() {
        return new CompositeLogRecordFilter(filters);
    }

    public LogRecordFilterBuilder fromDateFilter(LocalDateTime date) {
        filters.add(elem -> elem.timeLocal().isAfter(date));
        return this;
    }

    public LogRecordFilterBuilder toDateFilter(LocalDateTime date) {
        filters.add(elem -> elem.timeLocal().isBefore(date));
        return this;
    }

    public LogRecordFilterBuilder fieldFilter(LogRecordField field, String value) {
        filters.add(new LogRecordFieldFilter(field, value));
        return this;
    }
}
