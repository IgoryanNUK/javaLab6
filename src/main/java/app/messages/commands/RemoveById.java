package app.messages.commands;

import app.client.Client;
import app.client.UserIOManager;
import app.exceptions.WrongCommandFormat;
import app.messages.requests.RemoveReq;
import app.messages.requests.Request;
import app.server.Communicator;

public class RemoveById extends Command {

    {
        name = "remove_by_id {id}";
        description = "Удаляет элемент по заданному id";
    }

    @Override
    public Request prepareRequest(String[] args, UserIOManager ioManager, Client app) {
        if (args.length != 2) {throw new WrongCommandFormat(name);}
        int id;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException n) {throw new WrongCommandFormat(name);}

        return new RemoveReq(p -> p.getId() == id);
    }
}
