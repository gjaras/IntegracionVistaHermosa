/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import xml.Empaquetable;

/**
 *
 * @author christian
 */
public class UsuarioDto implements Empaquetable{
    private int id;
    private String nombre;
    private String clave;
    private String tipoUsuario;
    private FuncionarioDto funcionario;

    public UsuarioDto() {
        id = -1;
        nombre = null;
        clave = null;
        tipoUsuario = null;
        
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public FuncionarioDto getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioDto funcionario) {
        this.funcionario = funcionario;
    }
    
    @Override
    public void empaquetarXML(Node nodeXML, Document doc)
    {
        Element usuarioXML = doc.createElement("Usuario");
        nodeXML.appendChild(usuarioXML);
        if(id != -1)
        {
            Element elementoId = doc.createElement("id");
            elementoId.appendChild(doc.createTextNode(String.valueOf(id)));
            usuarioXML.appendChild(elementoId);
        }
        if(nombre != null)
        {
            Element elementoNombre = doc.createElement("nombre");
            elementoNombre.appendChild(doc.createTextNode(this.nombre));
            usuarioXML.appendChild(elementoNombre);
        }
        if(clave != null)
        {
            Element elementoClave = doc.createElement("clave");
            elementoClave.appendChild(doc.createTextNode(this.clave));
            usuarioXML.appendChild(elementoClave);
        }
        if(tipoUsuario != null)
        {
            Element elementoTipoUsuario = doc.createElement("tipoUsuario");
            elementoTipoUsuario.appendChild(doc.createTextNode(this.tipoUsuario));
            usuarioXML.appendChild(elementoTipoUsuario);
        }
        if(funcionario != null)
        {
            funcionario.empaquetarXML(usuarioXML, doc);
        }
    }

    @Override
    public String toString() {
        return "UsuarioDto{" + "id=" + id + ", nombre=" + nombre + ", clave=" + clave + ", tipoUsuario=" + tipoUsuario + ", funcionario=" + ((funcionario==null)?funcionario:funcionario.toString()) + '}';
    }
    
    
    
}
