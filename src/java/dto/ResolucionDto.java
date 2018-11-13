/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import xml.Empaquetable;

/**
 *
 * @author christian
 */
public class ResolucionDto implements Empaquetable{
    private int id;
    private Date fechaResolucion;
    private int estado; 
    private PermisoDto permiso;
    private FuncionarioDto resolvente;

    public ResolucionDto() {
        id = -1;
        fechaResolucion = null;
        estado = -1;
        permiso = null;
        resolvente = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(Date fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public PermisoDto getPermiso() {
        return permiso;
    }

    public void setPermiso(PermisoDto permiso) {
        this.permiso = permiso;
    }

    public FuncionarioDto getResolvente() {
        return resolvente;
    }

    public void setResolvente(FuncionarioDto resolvente) {
        this.resolvente = resolvente;
    }

    @Override
    public void empaquetarXML(Node nodeXML, Document doc) {
        Element resolucionXML = doc.createElement("Resolucion");
        nodeXML.appendChild(resolucionXML);
        if(id != -1)
        {
            Element elementoId = doc.createElement("id");
            elementoId.appendChild(doc.createTextNode(String.valueOf(id)));
            resolucionXML.appendChild(elementoId);
        }
        if(fechaResolucion != null)
        {
            Element elementoFechaResolucion = doc.createElement("fechaResolucion");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            elementoFechaResolucion.appendChild(doc.createTextNode(sdf.format(fechaResolucion)));
            resolucionXML.appendChild(elementoFechaResolucion);
        }
        if(estado != -1)
        {
            Element elementoEstado = doc.createElement("estado");
            elementoEstado.appendChild(doc.createTextNode(String.valueOf(estado)));
            resolucionXML.appendChild(elementoEstado);
        }
        if(permiso != null)
        {
            permiso.empaquetarXML(resolucionXML, doc);
        }
        if(resolvente != null)
        {
            resolvente.empaquetarXML(resolucionXML, doc);
        }
    }

    @Override
    public String toString() {
        return "ResolucionDto{" + "id=" + id + ", fechaResolucion=" + fechaResolucion + ", estado=" + estado + ", \npermiso=" + ((permiso==null)?permiso:permiso.toString()) + ", \nresolvente=" + ((resolvente==null)?resolvente:resolvente.toString()) + '}';
    }
    
    
}
