/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xml;

import java.io.StringWriter;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author christian
 */
public class XmlSerializador {
    private String raiz;
    
    public XmlSerializador(String raiz)
    {
        this.raiz = raiz;
    }
    
    public String Serializar(List<Empaquetable> listaObjetos) throws ParserConfigurationException, TransformerConfigurationException, TransformerException
    {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element raizXML = doc.createElement(raiz);
        doc.appendChild(raizXML);
        for (Empaquetable objeto : listaObjetos) {
            objeto.empaquetarXML(raizXML, doc);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.getBuffer().toString();
    }
    
    public String Serializar(Empaquetable objeto) throws ParserConfigurationException, TransformerConfigurationException, TransformerException
    {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        objeto.empaquetarXML(doc, doc);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.getBuffer().toString();
    }
    
    public String Serializar(int respuesta) throws ParserConfigurationException, TransformerConfigurationException, TransformerException
    {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element raizXML = doc.createElement(raiz);
        doc.appendChild(raizXML);
        raizXML.appendChild(doc.createTextNode(String.valueOf(respuesta)));
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.getBuffer().toString();
    }
    
    public String getDummyError(String errorMessage)
    {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Error>"+errorMessage+"</Error>";
    }
    
}
