package backend.academy.egfedo;

import backend.academy.egfedo.collect.LogReportResult;
import backend.academy.egfedo.data.FileData;
import backend.academy.egfedo.data.FormatType;
import backend.academy.egfedo.file.FileLoader;
import backend.academy.egfedo.filter.LogRecordFilter;
import backend.academy.egfedo.filter.LogRecordFilterBuilder;
import backend.academy.egfedo.format.LogReportFormatter;
import backend.academy.egfedo.format.LogReportFormatterFactory;
import backend.academy.egfedo.parse.LogRecordParser;
import backend.academy.egfedo.util.CLIParameters;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.stream.Collector;

public class Program {
    private final PrintStream out;
    private final PrintStream err;

    private CLIParameters parameters = new CLIParameters();

    public Program(PrintStream out, PrintStream err) {
        this.out = Objects.requireNonNull(out);
        this.err = Objects.requireNonNull(err);
    }

    @SuppressWarnings({"ReturnCount", "MagicNumber"})
    private boolean loadArguments(String[] args) {
        parameters = new CLIParameters();

        try {
            JCommander.newBuilder()
                .addObject(parameters)
                .build()
                .parse(args);
        } catch (ParameterException e) {
            var list = e.getMessage().split(" ");
            var parameter = e.getMessage().startsWith("Expected") ? list[list.length - 1] : list[3];
            err.println("Error: Ошибка в параметре `" + parameter
                + "`. Проверьте правильность значения параметра.");
            return true;
        } catch (DateTimeParseException e) {
            err.println("Error: Ошибка при чтении параметра времени. "
                + "Проверьте, что оно соответствует правильному формату.");
            return true;
        }

        if (Objects.isNull(parameters.path())) {
            err.println("Error: отсутствует обязательный параметр пути `-path`");
            return true;
        }

        if (Objects.nonNull(parameters.filterField()) ^ Objects.nonNull(parameters.filterValue())) {
            err.println("Error: если используются фильтрация, "
                + "то обязаны присутствовать оба параметра `-filter-field`, `-filter-value`.");
            return true;
        }

        if (Objects.nonNull(parameters.filterField())
            && parameters.filterField().size() != parameters.filterValue().size()) {
            err.println("Error: если используются фильтрация,"
                + " то количество значений у параметров `-filter-field`, `-filter-value` должно быть одинаковым.");
            return true;
        }

        return false;
    }

    public void run(String[] args) {
        loadArguments(args);

        var logParser = new LogRecordParser();

        var recordCollector = Collector.of(
            LogReportResult::new,
            LogReportResult::accumulate,
            LogReportResult::combine,
            LogReportResult::toLogReport
        );

        var builder = new LogRecordFilterBuilder();

        if (Objects.nonNull(parameters.from())) {
            builder.fromDateFilter(parameters.from());
        }
        if (Objects.nonNull(parameters.to())) {
            builder.toDateFilter(parameters.to());
        }
        if (Objects.nonNull(parameters.filterField()) && Objects.nonNull(parameters.filterValue())) {
            for (int i = 0; i < parameters.filterField().size(); i++) {
                builder.fieldFilter(
                    parameters.filterField().get(i),
                    parameters.filterValue().get(i)
                );
            }

        }

        LogRecordFilter filter = builder.build();

        LogReportFormatter formatter = new LogReportFormatterFactory().getFormatter(
            Objects.requireNonNullElse(parameters.formatType(), FormatType.MARKDOWN)
        );


        try (var fileLoader = new FileLoader()) {

            try (FileData fileData = fileLoader.getFileLines(parameters.path())) {
                if (fileData.fileNames().isEmpty()) {
                    err.println("Error: Не найдено ни одного файла по шаблону. Проверьте правильность шаблона.");
                    return;
                }
                var count = fileData.fileLines()
                    .map(logParser::parse)
                    .filter(filter::test)
                    .collect(recordCollector);
                out.println(formatter.format(count, fileData.fileNames(), parameters.from(), parameters.to()));
            }
        } catch (URISyntaxException e) {
            err.println("Error: У ссылки неправильный синтаксис.");
        } catch (IOException e) {
            err.println("Error: Ошибка ввода/вывода.");
        } catch (InterruptedException e) {
            err.println("Error: Ошибка прерывания.");
        } catch (Exception e) {
            err.println("Error: Произошла неизвестная ошибка.");
        }
    }
}
