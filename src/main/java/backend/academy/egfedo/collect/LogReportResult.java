package backend.academy.egfedo.collect;

import backend.academy.egfedo.data.LogRecord;
import backend.academy.egfedo.data.LogReport;
import backend.academy.egfedo.util.ReportConfig;
import com.datadoghq.sketch.ddsketch.DDSketch;
import com.datadoghq.sketch.ddsketch.DDSketches;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

public class LogReportResult {

    long reqCount = 0;
    double averageResSize = 0.0;
    Map<String, Long> resources = new HashMap<>();
    Map<Integer, Long> statusCodes = new HashMap<>();
    DDSketch percentileSketch = DDSketches.unboundedDense(ReportConfig.PERCENTILE_ACCURACY);

    public void accumulate(LogRecord logRecord) {
        double averageMult = averageResSize * reqCount;
        averageMult += logRecord.bodyBytesSent();
        reqCount++;
        averageResSize = averageMult / reqCount;

        percentileSketch.accept(logRecord.bodyBytesSent());

        long curr = statusCodes.getOrDefault(logRecord.status(), 0L);
        statusCodes.put(logRecord.status(), curr + 1);
        curr = resources.getOrDefault(logRecord.request().resource(), 0L);
        resources.put(logRecord.request().resource(), curr + 1);
    }

    public LogReportResult combine(LogReportResult other) {
        LogReportResult result = new LogReportResult();
        result.reqCount = reqCount + other.reqCount;
        result.averageResSize =
            (averageResSize * reqCount + other.averageResSize * other.reqCount) / (reqCount + other.reqCount);

        result.percentileSketch.mergeWith(percentileSketch);
        result.percentileSketch.mergeWith(other.percentileSketch);
        result.statusCodes.putAll(statusCodes);
        for (var key : other.statusCodes.keySet()) {
            result.statusCodes.put(key, statusCodes.getOrDefault(key, 0L) + other.statusCodes.get(key));
        }

        result.resources.putAll(resources);
        for (var key : other.resources.keySet()) {
            result.resources.put(key, resources.getOrDefault(key, 0L) + other.resources.get(key));
        }

        return result;
    }

    public LogReport toLogReport() {
        List<Pair<String, Long>> topResources = new ArrayList<>();
        for (var resource : resources.entrySet()) {
            for (int i = 0; i < topResources.size(); i++) {
                if (topResources.get(i).getRight() < resource.getValue()) {
                    topResources.add(i, Pair.of(resource.getKey(), resource.getValue()));
                    break;
                }
            }
            if (topResources.size() < ReportConfig.TOP_N_RESOURCES) {
                topResources.add(Pair.of(resource.getKey(), resource.getValue()));
            }
            if (topResources.size() > ReportConfig.TOP_N_RESOURCES) {
                topResources.removeLast();
            }
        }

        if (reqCount == 0) {
            return new LogReport(
                reqCount, -1,
                -1, -1, -1,
                topResources, statusCodes
            );
        }
        return new LogReport(
            reqCount, Math.round(averageResSize),
            Math.round(percentileSketch.getValueAtQuantile(ReportConfig.PERCENTILE)),
            Math.round(percentileSketch.getValueAtQuantile(ReportConfig.MEDIAN_PERCENTILE)),
            Math.round(percentileSketch.getMaxValue()),
            topResources, statusCodes
        );
    }
}
