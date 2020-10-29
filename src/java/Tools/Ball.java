/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.util.Collection;

/**
 *
 * @author MoisésUlises
 */
public class Ball extends GameEntity{
    private int dx;
    private int dy;
    
    
    public Ball() {
        super("bola", 472, 30, 16, 16);
        this.setStep(8);
        this.dx = this.getStep();
        this.dy = -this.getStep();
    }
    
    public void update(Collection<Player> players){
        if(this.getX() + this.dx > GAME_WIDTH-this.getWidth() || this.getX() + this.dx < 0) {
            this.changeXDirection();
        }
        if(this.getY() + this.dy > GAME_HEIGHT-this.getHeight() || this.getY() + this.dy < 0) {
            this.changeYDirection();
        }
        players.forEach((p) -> {
            detectColission(p);
        });
        
        this.moveX();
        this.moveY();
    }
    
     private void detectColission(Player p) { 
        //La pelota colisiona al jugador p a la izquierda
        //Saber si está a la misma altura (Py >= By y By+H <= Py+H)
        //Saber si lo está colisionando en x de izq a der (Bx+W + dx > Px)
        /*if(p.getY() >= this.getY() && this.getY()+this.getWidth() <= p.getY()+p.getHeight())
            if(this.dx>0)
                if(this.getX()+this.getHeight()+this.dx >= p.getX() && this.getX()+this.getHeight()+this.dx < p.getX()+this.dx){
                    this.changeXDirection();
                    System.out.println(p.getName()+" collision case: 4");
                }
            else 
                if(this.getX()+this.dx >= p.getX()+p.getHeight() && this.getX()+this.dx < p.getX()+p.getHeight()+dx){
                    this.changeXDirection();
                    System.out.println(p.getName()+" collision case: 2");
                }
                    
                
                
        //La pelota colisiona al jugador p por abajo
        //Saber si está a la misma altura (Px >= Bx y Bx+W <= Px+W)
        //Saber si lo está colisionando en x de izq a der (By + dy > Py+H)
        else if(p.getX() >= this.getX() && this.getX()+this.getHeight() <= p.getX()+p.getWidth())
            if(this.dy>0)
                if(this.getY()+this.getWidth()+this.dy >= p.getY() && this.getY()+this.getWidth()+this.dy < p.getY()+this.dy){
                    this.changeYDirection();
                    System.out.println(p.getName()+" collision case: 1");
                }
            else
                if(this.getY()+this.dy >= p.getY()+p.getWidth() && this.getY()+this.dy < p.getY()+p.getHeight()+dy){
                    this.changeYDirection();
                    System.out.println(p.getName()+" collision case: 3");
                }   
        */
        
        /*if(p.getY() >= this.getY()+dy && this.getY()+this.getWidth()+dy <= p.getY()+p.getHeight() &&
            p.getX() >= this.getX()+dx && this.getX()+this.getHeight()+dx <= p.getX()+p.getWidth()){
            this.changeXDirection();
            this.changeYDirection();
        }*/
        
        if((this.getY() + this.getHeight() > p.getY() && this.getY() + this.getHeight() < p.getY() + p.getHeight()) && (this.getX() + this.getWidth() > p.getX() && this.getX() + this.getWidth() < p.getX() + p.getWidth())){
            this.changeXDirection();
            this.changeYDirection();
        }
        if((this.getY() > p.getY() && this.getY() < p.getY() + p.getHeight()) && (this.getX() > p.getX() && this.getX() < p.getX() + p.getWidth())){
            this.changeXDirection();
            this.changeYDirection();
        }
            
                
    }
    
    public void changeXDirection(){
        this.dx = -this.dx;
    }
    
    public void changeYDirection(){
        this.dy = -this.dy;
    }

    private void moveX(){
        this.setX(this.getX() + dx);
    }

    private void moveY(){
        this.setY(this.getY() + dy);
    }
    
    public String getPosition(){
        return String.format("[%d, %d]", this.getX(), this.getY());
    }

}
