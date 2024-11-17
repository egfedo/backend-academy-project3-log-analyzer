package backend.academy.egfedo.util;

import backend.academy.egfedo.data.FormatType;
import backend.academy.egfedo.data.LogRecordField;
import com.beust.jcommander.Parameter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class CLIParameters {
    @Parameter(names = "-path")
    private String path;

    @Parameter(names = "-from", converter = OffsetDateArgumentConverter.class)
    private LocalDateTime from;

    @Parameter(names = "-to", converter = OffsetDateArgumentConverter.class)
    private LocalDateTime to;

    @Parameter(names = "-format")
    private FormatType formatType;

    @Parameter(names = "-filter-field", variableArity = true)
    private List<LogRecordField> filterField = new ArrayList<>();

    @Parameter(names = "-filter-value", variableArity = true)
    private List<String> filterValue = new ArrayList<>();

}
