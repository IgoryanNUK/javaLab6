package app.server;

import app.messages.requests.GetReq;
import app.messages.requests.RemoveReq;
import app.messages.requests.Request;
import app.messages.requests.RequestType;
import app.messages.response.MessageResp;
import app.messages.response.ProductsResp;
import app.messages.response.Response;
import app.product.Product;

import java.util.List;

public class RequestHandler {
    private CollectionManager collection;

    public RequestHandler(CollectionManager collection) {
        this.collection = collection;
    }

    public Response handleRequest(Request req) {
        RequestType type = req.getType();

        return switch (type) {
            case RequestType.REMOVE -> removeIf((RemoveReq) req);
            case RequestType.GET -> getIf((GetReq) req);
            case RequestType.INFO -> getInfo();
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

    public Response getIf(GetReq req) {
        List<Product> list = collection.getIf(req.getPredicate());
        return new ProductsResp(list);
    }

    public Response getInfo() {
        String resp = "Данные о коллекции:" +
                "\nтип: " + collection.getCollectionName() +
                "\nдата инициализации: " + collection.getInitDate() +
                "\nколичество элементов: " + collection.getSize();

        return new MessageResp(resp);
    }
}
