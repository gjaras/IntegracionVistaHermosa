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
import dto.PermisoDto;
import javax.xml.transform.OutputKeys;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;

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
    
    public String generarArchivoXmlAnualPermiso(List<PermisoDto> permisos) throws ParserConfigurationException, TransformerException
    {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootXML = doc.createElement("MENSAJE");
        doc.appendChild(rootXML);
        for (PermisoDto permiso : permisos) {
            permiso.empaquetarPermisoParaArchivoXml(rootXML, doc);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        DOMImplementation domImpl = doc.getImplementation();
        DocumentType doctype = domImpl.createDocumentType("doctype",
            "",
            "define_permiso.dtd");
        //transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.getBuffer().toString();
    }
    
    public String getDummyError(String errorMessage)
    {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Error>"+errorMessage+"</Error>";
    }
    
}
