/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.*;

public class Server extends Thread {
    int port;
    String host;
    String log = "";
    private Selector selector;
    Map<SocketChannel,String> clientNames = new HashMap<>();
    Map<SocketChannel,String> clientLogs = new HashMap<>();

    public void stopServer() {
        interrupt();
    }
    public String getServerLog() {
        return log;
    }

    Server(String host,int port){
        this.host = host;
        this.port = port;
    }
    public void startServer(){
        try {
            ServerSocketChannel channel = ServerSocketChannel.open();
            channel.bind(new InetSocketAddress(host, port));
            channel.configureBlocking(false);
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_ACCEPT);
            start();
        } catch (IOException e){
            System.out.println("Error while starting server");
        }
    }
    @Override
    public void run (){
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        SocketChannel client = serverSocketChannel.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        clientNames.put(client, "");
                        clientLogs.put(client, "");
                    }
                    if (key.isReadable()) {
                        request(key);
                    }
                }
            }
        } catch (IOException e){
            System.out.println("Error while running server");
        }
    }

    private void request (SelectionKey key) throws IOException {

        SocketChannel client = (SocketChannel) key.channel();

        ByteBuffer requestBuffer = ByteBuffer.allocate(1024);
        client.read(requestBuffer);
        String request = new String(requestBuffer.array()).trim();

        String response = "";
        if (!request.isEmpty()) {
            if (request.contains("login")) {
                String str = "logged in";
                response += str;
                clientNames.put(client, request.substring(request.indexOf(' ') + 1));
                log += clientNames.get(client) + " logged in at " + LocalTime.now() + '\n';
                clientLogs.put(client, "=== " + clientNames.get(client)
                        + " log start ===" + '\n' + "logged in" + '\n');

            }else if (request.equals("bye")) {
                String str = "logged out";
                response += str;
                log += (clientNames.get(client) + " logged out at " + LocalTime.now() + "\n");
                clientLogs.put(client,clientLogs.get(client) + str +
                        "\n" + "=== " + clientNames.get(client) + " log end ===" + "\n");
            }
            else if (request.equals("bye and log transfer")) {
                String str = "logged out";
                clientLogs.put(client, clientLogs.get(client) + str + "\n" + "=== "
                        + clientNames.get(client) + " log end ===" + "\n");
                log += (clientNames.get(client)+" logged out at "+ LocalTime.now() + "\n");
                response += (clientLogs.get(client));
            }
            else {
                String result = Time.passed(request.substring(0, request.indexOf(' ')), request.substring(request.indexOf(' ') + 1));
                response += result;
                log += (clientNames.get(client) + " request at " + LocalTime.now()
                        + ": \"" + request + "\"" + "\n");
                clientLogs.put(client, clientLogs.get(client) + "Request: "
                        + request + "\n" + "Result: " + "\n" + result + "\n");
            }

            ByteBuffer responseBuffer = StandardCharsets.UTF_8.encode(response);
            client.write(responseBuffer);
            responseBuffer.clear();

        }
        else {
            System.out.println("empty request");
            client.close();
        }
    }

}