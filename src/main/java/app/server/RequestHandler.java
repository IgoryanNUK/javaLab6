package app.server;

import app.messages.requests.RemoveReq;
import app.messages.requests.Request;
import app.messages.requests.RequestType;
import app.messages.response.MessageResp;
import app.messages.response.Response;

public class RequestHandler {
    private CollectionManager collection;

    public RequestHandler(CollectionManager collection) {
        this.collection = collection;
    }

    public Response handleRequest(Request req) {
        RequestType type = req.getType();

        return switch (type) {
            case RequestType.REMOVE -> removeIf((RemoveReq) req);
            default -> new MessageResp("Ошибка чтения комманды");
        };
    }

    public Response removeIf(RemoveReq req) {
        String message;
        if (collection.removeIf(req.getPredicate())) {
            message = "Продукт(ы) успешно удален(ы).";
        } else {
            message = "Не нашёл подходящих продуктов(.";
        }
        return new MessageResp(message);
    }
}
