/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChatClient extends Thread {
    private SocketChannel socketChannel;
    private String person;
    private int port;
    private String host;
    private StringBuilder result;
    private Lock lock = new ReentrantLock();
    private volatile boolean running = true;

    public ChatClient(String host, int port, String person) {
        this.person = person;
        this.host = host;
        this.port = port;
        this.result = new StringBuilder("=== " + person + " chat view\n");
    }

    public String getChatView() {
        return result.toString();
    }

    public void login() {
        try {
            this.socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            socketChannel.configureBlocking(false);
            int n = 20;

            while (!socketChannel.finishConnect()) {
                System.out.println(n);
                Thread.sleep(500);
                n--;
                if (n <= 0) {
                    throw new Exception("Failed connection to server.");
                }
            }

            this.request("login " + person);
            this.start();

        } catch (Exception e) {
e.printStackTrace();        }
    }

    public void logout() {
        try {
            this.request("end");
            Thread.sleep(500);

            lock.lock();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void send(String request) {
        this.request(request);
    }

    @Override
    public void run() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        int i;

        while (running) {  // Use the running flag to control the loop
            try {
                lock.lock();
                i = socketChannel.read(byteBuffer);
                if (i > 0) {
                    byteBuffer.flip();
                    Charset charset = StandardCharsets.UTF_8;
                    CharBuffer charBuffer = charset.decode(byteBuffer);
                    StringBuilder sb = new StringBuilder();

                    while (charBuffer.hasRemaining()) {
                        char x = charBuffer.get();
                        sb.append(x);
                    }

                    String res = sb.toString().trim();
                    if (res.trim().startsWith(person + ";")) {
                        this.result.append(res.replace(";", "")).append("\n");
                    } else {
                        this.result.append(res).append("\n");
                    }
                }
            } catch (IOException e) {
                System.out.println(" ");
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            byteBuffer.clear();
        }

        try {
            if (socketChannel != null && socketChannel.isOpen()) {
                socketChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void request(String message) {
        try {
            this.socketChannel.write(StandardCharsets.UTF_8.encode(message + '!'));
        } catch (Exception e) {
e.printStackTrace();        }
    }
}
