package app.messages.requests;

import java.io.Serializable;

public interface Request extends Serializable {
    RequestType getType();
}
