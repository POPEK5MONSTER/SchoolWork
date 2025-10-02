/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad1;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatClientTask implements Runnable {
    private ChatClient client;
    private List<String> msgs;
    private int wait;
    private volatile boolean ready = false;

    private ChatClientTask(ChatClient client, List<String> msgs, int wait) {
        this.client = client;
        this.msgs = msgs;
        this.wait = wait;
    }


    public static ChatClientTask create(ChatClient c, List<String> msgs, int wait) {
        return new ChatClientTask(c, msgs, wait);
    }

    public void get() throws InterruptedException, ExecutionException {
        while (!ready) {
            Thread.sleep(500);
        }
    }

    public ChatClient getClient() {
        return client;
    }

    @Override
    public void run() {
        if (Thread.interrupted()) return;
        client.login();
        for (String msg : msgs) {
            if (Thread.interrupted()) return;
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                System.out.println("error");
            }
            if (Thread.interrupted()) return;
            client.send(msg);
        }
        if (Thread.interrupted()) return;
        client.logout();
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            System.out.println("error2");
        }
        ready = true;
    }

}