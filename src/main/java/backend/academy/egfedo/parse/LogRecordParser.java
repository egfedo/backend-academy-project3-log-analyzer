package backend.academy.egfedo.parse;

import backend.academy.egfedo.data.LogRecord;
import backend.academy.egfedo.data.Request;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogRecordParser {

    private final Pattern linePattern = Pattern.compile(
        "(?<remoteAddr>.*?) - (?<remoteUser>\\S) \\[(?<timeLocal>.*?)] "
            + "\"(?<request>.*?)\" (?<status>\\d{3}) (?<bodyBytesSent>\\d+) "
            + "\"(?<httpReferer>.*?)\" \"(?<httpUserAgent>.*?)\""
    );

    private final DateTimeFormatter dateTimeFormatter =
        DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    public LogRecord parse(String line) {
        Matcher matcher = linePattern.matcher(line);
        if (matcher.matches()) {
            var reqList = matcher.group("request").split(" ");
            var request = new Request(
                reqList[0],
                reqList[1],
                reqList[2]
            );

            return new LogRecord(
                matcher.group("remoteAddr"),
                matcher.group("remoteUser"),
                LocalDateTime.parse(matcher.group("timeLocal"), dateTimeFormatter),
                request,
                Integer.parseInt(matcher.group("status")),
                Integer.parseInt(matcher.group("bodyBytesSent")),
                matcher.group("httpReferer"),
                matcher.group("httpUserAgent")
            );
        } else {
            throw new IllegalArgumentException("Неправильный формат строки в лог-файле: \"" + line + "\"");
        }
    }
}
