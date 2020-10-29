/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerPackage;

import Tools.Player;
import java.io.EOFException;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author MoisésUlises
 */

@ServerEndpoint(value="/game")
public class GameSocket {
    
    private static AtomicInteger ids = new AtomicInteger(0);
    private Player player;
    private final int id;
    
    public GameSocket(){
        this.id = ids.getAndIncrement();
    }
    
    @OnOpen
    public void init(Session s){
        this.player = new Player(this.id, s);
        GameController.addPlayer(player);
        String plyStates = this.getPlayerStates();
        GameController.broadcast(this.writeMessage("join", "\"players\":" + plyStates));
        //System.out.println(s.getId() + " has joined");        
    }
    
    @OnMessage
    public void onmessage(String message, Session s){
        //Si el mensaje es un comando
        if(message.charAt(0)=='/'){
            this.exCommand(message);
        }
        //Si se trata de un mensaje normal, solo lo manda y ya
        else{
            GameController.broadcast(this.writeMessage("message", "\"text\":\""+message+"\""));
        }
    }
    
    @OnClose
    public void onclose(Session s){
        GameController.removePlayer(player);
        GameController.broadcast(this.writeMessage("levae", "\"reason\":\"un\""));
        //System.out.println(s.getId() + " has left");
    }
    
    @OnError
    public void onError(Throwable t) throws Throwable {
        // Most likely cause is a user closing their browser. Check to see if
        // the root cause is EOF and if it is ignore it.
        // Protect against infinite loops.
        System.out.println("Something went wrong.");
        int count = 0;
        Throwable root = t;
        while (root.getCause() != null && count < 20) {
            root = root.getCause();
            count ++;
        }
        if (root instanceof EOFException) {
            // Assume this is triggered by the user closing their browser and
            // ignore it.
        } else {
            throw t;
        }
    }
    
    private String getPlayerStates(){
        String aux = "";
        for (Iterator<Player> it = GameController.getPlayers().iterator(); it.hasNext();) {
            Player p = it.next();
            aux += "["+p.getId()+", "+p.getX()+", "+p.getY()+"]";
            if(it.hasNext())
                aux += ",";
        }
        return "["+aux+"]";
    }
    
    private String writeMessage(String type, String message){
        return  "{\"type\":\""+type+"\", \"id\":"+this.id+", \"name\":\""+player.getName()+"\", "+message+"}";
    }

    private void exCommand(String command) {
        //Cambiar expresión regular
        String regEx = "\\/((move [3-4][0-9]|name [a-z A-Z _ -]+)|(start|stop))";
        if(command.matches(regEx)){
            String[] cmd = command.substring(1, command.length()).split(" ");
            String msg = "";
            switch(cmd[0]){
                case "move":
                    this.player.move(cmd[1]);
                    break;
                case "name":
                    msg = player.getName()+" has changed its name to "+cmd[1];
                    this.player.setName(cmd[1]);
                    GameController.broadcast(this.writeMessage("message", "\"text\":\""+msg+"\""));
                    break;
                case "color":
                    this.player.setColor(cmd[1]);
                    break;
                case "stop":
                    System.out.println(this.player.getName()+" has stopped the loop");
                    GameController.stopTimer();
                    break;
                case "start":
                    GameController.startTimer();
                    System.out.println(this.player.getName()+" has restarted the loop");
                    break;
            }
        }
    }
    
}
