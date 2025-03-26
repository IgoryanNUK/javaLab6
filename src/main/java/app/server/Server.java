package app.server;


import app.messages.commands.Command;
import app.messages.commands.Request;
import app.messages.commands.Response;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port = 4027;
    private ServerSocket server;
    private ConnetionGetter connection;

    public Server() {
        try {
            server = new ServerSocket(port);
            connection = new ConnetionGetter(server);
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
                    Response response = req.getResponse();
                    Communicator.send(response, sock);
                }
            } catch (Exception e) {
                System.out.println(server.isClosed());
                e.printStackTrace();
            }
        }
    }
}
