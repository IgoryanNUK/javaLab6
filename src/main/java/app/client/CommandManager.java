package app.client;

import app.messages.commands.Command;
import app.exceptions.KnownException;
import app.exceptions.NoSuchCommand;
import app.exceptions.RecursiveCallError;
import app.exceptions.UnknownException;
import app.messages.requests.Request;
import app.messages.commands.*;

import java.util.ArrayDeque;
import java.util.HashMap;

public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();
    private final ArrayDeque<String> history = new ArrayDeque<>(14);
    private Client app;


   public CommandManager(Client app) {
       this.app = app;

        addCommand(new Exit(app), new RemoveById(), new Show(),
                new RemoveGreater(), new RemoveLower(), new RemoveByPartNumber(),
                new Help(this), new Clear(), new Info(), new History(this),
                new FilterStartsWithPN(), new FilterGreaterThanUOM());
//       addCommand(new Add(), new Update(), new Save(),
//                new ExecuteScript();
    }

    /**
     * Добавляет команды в функционал приложения.
     * @param cs команды
     */
    public void addCommand(Command ...cs) {
        for (Command c : cs) {
            commands.put(c.getName().split(" ")[0], c);
        }
    }

    /** Обрабатывает команду из входного потока.
     *
     * @param ioManager менеджер ввода-вывода приложения
     */
    public void handleCommand(UserIOManager ioManager) {
        try {
            String[] commandRequest = ioManager.getInput().readLine().trim().split(" ");
            Request req = getRequestByName(commandRequest, ioManager);
            if (req!=null) {
                ClientConnectionManager cm = new ClientConnectionManager(4027, "Igoryan-Laptop");
                ioManager.getOutput().println(cm.askServer(req));
            }
        } catch (StackOverflowError e) {
            throw new RecursiveCallError(e.getMessage());
        } catch (KnownException k) {
            throw k;
        } catch (Exception e) {
            throw new UnknownException(e);
        }
    }

    /**
     * Возвращает объект запроса по текстовому запросу команды.
     *
     * @param stringCommand массив из команды и её аргументов
     * @return запрос, готовый для отправления серверу
     */
    public Request getRequestByName(String[] stringCommand, UserIOManager ioManager) {
        if (!stringCommand[0].isEmpty()) {
            Command command = getCommandByName(stringCommand[0]);
            if (command == null) {
                throw new NoSuchCommand("command");
            } else {
                addInHistory(stringCommand[0]);
                return command.prepareRequest(stringCommand, ioManager);
            }
        }
        return null;
    }

    /**
     * Возвращает объект команды по её имени.
     * @param request имя команды
     * @return объект команды
     */
    public Command getCommandByName(String request) {
        return commands.get(request);
    }

    private void addInHistory(String command) {
        if (history.size() == 14) {
            history.removeFirst();
        }
        history.add(command);
    }

    /**
     * Выводит информацию обо всех командах.
     *
     * @param io менеджер ввода-вывода
     */
    public void showCommands(UserIOManager io) {
        commands.values().forEach(c -> io.println(c.getName() + " | " + c.getDescription()));
    }

    public void showHistory(UserIOManager io) {
        history.forEach(io::println);
    }
}
