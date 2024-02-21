package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Player")
@XmlType(propOrder = {"id", "username", "password", "name", "games", "victories"})
public class Player {

	private int id;
        private String username;
        private String password;
	private String name;
	private int games;
	private int victories;
        
        public Player(){
            
        }
	
	public Player(String nombre) {
		this.name = nombre;
		this.games = 0;
		this.victories = 0;
	}

	public Player(int id, String name, int games, int victories) {
		this.id = id;
		this.name = name;
		this.games = games;
		this.victories = victories;
	}
        
        public Player(int id, String username, String password, String name, int games, int victories){
            this.id = id;
            this.username = username;
            this.password = password;
            this.name = name;
            this.games = games;
            this.victories = victories;
        }
        @XmlAttribute(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
        @XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
        @XmlElement(name = "games")
	public int getGames() {
		return games;
	}

	public void setGames(int games) {
		this.games = games;
	}
        @XmlElement(name = "victories")
	public int getVictories() {
		return victories;
	}

	public void setVictories(int victories) {
		this.victories = victories;
	}
        
        @XmlElement(name = "username")
        public String getUsername(){
            return this.username;
        }
        
        @XmlElement(name = "password")
        public String getPassword(){
            return this.password;
        }

	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", games=" + games + ", victories=" + victories + "]";
	}

	
	
	
}
