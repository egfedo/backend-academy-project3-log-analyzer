package backend.academy.egfedo.data;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record LogRecord(
    String remoteAddr,
    String remoteUser,
    LocalDateTime timeLocal,
    Request request,
    int status,
    int bodyBytesSent,
    String httpReferer,
    String httpUserAgent
) {
    public String getField(LogRecordField field) {
        return switch (field) {
            case ADDR -> remoteAddr;
            case USER -> remoteUser;
            case METHOD -> request.method();
            case RESOURCE -> request.resource();
            case PROTOCOL -> request.protocol();
            case STATUS -> String.valueOf(status);
            case BYTES_SENT -> String.valueOf(bodyBytesSent);
            case REFERER -> httpReferer;
            case AGENT -> httpUserAgent;
        };
    }
}
