/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author christian
 */
public interface Empaquetable {
    public void empaquetarXML(Node nodeXML, Document doc);
}
