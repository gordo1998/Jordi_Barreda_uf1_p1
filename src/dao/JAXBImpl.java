/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import model.Player;
import model.Players;

/**
 *
 * @author Usuario
 */
public class JAXBImpl {
    JAXBContext context;
    Marshaller marshall;
    Players players;

    public JAXBImpl() throws JAXBException, SQLException {
        this.context = JAXBContext.newInstance(Players.class);
        players = new Players();
    }
    
    public void marshaller() throws JAXBException{
        marshall = this.context.createMarshaller();
    }
    
    public void createXML(ArrayList<Player> playerslist) throws SQLException, PropertyException, IOException, JAXBException{
        players.setPlayers(playerslist);
        marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshall.marshal(players, new FileWriter("playersJAXB.xml"));
    }
}
