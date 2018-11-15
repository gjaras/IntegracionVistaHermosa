/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import xml.Empaquetable;

/**
 *
 * @author thor
 */
public class ReportePermisosFilaDto implements Empaquetable{
        private String unidad;
        private String tipo_permiso; 
        private int cantidad;
        
        public ReportePermisosFilaDto(){
            unidad = "";
            tipo_permiso = ""; 
            cantidad = -1;
        }

    /**
     * @return the unidad
     */
    public String getUnidad() {
        return unidad;
    }

    /**
     * @param unidad the unidad to set
     */
    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    /**
     * @return the tipo_permiso
     */
    public String getTipo_permiso() {
        return tipo_permiso;
    }

    /**
     * @param tipo_permiso the tipo_permiso to set
     */
    public void setTipo_permiso(String tipo_permiso) {
        this.tipo_permiso = tipo_permiso;
    }

    /**
     * @return the cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
        
    @Override
    public void empaquetarXML(Node nodeXML, Document doc)
    {
        Element reportePermisosFilaXML = doc.createElement("Antecedentes");
        nodeXML.appendChild(reportePermisosFilaXML);
        if(unidad != "")
        {
            Element elementoUnidad = doc.createElement("unidad");
            elementoUnidad.appendChild(doc.createTextNode(String.valueOf(unidad)));
            reportePermisosFilaXML.appendChild(elementoUnidad);
        }
        if(tipo_permiso != "")
        {
            Element elementoTipo_permiso = doc.createElement("tipo_permiso");
            elementoTipo_permiso.appendChild(doc.createTextNode(String.valueOf(tipo_permiso)));
            reportePermisosFilaXML.appendChild(elementoTipo_permiso);
        }
        if(cantidad != -1)
        {
            Element elementoCantidad = doc.createElement("cantidad");
            elementoCantidad.appendChild(doc.createTextNode(String.valueOf(cantidad)));
            reportePermisosFilaXML.appendChild(elementoCantidad);
        }
    }    
}
