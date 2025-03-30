package app.messages.commands;

import app.client.Client;
import app.client.UserIOManager;
import app.messages.requests.Request;

import java.io.PrintStream;

public class Exit extends Command {
    {
        name = "exit";
        description = "Завершает приложение.";
    }


    @Override
    public Request prepareRequest(String[] args, UserIOManager ioManager, Client app) {
        PrintStream output = ioManager.getOutput();

        loop:
        while (true) {
            switch(ioManager.ask("Вы действительно хотите выйти? (y/n): ")) {
                case "y" -> {
                    app.stop();
                    break loop;
                }
                case "n" -> {break loop;}
                default -> ioManager.println("0_0 Неверный формат ввода. Пожалуйста, повторите попытку.");
            }
        }
        return null;
    }
}
