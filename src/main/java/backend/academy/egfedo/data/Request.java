package backend.academy.egfedo.data;

import lombok.Builder;

@Builder
public record Request(
    String method,
    String resource,
    String protocol
) {
}
