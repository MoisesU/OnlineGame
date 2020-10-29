import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ApplicationScoped
@ServerEndpoint("/progress")
public class ServerDashboardWebSocket {

    private Set<Session> sessions = new HashSet<>();

    @OnOpen
    public void open(Session session) {
        System.out.println("Session opened ==>");
        sessions.add(session);
        System.out.println(session.getId() + " has joined, there are "+this.sessions.size()+" now");
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        System.out.println("new message ==> " + message);
        try {
            for (int c = 0; c < 100; c++) {
                for (Session s : sessions) {
                    s.getBasicRemote().sendText("{\"value\" : \"" + (c + 1) + "\"}");
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @OnClose
    public void close(Session session) {
        System.out.println("Session closed ==>");
        sessions.remove(session);
    }

    @OnError
    public void onError(Throwable e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }
}