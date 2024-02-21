package controller;

import dao.DOMImpl;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import dao.DaoImpl;
import dao.JAXBImpl;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import model.Card;
import model.Player;
import utils.Color;
import utils.Constants;
import utils.Number;

/**
 * 
 */
/**
 * 
 */
public class Controller {
	private static Controller controller;//Esto es el controlador
	private DaoImpl dao;
	private Player player;
	private ArrayList<Card> cards;
	private Scanner s;
	private Card lastCard;
        private DOMImpl domImpl;

	private Controller () {
		dao = new DaoImpl();
		s = new Scanner(System.in);
	}
	
	public static Controller getInstance() {
		if (controller == null) {
			controller = new Controller();
		}
		return controller;
	}

	
	/**
	 * Start game,
	 * connect to db
	 * check user/pw
	 * play a card
	 */
	public void init() throws ParserConfigurationException, IOException, TransformerException, JAXBException {
		try {
			// connect to data
			dao.connect();
			
			// if login ok 
			if (loginUser()) {
				// check last game
				startGame();
				// play turn based on cards in hand
				playTurn();
				
				
			} else
				System.out.println("User or password incorrect.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				// disconnect data
				dao.disconnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Show cards in hand
	 * Ask for player action
	 * @throws SQLException
	 */
	private void playTurn() throws SQLException, ParserConfigurationException, IOException, TransformerException, JAXBException {
		Card card = null;
		boolean correctCard= false;
		boolean end=false;
		
		// loop until option end game option or no more cards
		while (!end) {
			// loop until selected card matches rules based on last card on table
			do {				
				showCards();				
				System.out.println("Press -1 to take a new one.");
				System.out.println("Press -2 to exit game.");
                                System.out.println("Press -3 for RECU");
				int position=0;
				do {
					System.out.println("Select card to play.");
					position = s.nextInt();
					if (position>=cards.size()) {
						System.out.println("Card does not exist in your hand, try again.");
					}
				} while (position>=cards.size());
								
				switch (position) {
                                    //AQUÍ EMPIEZA MI RECUPERACIÓN
                                case -3:
                                    recuperationMenu();
                                    String opcionMenu;
                                    opcionMenu = choseSecondMenuOption();
                                    
                                    while(!opcionMenu.equals("-D") && !opcionMenu.equals("-J") && !opcionMenu.equals("-F")){
                                        if(opcionMenu.equals("-D") || opcionMenu.equals("-J") || opcionMenu.equals("-F")){
                                            break;
                                        }else{
                                            System.out.println("Opción incorrecta!");
                                        }
                                        opcionMenu = choseSecondMenuOption();
                                    }
                                    //Aqui pondremos lo que va a continua
                                    boolean valueFunction = metodoRecu(opcionMenu);
                                    if(valueFunction){
                                        System.out.println("Archivo generado correctamente");
                                    }else{
                                        System.out.println("No se ha podido generar el archivo correctamente");
                                    }
                                    
                                    break;
				case -2:
					correctCard = true;
					end = true;
					System.out.println("Exiting game by pressing EXIT OPTION");
					break;

				case -1:
					drawCards(1);
					break;

				default:
					card = selectCard(position);
					correctCard = validateCard(card);

					// if skip or change side, remove it and finish game
					if (correctCard) {
						if (card.getNumber().equalsIgnoreCase(Number.SKIP.toString())
								|| card.getNumber().equalsIgnoreCase(Number.CHANGESIDE.toString())) {
							// remove from hand
							this.cards.remove(card);
							dao.deleteCard(card);
							// to end game
							end = true;
							System.out.println("Exiting game by EXIT CARD");
							break;
						}
					}

					// if correct card and no exit card
					if (correctCard && !end) {
						System.out.println("Well done, next turn");
						lastCard = card;
						// save card in game data
						dao.saveGame(card);
						// remove from hand
						this.cards.remove(card);

					} else {
						System.out.println("This card does not match the rules, try other card or draw the deck");
					}
					break;
				}
			} while (!correctCard);
			
			// if no more cards, ends game
			if (this.cards.size() == 0) {
				endGame();
				end=true;
				System.out.println("Exiting game, no more cards, you win.");
				break;
			}			
		}		
	}

	/**
	 * @param card to be played
	 * @return true if it is right based on last card
	 */
	private boolean validateCard(Card card) {
		if (lastCard != null) {
			// same color than previous one
			if (lastCard.getColor().equalsIgnoreCase(card.getColor())) return true;
			// same number than previous one
			if (lastCard.getNumber().equalsIgnoreCase(card.getNumber())) return true;
			// last card is black, it does not matter color
			if (lastCard.getColor().equalsIgnoreCase(Color.BLACK.name())) return true;
			// current card is black, it does not matter color
			if (card.getColor().equalsIgnoreCase(Color.BLACK.name())) return true;
			
			return false;
		} else {
			return true;
		}
	}

	/**
	 * add a new win game
	 * add a new played game
	 * @throws SQLException
	 */
	private void endGame() throws SQLException {
		dao.addVictories(player.getId());
		dao.addGames(player.getId());
		dao.clearDeck(player.getId());
	}

	private Card selectCard(int id) {
		Card card = this.cards.get(id);
		return card;
	}

	private void showCards() {
		System.out.println("================================================");
		if (null == lastCard) {
			System.out.println("First time playing, no cards on table");
		} else {
			System.out.println("Card on table is " + lastCard.toString());
		}
		System.out.println("================================================");
		System.out.println("Your " + cards.size() + " cards in your hand are ...");
		for (int i = 0; i < cards.size(); i++) {
			System.out.println(i + "." + cards.get(i).toString());
		}
	}

	/**
	 * @return true if user/pw found
	 * @throws SQLException
	 */
	private boolean loginUser() throws SQLException {
		System.out.println("Welcome to UNO game!!");
		System.out.println("Name of the user: ");
		String user = s.next();
		System.out.println("Password: ");
		String pass = s.next();
		
		player = dao.getPlayer(user, pass);
		
		if (player != null) {
			return true;
		}
		return false;		
	}
	
	
	/**	 
	 * @throws SQLException
	 */
	private void startGame() throws SQLException {
		// get last cards of player
		cards = dao.getCards(player.getId());
		
		// if no cards, first game, take 3 cards
		if (cards.size() == 0) drawCards(3);
		
		// get last played card --> Esta carta es del jugador que está jugando?
		lastCard = dao.getLastCard(player.getId());
		
		// for last card +2, take two more
		if (lastCard != null && lastCard.getNumber().equalsIgnoreCase(Number.TWOMORE.toString())) drawCards(2);
		// for last card +4, take four more
		if (lastCard != null && lastCard.getNumber().equalsIgnoreCase(Number.FOURMORE.toString())) drawCards(4);
	}
	
	
	/**
	 * get a number of cards from deck adding them to hand of player
	 * @param numberCards
	 * @throws SQLException
	 */
	private void drawCards(int numberCards) throws SQLException {
		
		for (int i = 0; i < numberCards; i++) {
			int id = dao.getLastIdCard(player.getId());
			
			// handle depends on number color must be black or random
			String number = Number.getRandomCard();
			String color="";
			if (number.equalsIgnoreCase(Number.WILD.toString())|| number.equalsIgnoreCase(Number.FOURMORE.toString())){
				color = Color.BLACK.toString();
			}else {
				color = Color.getRandomColor();
			}
					
			Card c = new Card(id, number , color , player.getId());
			dao.saveCard(c);
			cards.add(c);
		}
	}
        
        public void recuperationMenu(){
            System.out.println("Press -F Option File --> Save player in txt with FileWriter\nPres -D option DOM --> Save player in XML with DOM\n"
                            + "Press -J option JAXB --> Save player in XML with JAXB");
        }
        
        public String choseSecondMenuOption(){
            Scanner scan = new Scanner(System.in);
            System.out.println("Introduzca la opción");
            String pos = scan.nextLine();
            return pos;
        }
        
        public boolean metodoRecu(String opcion) throws ParserConfigurationException, IOException, TransformerException, JAXBException, SQLException{
            boolean valueFunction = false;
            switch (opcion){
                case "-F":
                    valueFunction = File();
                    //Crear una
                    break;
                case "-D":
                    valueFunction = DOMmethod();
                    break;
                case "-J":
                    valueFunction = JAXBmethod();
                    break;
            }
            
            return valueFunction;
        }
        
        public boolean DOMmethod() throws ParserConfigurationException, IOException, TransformerException{
            domImpl = new DOMImpl();
            ArrayList<String[]> array = dao.returnValuesPropertiesPlayer();
            for(String[] s: array){
                domImpl.generateXML(s);
            }
            domImpl.writeXML();
            return true;
        }
        
        public boolean JAXBmethod() throws JAXBException, SQLException, PropertyException, IOException{
            ArrayList<Player> players = dao.getListPlayers();
            JAXBImpl jaxb = new JAXBImpl();
            jaxb.marshaller();
            jaxb.createXML(players);
            return true;
        }
        
        public boolean File() throws IOException{
            File file = new File("PlayersFile.txt");
            FileWriter fw;
            ArrayList<Player> jugadores = dao.getListPlayers();
            if (file.createNewFile()){
                System.out.println("Archivo creado: " + file.getName());
            }else{
                System.out.println("El archivo ya existe");
            }
            
            try{
                fw = new FileWriter(file);
                fw.write("idPlayer\tnameUser\tpasswordUser\tnamePlayer\tgames\tvictories\n");
                for(Player i: jugadores){
                    fw.write(i.getId() + "\t\t" + i.getUsername() + "\t\t" + i.getPassword() 
                            + "\t\t" + i.getName() + "\t\t" + i.getGames() + "\t" + i.getVictories() + "\n");
                }
                fw.close();
                return true;
            }catch(Exception e){
                e.printStackTrace();
            }
            return false;
        }


}
