package app.client;

import java.io.BufferedReader;
import java.io.PrintStream;

public class UserIOManager {
    private final PrintStream output;
    private final BufferedReader input;

    public UserIOManager(BufferedReader input, PrintStream output) {
        this.output = output;
        this.input = input;
    }

    public PrintStream getOutput() {return output;}

    public BufferedReader getInput() {return input;}
}
