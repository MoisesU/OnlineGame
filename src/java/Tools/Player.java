/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;

/**
 *
 * @author MoisésUlises
 */
public class Player extends GameEntity{
    private final int id;
    private final Session session;
    private String color = "#000000";
    private String name;

    public Player(int id, Session s){
        super(Integer.toString(id), 0, 0, 50, 50);
        this.setStep(8);
        this.id = id;
        this.name = s.getId();
        this.session = s;
    }    
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
    public void move(String dir){
        this.move(Integer.parseInt(dir));
    }

    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException ex) {
            System.err.println("Error at sending a Message from Player "+this.name);
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getPosition(){
        return String.format("[%d, %d, %d]", this.id, this.getX(), this.getY());
    }
       
}
