/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Usuario
 */
public class DOMImpl {
    private Document document;
    private Element elemento;
    public DOMImpl() throws ParserConfigurationException{
        DocumentBuilderFactory dBFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dBFactory.newDocumentBuilder();
        document = dBuilder.newDocument();
        elemento = document.createElement("Players");
        document.appendChild(elemento);
    }
    
    public void generateXML(int idJugador, String[] listValueProperties){
        //Creamos la etiqueta Players
        Element jugador = document.createElement("Player");
        //Ahora vamos a crear un jugador
        jugador.setAttribute("id", String.valueOf(idJugador));
        elemento.appendChild(jugador);
        
        //Creamos una lista element
        Element[] propertiesPlayer = new Element[5];
        //Creamos una lista con los nombres de etiqueta para asignarlos en la creación de los element
        String[] strPropertiesPlayer = {"username", "password", "name", "games", "victories"};
        //recorremos el array para crear cada element. Ponemos el contenido también en la etiqueta que pertoca
        for (int i = 0; i < strPropertiesPlayer.length; i++){
            propertiesPlayer[i] = document.createElement(strPropertiesPlayer[i]);
            propertiesPlayer[i].setTextContent(listValueProperties[i]);
            jugador.appendChild(propertiesPlayer[i]);
        }
        
        
    }
}
