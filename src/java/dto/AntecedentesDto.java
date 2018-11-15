/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import xml.Empaquetable;

/**
 *
 * @author thor
 */
public class AntecedentesDto implements Empaquetable{
    
        private int permisos_administrativos_restantes;
        private int feriados_anuales_restantes;
        private List<List<Object>> filas;
        
    public AntecedentesDto() {
        permisos_administrativos_restantes = -1;
        feriados_anuales_restantes = -1;
        filas = null;
        
    }

    /**
     * @return the permisos_administrativos_restantes
     */
    public int getPermisos_administrativos_restantes() {
        return permisos_administrativos_restantes;
    }

    /**
     * @param permisos_administrativos_restantes the permisos_administrativos_restantes to set
     */
    public void setPermisos_administrativos_restantes(int permisos_administrativos_restantes) {
        this.permisos_administrativos_restantes = permisos_administrativos_restantes;
    }

    /**
     * @return the feriados_anuales_restantes
     */
    public int getFeriados_anuales_restantes() {
        return feriados_anuales_restantes;
    }

    /**
     * @param feriados_anuales_restantes the feriados_anuales_restantes to set
     */
    public void setFeriados_anuales_restantes(int feriados_anuales_restantes) {
        this.feriados_anuales_restantes = feriados_anuales_restantes;
    }

    /**
     * @return the filas
     */
    public List<List<Object>> getFilas() {
        return filas;
    }

    /**
     * @param filas the filas to set
     */
    public void setFilas(List<List<Object>> filas) {
        this.filas = filas;
    }
    
    
    @Override
    public void empaquetarXML(Node nodeXML, Document doc)
    {
        Element antecedentesXML = doc.createElement("Antecedentes");
        nodeXML.appendChild(antecedentesXML);
        if(permisos_administrativos_restantes != -1)
        {
            Element elementoPermisos_administrativos_restantes = doc.createElement("permisos_administrativos_restantes");
            elementoPermisos_administrativos_restantes.appendChild(doc.createTextNode(String.valueOf(permisos_administrativos_restantes)));
            antecedentesXML.appendChild(elementoPermisos_administrativos_restantes);
        }
        if(feriados_anuales_restantes != -1)
        {
            Element elementoFeriados_anuales_restantes = doc.createElement("feriados_anuales_restantes");
            elementoFeriados_anuales_restantes.appendChild(doc.createTextNode(String.valueOf(feriados_anuales_restantes)));
            antecedentesXML.appendChild(elementoFeriados_anuales_restantes);
        }
        if(filas != null)
        {
            //Se crea elemento XML
            Element filasXML = doc.createElement("filas");
            for (List<Object> columna : filas) {
                System.out.println("ENTRO EN FILAS");
                Element filaXML = doc.createElement("fila");
                //Luego de crear una fila se pasa la informacion detallada de permisos
                //Estado Solicitud
                Element estadoXML = doc.createElement("estado");
                estadoXML.appendChild(doc.createTextNode(String.valueOf(columna.get(0))));
                //Tipo de permiso
                Element tipo_permisoXML = doc.createElement("tipo_permiso");
                tipo_permisoXML.appendChild(doc.createTextNode(String.valueOf(columna.get(1))));
                //Cantidad
                Element cantidadXML = doc.createElement("cantidad");
                cantidadXML.appendChild(doc.createTextNode(String.valueOf(columna.get(2))));
                
                //Ahora se agregan los elementos a la fila
                filaXML.appendChild(estadoXML);
                filaXML.appendChild(tipo_permisoXML);
                filaXML.appendChild(cantidadXML);
                //Finalmente se agrega la fila al conjunto de filas
                filasXML.appendChild(filaXML);
            }
            //Se agrega el elemento al XML
            antecedentesXML.appendChild(filasXML);
        }
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        String filasTXT = "";
        if(filas==null){
            filasTXT = "Sin antecedentes";
        }else{
            filasTXT = "{";
            boolean flagPrimerElemento = true;
            for(List<Object> columna : filas){
                if(!flagPrimerElemento) //si se agrega mas de un elemento, se separaran por comas
                    filasTXT += ", ";
                filasTXT += "fila{estado="+String.valueOf(columna.get(0))+
                            ", tipo_permiso="+String.valueOf(columna.get(1))+
                            ", cantidad="+String.valueOf(columna.get(2))+"}";
            }
            filasTXT += "}";
        }
        String salida ="AntecedenteDto{" + "Permisos administrativos restantes=" +
                        this.permisos_administrativos_restantes +
                        ", feriados_anuales_restantes=" +
                        this.feriados_anuales_restantes +
                        ", Antecedentes=" + filasTXT + '}'; 
        return salida;
    }
}
