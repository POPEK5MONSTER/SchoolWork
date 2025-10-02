/**
 *
 *  @author Popowski Mateusz S27485
 *
 */

package zad1;


import java.util.List;
import java.util.concurrent.FutureTask;

public class ClientTask extends FutureTask<String> {
    static String stringBuffer = "";

    private ClientTask(Client client, List<String> request, boolean showSendRes) {
        super(()->{
            client.connect();
            client.send("login " + client.getId());

            for (String req : request) {
                String response = client.send(req);
                if (showSendRes)
                    System.out.println(response);
            }
            stringBuffer += (client.send("bye and log transfer"));
            return stringBuffer;
        });
    }

    public static ClientTask create(Client client, List<String> request, boolean showSendRes){
        return new ClientTask(client, request, showSendRes);
    }
}
