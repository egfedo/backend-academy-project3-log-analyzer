package backend.academy.egfedo.data;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

public record LogReport(
    long reqCount,
    long averageResSize,
    long percentileResSize,
    long medianResSize,
    long maxResSize,
    List<Pair<String, Long>> topResources,
    Map<Integer, Long> statusCodes
) {
}
