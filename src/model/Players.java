
package model;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "Players")
public class Players {
    ArrayList<Player> players = new ArrayList<>();
    
    @XmlElementWrapper(name = "Players")
    @XmlElement(name = "Player")
    public ArrayList<Player> getPlayers(){
        return this.players;
    }
    
    public void setPlayers(ArrayList<Player> players){
        this.players = players;
    }
    
    @Override
    public String toString(){
        return "Players{Players:"  + this.players;
    } 
}
