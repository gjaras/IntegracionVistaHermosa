/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import static com.sun.faces.facelets.util.Path.context;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import xml.Empaquetable;

/**
 *
 * @author thor
 */
public class DocumentoDto implements Empaquetable{
    private int id_documento = -1;
    private String nombre_documento;
    private String formato_documento;
    private Date fecha_creacion;
    private String dir;
    private int id_permiso;

    public DocumentoDto(){
        nombre_documento = "";
        formato_documento = "";
        fecha_creacion = new Date(Long.MIN_VALUE);
        dir = "";
        id_permiso = -1;
    }

    /**
     * @return the id_documento
     */
    public int getId_documento() {
        return id_documento;
    }

    /**
     * @param id_documento the id_documento to set
     */
    public void setId_documento(int id_documento) {
        this.id_documento = id_documento;
    }

    /**
     * @return the nombre_documento
     */
    public String getNombre_documento() {
        return nombre_documento;
    }

    /**
     * @param nombre_documento the nombre_documento to set
     */
    public void setNombre_documento(String nombre_documento) {
        this.nombre_documento = nombre_documento;
    }

    /**
     * @return the formato_documento
     */
    public String getFormato_documento() {
        return formato_documento;
    }

    /**
     * @param formato_documento the formato_documento to set
     */
    public void setFormato_documento(String formato_documento) {
        this.formato_documento = formato_documento;
    }

    /**
     * @return the fecha_creacion
     */
    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    /**
     * @param fecha_creacion the fecha_creacion to set
     */
    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    /**
     * @return the dir
     */
    public String getDir() {
        return dir;
    }

    /**
     * @param dir the dir to set
     */
    public void setDir(String dir) {
        this.dir = dir;
    }

    /**
     * @return the id_permiso
     */
    public int getId_permiso() {
        return id_permiso;
    }

    /**
     * @param id_permiso the id_permiso to set
     */
    public void setId_permiso(int id_permiso) {
        this.id_permiso = id_permiso;
    }
        
     @Override
    public void empaquetarXML(Node nodeXML, Document doc)
    {
        Element documentoXML = doc.createElement("Documento");
        nodeXML.appendChild(documentoXML);
        if(id_documento != -1)
        {
            Element elementoId = doc.createElement("id_documento");
            elementoId.appendChild(doc.createTextNode(String.valueOf(id_documento)));
            documentoXML.appendChild(elementoId);
        }
        if(nombre_documento != "") 
        {        
            Element elementoNombre_documento = doc.createElement("nombre_documento");
            elementoNombre_documento.appendChild(doc.createTextNode(String.valueOf(nombre_documento)));
            documentoXML.appendChild(elementoNombre_documento);
            //AQUI INDEPENDIENTE DEL DIR SE CREARA LA CUESTION        
            String pdfBinario = this.PDFtoBytes();
            System.out.println(" PDF: " + pdfBinario.charAt(0));
            Element elementoPDF = doc.createElement("pdfBinario");
            elementoPDF.appendChild(doc.createTextNode(String.valueOf(pdfBinario)));
            documentoXML.appendChild(elementoPDF);
        }
        if(fecha_creacion != new Date(Long.MIN_VALUE))
        {
            Element elementoFecha_creacion = doc.createElement("fecha_creacion");
            elementoFecha_creacion.appendChild(doc.createTextNode(String.valueOf(fecha_creacion)));
            documentoXML.appendChild(elementoFecha_creacion);
        }
        if(id_permiso != -1)
        {
            Element elementoId_permiso = doc.createElement("id_permiso");
            elementoId_permiso.appendChild(doc.createTextNode(String.valueOf(id_permiso)));
            documentoXML.appendChild(elementoId_permiso);
        }
    }   
        
    public String PDFtoBytes(){
        String salida = "";
        FileInputStream fileInputStream = null;
        try {
            //Se crea un array de byte
            byte[] pdf = null;
            
            /**Se obtiene la direccion de la clase "DocumentoDto" en elp proyecto
             * No es necesario que sea esa clase en particular, solo es para obtener
             * el path real y manejarlo.
             */
            String classPath = DocumentoDto.class.getResource("").toString();
            String relativePath = this.getRelativePath(classPath).replaceAll("%20", " ");
            System.out.println("TEST OBTENER URL RELATIVA: "+ relativePath);
            String archivosPath = "\\src\\java\\archivos\\";
            //Se define el directorio del archivo y se crea un objeto File
            String dir =    relativePath+ //Este dependera de donde esta instalado el sistema
                            archivosPath+ //Este debe ser siempre el mismo
                            this.dir+
                            this.nombre_documento+
                            this.formato_documento;
            File file = new File(dir);
            //Se llama a la funcion loadFile, que carga los byte en la variable pdf
            pdf = loadFile(file);
            String codificado = Base64.encode(pdf); //tranforma el arreglo a un string codificado a Base64
            //Se define la salida
            salida = codificado;
        } catch (IOException ex) {
            Logger.getLogger(DocumentoDto.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return salida;
    }
    
    private static byte[] loadFile(File file) throws IOException {
	    InputStream is = new FileInputStream(file);

	    long length = file.length();
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }
	    byte[] bytes = new byte[(int)length];
	    
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }

	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file "+file.getName());
	    }

	    is.close();
	    return bytes;
	}
    
    public String getRelativePath(String classPath){
        URI uri;
        String salida = "";
        try {
            uri = new URI(classPath);
            String path = uri.getPath();
            // Return substring containing all characters before a string.
            int posA = classPath.indexOf("/build/web/WEB-INF/classes/dto/");
            if (posA == -1) {
                return "";
            }
            salida = classPath.substring(6, posA);
            
        } catch (URISyntaxException ex) {
            Logger.getLogger("Error: DocumentoDto.getRelativePath: "+DocumentoDto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return salida;
    }
}
