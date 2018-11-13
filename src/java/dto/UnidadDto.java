/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import xml.Empaquetable;

/**
 *
 * @author christian
 */
public class UnidadDto implements Empaquetable{
    private int id;
    private String nombre;
    private String descripcion;
    private String direccion;
    private int habilitado;
    private UnidadDto padre;
    private FuncionarioDto jefe;

    public UnidadDto() {
        padre = null;
        jefe = null;
        id = -1;
        nombre = null;
        descripcion = null;
        direccion = null;
        habilitado = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(int habilitado) {
        this.habilitado = habilitado;
    }

    public UnidadDto getPadre() {
        return padre;
    }

    public void setPadre(UnidadDto unidad) {
        this.padre = unidad;
    }

    public FuncionarioDto getJefe() {
        return jefe;
    }

    public void setJefe(FuncionarioDto jefe) {
        this.jefe = jefe;
    }
    
    
    @Override
    public void empaquetarXML(Node nodeXML, Document doc)
    {
        Element unidadXML = doc.createElement("Unidad");
        nodeXML.appendChild(unidadXML);
        if(id != -1)
        {
            Element elementoId = doc.createElement("id");
            elementoId.appendChild(doc.createTextNode(String.valueOf(id)));
            unidadXML.appendChild(elementoId);
        }
        if(nombre != null)
        {
            Element elementoNombre = doc.createElement("nombre");
            elementoNombre.appendChild(doc.createTextNode(this.nombre));
            unidadXML.appendChild(elementoNombre);
        }
        if(descripcion != null)
        {
            Element elementoDescripcion = doc.createElement("descripcion");
            elementoDescripcion.appendChild(doc.createTextNode(this.descripcion));
            unidadXML.appendChild(elementoDescripcion);
        }
        if(direccion != null)
        {
            Element elementoDireccion = doc.createElement("direccion");
            elementoDireccion.appendChild(doc.createTextNode(this.direccion));
            unidadXML.appendChild(elementoDireccion);
        }
        if(habilitado != -1)
        {
            Element elementoHabilitado = doc.createElement("habilitado");
            elementoHabilitado.appendChild(doc.createTextNode(String.valueOf(habilitado)));
            unidadXML.appendChild(elementoHabilitado);
        }
        if(padre != null)
        {
            padre.empaquetarXML(unidadXML, doc);
        }
        if(jefe != null)
        {
            jefe.empaquetarXML(unidadXML, doc);
        }
    }

    @Override
    public String toString() {
        return "UnidadDto{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", direccion=" + direccion + ", habilitado=" + habilitado + ", padre=" + ((padre==null)?padre:padre.toString()) + ", jefe=" + ((jefe==null)?jefe:jefe.toString()) + '}';
    }
    
    
    
}
