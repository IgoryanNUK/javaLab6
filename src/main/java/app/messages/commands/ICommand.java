package app.messages.commands;

import java.io.Serializable;

public interface ICommand extends Serializable {
    String getName();
    String getDescription();
    Request prepareRequest(String[] args);
}
