package app.messages.commands;

/**
 * Команда, выводящая информацию о коллекции.
 */
public class Info extends Command {
    {
        name = "info";
        description = "Выводит информацио о коллекции.";
    }

    /*public void execute(Application app, String[] pars, BufferedReader input) {
        if (pars.length != 1) throw new WrongCommand(name);

        ProductManager pm = app.getProductManager();
        System.out.println("Данные о коллекции:" +
                "\nтип: " + pm.getCollectionName() +
                "\nдата инициализации: " + pm.getInitDate() +
                "\nколичество элементов: " + pm.getSize());
    }*/

    @Override
    public void execute() {
    }
 }
