/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad1;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.channels.SocketChannel;
import java.nio.CharBuffer;
import java.net.InetSocketAddress;

public class Client{

    public int port;
    public String id;
    public String host;
    SocketChannel channel;

    public Client(String host, int port, String id){
        this.id=id;
        this.port=port;
        this.host=host;
    }
    public String getId() {
        return id;
    }
    public void connect(){
        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(host, port));
            channel.finishConnect();

        } catch (IOException e){
            System.out.println("Cant connect to the server");
        }
    }

    public String send(String requestData){
        ByteBuffer responseBuffer = ByteBuffer.allocateDirect(1024);
        StringBuilder responseBuilder = new StringBuilder();
        try {
            ByteBuffer buffer = ByteBuffer.allocateDirect(requestData.getBytes().length);
            buffer.put(StandardCharsets.UTF_8.encode(requestData));
            buffer.flip();
            channel.write(buffer);

            int bytesRead;
            do {
                bytesRead = channel.read(responseBuffer);
            } while (bytesRead <= 0);
            while(bytesRead > 0){
                bytesRead = channel.read(responseBuffer);
                responseBuffer.flip();
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(responseBuffer);
                responseBuilder.append(charBuffer);
            }
        } catch (IOException e){
            System.out.println("Cant send a request");
        }

        return responseBuilder.toString();
    }
}
