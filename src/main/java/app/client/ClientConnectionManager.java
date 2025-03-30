package app.client;

import app.messages.commands.Command;
import app.exceptions.UnavaluableServer;
import app.messages.commands.RemoveById;
import app.messages.requests.Request;
import app.messages.response.MessageResp;
import app.messages.response.Response;
import app.messages.response.ResponseType;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientConnectionManager {
    private int port;
    private InetAddress host;

    public static void main(String[] args) {
        /*Object o = new Object();
        try {
            ClientConnectionManager cc = new ClientConnectionManager(4027, "Igoryan-Laptop");
            byte[] arr = cc.serializeRequest(new Command() {
                @Override
                public String getName() {return "Прошло успешно!";}
            });
            for (byte a : arr) {
                System.out.println(a);
            }
        } catch (Exception e) {}*/
    }


    /**
     * Конструктор, определяющий сервер, с которым будет проводиться подклбчение.
     *
     * @param port порт сервера
     * @param hostName имя хоста сервера
     * @throws Exception непредвиденная ошибка
     */
    public ClientConnectionManager(int port, String hostName) throws Exception {
        this.port = port;
        host = InetAddress.getByName(hostName);
    }


    /**
     * Отправляет запрос серверу.
     *
     * @param req объект запроса
     * @throws Exception непредвиденая ошибка
     * @return ответ сервера
     */
    public String askServer(Request req) throws Exception {
        InetSocketAddress address = new InetSocketAddress(host, port);
        byte[] reqArray = serializeRequest(req);

        try (SocketChannel sock = SocketChannel.open()) {
            //установка соединения и настройка сокета
            sock.connect(address);
            sock.configureBlocking(false);

            ByteBuffer buff = ByteBuffer.wrap(reqArray);
            sock.write(buff);

            return readServerResponse(sock);
        } catch (java.net.ConnectException cE) {
            throw new UnavaluableServer();
        }
    }

    /**
     * Сериализует запросы.
     *
     * @param req объект запроса
     * @return массив байтов
     */
    private byte[] serializeRequest(Request req) {
        ByteArrayOutputStream bA = new ByteArrayOutputStream();
        try(ObjectOutputStream oS = new ObjectOutputStream(bA)) {
            oS.writeObject(req);
        } catch (Exception e) {}

        return bA.toByteArray();
    }

    private String readServerResponse(SocketChannel sock) throws Exception {
        ByteBuffer responseBuffer = ByteBuffer.allocate(1024);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int bytesRead;
        boolean isReading = false;
        int count = 0;
        while ((bytesRead = sock.read(responseBuffer)) != 0 || !isReading || count < 7) {
            if (bytesRead != 0 && !isReading) {
                isReading = true;
            }
            if (bytesRead == 0 && isReading) {
                count++;
            } else {
                System.out.println(count + " " + isReading);
            }
            responseBuffer.flip();
            byte[] bytes = new byte[bytesRead];
            responseBuffer.get(bytes);
            baos.write(bytes);
            responseBuffer.clear();
        }


        byte[] responseBytes = baos.toByteArray();
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(responseBytes))) {
            Response r = (Response) ois.readObject();
            return handleResponse(r);
        }
    }

    private String handleResponse(Response resp) {
        return switch(resp.getType()) {
            case ResponseType.MESSAGE -> ((MessageResp) resp).getMessage();
            default -> "Ошибка чтения ответа";
        };
    }
}
