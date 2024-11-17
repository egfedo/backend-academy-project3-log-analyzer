package backend.academy;

import backend.academy.egfedo.Program;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        var program = new Program(System.out, System.err);
        program.run(args);
    }
}
