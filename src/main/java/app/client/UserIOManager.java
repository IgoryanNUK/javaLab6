package app.client;

import app.exceptions.UnknownException;

import java.io.BufferedReader;
import java.io.IOException;
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

    public String ask(String request) {
        output.printf(request);
        try {
            return input.readLine();
        } catch (Exception e) {
            throw new UnknownException(e);
        }
    }

    public void println(String message) {
        output.println(message);
    }
}
