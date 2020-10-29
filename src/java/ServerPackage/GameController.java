/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerPackage;

import Tools.Ball;
import Tools.Player;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;



/**
 *
 * @author MoisésUlises
 */
public class GameController {

    private static Timer gameTimer = null;
    private static Ball ball = null;
    private static final long TICK_DELAY = 100;
    private static final ConcurrentHashMap<Integer, Player> players = new ConcurrentHashMap<>();
    
    
    
    public static synchronized void addPlayer(Player p){
        if (players.isEmpty()) {
            startTimer();
        }
        players.put(Integer.valueOf(p.getId()), p);
    }
    
    public static synchronized void removePlayer(Player p){
        players.remove(Integer.valueOf(p.getId()));
        if (players.isEmpty()) {
            stopTimer();
        }
    }
    
    public static synchronized void broadcast(String message){
        for(Player p : getPlayers()){
            p.sendMessage(message);
        }
    }
    
    public static synchronized void tick(){
        String aux = "";
        for (Iterator<Player> iterator = getPlayers().iterator(); iterator.hasNext();) {
            Player player = iterator.next();
            aux += player.getPosition();
            if (iterator.hasNext()) {
                aux += ",";
            }
        }
        ball.update(getPlayers());
        //.out.println(String.format("{\"type\": \"update\", \"data\" : [%s], \"ball\" : %s}", aux, ball.getPosition()));
        broadcast(String.format("{\"type\": \"update\", \"data\" : [%s], \"ball\" : %s}", aux, ball.getPosition()));
    }
    
    protected static Collection<Player> getPlayers() {
        return Collections.unmodifiableCollection(players.values());
    }
    
    public static void startTimer(){
        ball = new Ball();
        gameTimer = new Timer(GameController.class.getSimpleName() + " Timer");
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try{
                    tick();
                }catch(Exception e){
                    System.err.println("Error en la TimerTask: "+e.getMessage());
                    System.err.println("Cause: " + e.getCause());
                }
            }
        }, TICK_DELAY, TICK_DELAY);
    }
    
    public static void stopTimer(){
        if (gameTimer != null) {
            gameTimer.cancel();
        }
    }
   
}
