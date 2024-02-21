/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Card;
import model.Player;
import utils.Constants;

/**
 *
 * @author Usuario
 */
public class DaoImpl implements Dao{
    Connection conn;

    @Override
    public void connect() throws SQLException {

        try {
             conn = DriverManager.getConnection(Constants.URL, Constants.USER, Constants.PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() throws SQLException {
    }

    @Override
    public int getLastIdCard(int playerId) throws SQLException {
        try{
            //TODO cambiar sql
            PreparedStatement consult = this.conn.prepareStatement(Constants.SGETLASTIDCARDHAND);
            consult.setInt(1, playerId);
            ResultSet result = consult.executeQuery();
            boolean resultbool = result.next();
            if (resultbool){
                return result.getInt(1);
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return 1000000;
    }

    @Override
    public Card getLastCard(int idPlayer) throws SQLException {
        try{
            PreparedStatement consult = this.conn.prepareStatement(Constants.SFOUNDLASTCARDTABLE);
            consult.setInt(1, idPlayer);
            ResultSet result = consult.executeQuery();
            if(result.next()){
                Card card = new Card(result.getInt("idCard"), result.getString("numberCard"), result.getString("color"), idPlayer);
                return card;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Player getPlayer(String user, String pass) throws SQLException {
        /*Primero hay que saber si existe el usuario. Si no existe devolverá un usuario vacío.
        Después de comprobar si existe, en teoría, si es la primera vez, puede tener la partida
        */
        try{
           PreparedStatement consult = this.conn.prepareStatement(Constants.SLOGGIN);
           consult.setString(1, user);
           consult.setString(2, pass);
           ResultSet respuesta = consult.executeQuery();
           
           if(respuesta.next()){
               return new Player(respuesta.getInt("idPlayer"), respuesta.getString("nameUser"), respuesta.getInt("games"), respuesta.getInt("victories"));
           }
           
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Card> getCards(int playerId) throws SQLException {
        ArrayList<Card> cartas = new ArrayList<>();
       
        try{
            PreparedStatement consult = this.conn.prepareStatement(Constants.SGETCARDSHAND);
            consult.setInt(1, playerId);
            ResultSet respuesta = consult.executeQuery();
            boolean res = respuesta.next();
            int idCard;
            String numberCard;
            String colorCard;
            int idJugador;
            
            if (res){//ESTA CONDICIÓN ES IRRELEVANTE
               while (res){
                   idCard = respuesta.getInt("idCard");
                   numberCard = respuesta.getString("numberCard");
                   colorCard = respuesta.getString("color");
                   idJugador = respuesta.getInt("id_Player");
                   cartas.add(new Card(idCard, numberCard, colorCard, idJugador));
                   res = respuesta.next();
               }
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return cartas;
    }

    @Override
    public Card getCard(int cardId) throws SQLException {
        return null;
    }

    @Override
    public void saveGame(Card card) throws SQLException {
        try{
            PreparedStatement consult = this.conn.prepareStatement(Constants.SSAVEGAME);
            consult.setInt(1, card.getId());
            int setting = consult.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void saveCard(Card card) throws SQLException {
        try {
            PreparedStatement consult = this.conn.prepareStatement(Constants.SSAVECARD);
            consult.setInt(1, card.getPlayerId());
            consult.setString(2, card.getNumber());
            consult.setString(3, card.getColor());
            int setting = consult.executeUpdate();  
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCard(Card card) throws SQLException {
        try{
            PreparedStatement consult = this.conn.prepareStatement(Constants.SDELETECARD);
            consult.setInt(1, card.getId());
            int setting = consult.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void clearDeck(int playerId) throws SQLException {
        //elimino primero la mesa y luego el jugador
        try{
            PreparedStatement consult = this.conn.prepareStatement(Constants.SDELETECARDSTABLE);
            consult.setInt(1, playerId);
            int result = consult.executeUpdate();
            try{
                PreparedStatement consultDos = this.conn.prepareStatement(Constants.SDELETECARTS);
                consultDos.setInt(1, playerId);
                int restult = consultDos.executeUpdate();
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    @Override
    public void addVictories(int playerId) throws SQLException {
        int numberWins = 0;
        //Buscamos el jugador y guardamos el numero de victorias en la variable numberWins
        try {
            PreparedStatement consult = this.conn.prepareStatement(Constants.SRETRIEVEPLAYER);
            consult.setInt(1, playerId);
            ResultSet result = consult.executeQuery();
            if(result.next()){
                numberWins = result.getInt("victories");
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //Actualizamos la información de victories del jugador
        try {
            PreparedStatement consult = this.conn.prepareStatement(Constants.SADDVICTORIE);
            consult.setInt(1, numberWins + 1);
            consult.setInt(2, playerId);
            int result = consult.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @Override
    public void addGames(int playerId) throws SQLException {
        int numGames = 0;
        try {
            PreparedStatement consult = this.conn.prepareStatement(Constants.SRETRIEVEPLAYER);
            consult.setInt(1, playerId);
            ResultSet result = consult.executeQuery();
            if(result.next()){
                numGames = result.getInt("games");
                try {
                    PreparedStatement consultDos = this.conn.prepareStatement(Constants.SADDGAME);
                    consultDos.setInt(1, numGames + 1);
                    consultDos.setInt(2, playerId);
                    int resultDos = consultDos.executeUpdate();
                 }catch(Exception e){
                     e.printStackTrace();
                 }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Retorna los valores de todos los jugadores. Todos los jugadores van dentro de un arraylist,
     * pero los valores van en un array de strings, en cada una de las posiciones del arraylist.
     * @return 
     */
    public ArrayList<String[]> returnValuesPropertiesPlayer(){
        ArrayList<String[]> a = new ArrayList<>();
        try{
            PreparedStatement consult = this.conn.prepareStatement(Constants.SFINDALLPLAYERS);
            ResultSet result = consult.executeQuery();
            while(result.next()){
                String[] s = {String.valueOf(result.getInt(1))  ,
                            result.getString(2),result.getString(3), 
                            result.getString(4), result.getString(5),
                            result.getString(6)};
                a.add(s);
            }
            return a;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public ArrayList<Player> getListPlayers(){
        ArrayList<Player> players = new ArrayList<>();
        try{
            PreparedStatement consult = this.conn.prepareStatement(Constants.SFINDALLPLAYERS);
            ResultSet result = consult.executeQuery();
            while(result.next()){
                players.add(new Player(result.getInt("idPlayer"), result.getString("nameUser"), result.getString("passwordUser"),
                            result.getString("namePlayer"), result.getInt("games"), result.getInt("victories")));
            }
            return players;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
}
