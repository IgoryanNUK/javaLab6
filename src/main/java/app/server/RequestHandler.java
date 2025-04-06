package app.server;

import app.exceptions.RequestReadingException;
import app.messages.requests.*;
import app.messages.response.MessageResp;
import app.messages.response.ProductsResp;
import app.messages.response.Response;
import app.product.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
            case RequestType.ADD -> add((AddReq) req);
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

    private Response add(AddReq req) {
        String jsonMessage = req.getJsonMessage();
        System.out.println("message: " + jsonMessage);

        ObjectMapper oM = new ObjectMapper();
        String partNumber;
        Double manufactureCost;
        String name;
        double x;
        double y;
        float price;
        UnitOfMeasure unitOfMeasure;
        String ownerName;
        float height;
        Color eyeColor;
        Country nationality;
        try {
            JsonNode jsonNode = oM.readTree(jsonMessage);

            name = jsonNode.get("name").asText();
            x = jsonNode.get("x").asDouble();
            y = jsonNode.get("y").asDouble();
            price = (float) jsonNode.get("price").asDouble();

            partNumber = jsonNode.get("partNumber").asText();

            manufactureCost = jsonNode.get("manufactureCost").asDouble();

            String unitOfMeasureString = jsonNode.get("unitOfMeasure").asText();
            unitOfMeasure = getEnum(unitOfMeasureString, UnitOfMeasure.values());

            ownerName = jsonNode.get("ownerName").asText();

            height = (float) jsonNode.get("height").asDouble();

            String eyeColorString = jsonNode.get("eyeColor").asText();
            eyeColor = getEnum(eyeColorString, Color.values());

            String nationalityString = jsonNode.get("nationality").asText();
            nationality = getEnum(nationalityString, Country.values());

        } catch (Exception e) {
            throw new RequestReadingException(RequestType.ADD, e);
        }

        collection.addProduct(new Product(name, new Coordinates(x, y), price, partNumber, manufactureCost,
                unitOfMeasure, new Person(ownerName, height, eyeColor, nationality)));

        return new MessageResp("***Продукт " + name + " успешно добавлен в коллекцию***");


    }

    private <E extends Enum<E>> E getEnum(String string, E[] values) {
        for (E val : values) {
            if (val.toString().equals(string)) return val;
        }
        return null;
    }
}
