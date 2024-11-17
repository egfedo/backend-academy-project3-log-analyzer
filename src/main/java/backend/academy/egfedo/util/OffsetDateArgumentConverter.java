package backend.academy.egfedo.util;

import com.beust.jcommander.IStringConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class OffsetDateArgumentConverter implements IStringConverter<LocalDateTime> {

    private DateTimeFormatter formatter =
        new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd['T'HH:mm:ss]")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    @Override
    public LocalDateTime convert(String s) {
        return LocalDateTime.parse(s, formatter);
    }
}
