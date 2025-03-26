package app.server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import app.messages.commands.Command;
import app.messages.commands.Request;

public class Communicator {
    /**
     * Возвращает полученный от клиента массив байтов.
     *
     * @param sock сокет соединения с клиентом
     * @return прочитанный объект
     * @throws Exception непредвиденные ошибки
     */
    public static Request read(Socket sock) throws Exception {
        InputStream is = sock.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        return (Request) ois.readObject();
    }

    public static void send(Command c, Socket sock) throws Exception {
        OutputStream os = sock.getOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(os);
        ous.reset();
        ous.writeObject(c);
        ous.flush();
    }

    public static void sendString(String message, Socket sock) throws Exception {
        OutputStream os = sock.getOutputStream();
        os.write(message.getBytes(StandardCharsets.UTF_8));
        os.flush();
    }
}
