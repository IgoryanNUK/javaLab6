package app.messages.commands;

import java.io.Serializable;

public interface Request extends Serializable {
    String getName();
    void execute();
}
