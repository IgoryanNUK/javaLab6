package app.messages.commands;

import app.server.CollectionManager;

import java.io.Serializable;

public interface Request extends Serializable {
    RequestType getType();
}
