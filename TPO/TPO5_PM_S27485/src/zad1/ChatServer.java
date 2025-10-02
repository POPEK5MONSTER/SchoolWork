/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad1;


import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.nio.*;
import java.nio.charset.*;
import java.nio.channels.*;
import java.time.*;


public class ChatServer implements Runnable {
    private ServerSocketChannel serverSocketChannel = null;
    private Selector selector = null;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    Map<SocketChannel, String> map = new LinkedHashMap<>();
    StringBuilder log = new StringBuilder();
    Charset charset = StandardCharsets.UTF_8;
    private Lock lock = new ReentrantLock();

    public ChatServer(String host, int port) {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(host, port));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void startServer() {
        executorService.submit(this);
        System.out.println("Server started\n");
    }

    public void stopServer() {
        System.out.println("Server stopped");
        try {
            Thread.sleep(500);
            if (!map.isEmpty()) {
                for (SocketChannel socketChannel : map.keySet()) {
                    if (socketChannel.isOpen()) {
                        socketChannel.close();
                        socketChannel.socket().close();
                    }
                }
            }
            lock.lock();
            try {
                executorService.shutdownNow();
            } finally {
                lock.unlock();
            }
            if (selector != null) selector.close();
            if (serverSocketChannel != null) {
                serverSocketChannel.close();
                serverSocketChannel.socket().close();
            }
        } catch (IOException e) {
            System.out.println("Error6");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public String getServerLog() {
        return log.toString();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                selector.select();

                if (Thread.interrupted()) {
                    break;
                }

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isAcceptable()) {
                        SocketChannel clientChannel = this.serverSocketChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        continue;
                    }

                    if (key.isReadable()) {
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        this.serviceRequest(clientChannel);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error7");
            }
        }
    }

    private void serviceRequest(SocketChannel sc) throws IOException {
        if (!sc.isOpen()){
            return;
        }
        if (sc.socket().isClosed()){
            return;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int n = sc.read(byteBuffer);
        if (n <= 0){
            return;
        }
        byteBuffer.flip();
        CharBuffer charBuffer = charset.decode(byteBuffer);
        String s = charBuffer.toString();
        String[] split1 = s.split("!");
        for (String req : split1) {
            String resp;
            if (req.startsWith("login")) {
                String[] split = req.split("\\s+");
                map.put(sc, split[1]);
                String info = split[1] + " logged in\n";
                write(info);
            }
            else if (req.startsWith("end")) {
                resp = map.get(sc) + " logged out\n";
                write(resp);
                if (sc.isOpen()) {
                    sc.close();
                    sc.socket().close();
                }
                map.remove(sc);
            }
            else {
                String result = map.get(sc) +
                        ": " +
                        req +
                        "\n";
                write(result);
            }
        }
    }

    private void write(String res) throws IOException {

        log.append(LocalTime.now()).append(" ").append(res);
        ByteBuffer buf = charset.encode(CharBuffer.wrap(res));

        for (SocketChannel c : map.keySet()) {

            if (c.isOpen() && !c.socket().isClosed()) {
                c.write(buf);
                buf.rewind();
            }

        }
    }
}