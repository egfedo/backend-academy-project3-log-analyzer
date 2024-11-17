package backend.academy.egfedo.filter;

import backend.academy.egfedo.data.LogRecord;

public interface LogRecordFilter {

    boolean test(LogRecord logRecord);

}
