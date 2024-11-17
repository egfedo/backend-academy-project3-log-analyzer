package backend.academy.egfedo.format.impl;

import backend.academy.egfedo.format.LogReportFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public abstract class AbstractReportFormatter implements LogReportFormatter {

    protected List<Map.Entry<Integer, Long>> statusCodesToSortedList(Map<Integer, Long> statusCodes) {
        return statusCodes.entrySet().stream()
            .sorted(Comparator.<Map.Entry<Integer, Long>>comparingLong(Map.Entry::getValue).reversed())
            .toList();
    }
}
