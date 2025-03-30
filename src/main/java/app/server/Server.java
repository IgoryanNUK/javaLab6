package app.server;


import app.messages.requests.Request;
import app.messages.response.Response;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port = 4027;
    private ServerSocket server;
    private ConnetionGetter connection;
    private CollectionManager collection;
    private RequestHandler handler;
    private String envVar = "LAB5_PATH";

    public Server() {
        try {
            server = new ServerSocket(port);
            connection = new ConnetionGetter(server);
            collection = new CollectionManager(envVar);
            handler = new RequestHandler(collection);
        } catch (Exception e) {
        }
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    private void run() {
        while (true) {
            try {
                Socket sock = connection.getConnection();
                if (sock.isBound()) {
                    Request req = Communicator.read(sock);
                    Response response = handler.handleRequest(req);
                    Communicator.send(response, sock);
                }
            } catch (Exception e) {
                System.out.println(server.isClosed());
                e.printStackTrace();
            }
        }
    }
}
