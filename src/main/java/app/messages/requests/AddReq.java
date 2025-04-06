package app.messages.requests;

public class AddReq implements Request {
    private final RequestType type = RequestType.ADD;
    private final String jsonMessage;

    public AddReq(String jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    @Override
    public RequestType getType() {return type;}

    public String getJsonMessage() {return jsonMessage;}
}
