/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Usuario
 */
public class Constants {
    //Datos de acceso a la base de datos
    public static final String URL = "jdbc:mysql://localhost:3306/GameUno" + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static final String USER = "root";
    public static final String PASSWORD = "J.barreda98";
    
    //Sentencias SQL loggin
    public static final String SLOGGIN = "SELECT * FROM unoPlayer WHERE nameUser = ? and passwordUser = ?";
    
    //Sentencia SQL para buscar todas las cartas de un jugador
    public static final String SGETCARDS = "SELECT * FROM card WHERE id_Player = ?";
    
    //Sentencia SQL buscar las cartas de la mano de un jugador
    public static final String SGETCARDSHAND = "select card.idCard, card.id_Player, card.color, card.numberCard from card left join unoGame\n" +
                                                    "on card.idCard = unoGame.idCart\n" +
                                                    "where id_Player = ? and unoGame.idCart is null";
    
    //Sentencia SQL para devolver el índice de la siguiente carta en la tabla cartas de un jugador
    public static final String SGETLASTIDCARDHAND = "select ifnull(max(idCard), 0) + 1 from card\n" +
                                                    "where id_Player = ?";
    
    /**
     * Sentencia SQL para devolver la última carta que hay en la mesa de un jugador
     */
    public static final String SFOUNDLASTCARDTABLE = "select idCard, numberCard, color from unoGame left join card\n" +
                                                    "on card.idCard = unoGame.idCart\n" +
                                                    "where id_Player = ?\n" +
                                                    "order by idCart desc\n" +
                                                    "limit 1;";
                                                    
    /**
     * Sentencia SQL para insertar una carta al registro de cartas.
    */
    public static final String SSAVECARD = "INSERT INTO card(id_Player, numberCard, color) VALUES(?, ?, ?)";
    
    /**
     * Sentencia SQL para eliminar una carta
     */
    public static final String SDELETECARD = "DELETE FROM card WHERE idCard = ?";
    
    /**
     * Sentencia SQL para guardar una carta en el tablero mesa
     */
    public static final String SSAVEGAME = "INSERT INTO unoGame(idCart) VALUES (?)";
    
    /**
     * Sentencia SQL para guardar una nueva victoria de un jugador
     */
    public static final String SADDVICTORIE = "UPDATE unoPlayer SET victories = ? WHERE idPlayer = ?";
    
    /**
     * Sentencia SQL para encontrar un Jugador por ID.
     */
    public static final String SRETRIEVEPLAYER = "SELECT * FROM unoPlayer WHERE idPlayer = ?";
    
    /**
     * Sentencia SQL para guardar una nueva partida
     */
    public static final String SADDGAME = "UPDATE unoplayer SET games = ? WHERE idPlayer = ?";
    
    /**
     * Sentencia SQL para eleminar las cartas de la mano del jugador
     */
    public static final String SDELETECARTS = "DELETE FROM card WHERE id_Player = ?";
    
   /**
    * Sentencia SQL para eliminar las cartas de la mesa del jugador
    */ 
    public static final String SDELETECARDSTABLE = "delete unoGame from unoGame left join card on \n" +
                                                    "unoGame.idCart = card.idCard\n" +
                                                    "where id_Player = ?";
    
    /**
     * Sentencia SQL para recoger todos los datos de todos los players
     */
    public static final String SFINDALLPLAYERS = "SELECT * FROM unoPlayer";
    
}
