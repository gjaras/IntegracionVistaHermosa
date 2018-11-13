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
public class FuncionarioDto implements Empaquetable{
    private int run;
    private int dv;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Date fechaNacimiento;
    private String correo;
    private String direccion;
    private String cargo;
    private int habilitado;
    private UnidadDto unidad;
    

    public FuncionarioDto() {
        run = -1;
        dv = -1;
        nombre = null;
        apellidoPaterno = null;
        apellidoMaterno = null;
        fechaNacimiento = null;
        correo = null;
        direccion = null;
        cargo = null;
        habilitado= -1;
        
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public int getDv() {
        return dv;
    }

    public void setDv(int dv) {
        this.dv = dv;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(int habilitado) {
        this.habilitado = habilitado;
    }

    public UnidadDto getUnidad() {
        return unidad;
    }

    public void setUnidad(UnidadDto unidad) {
        this.unidad = unidad;
    }
    
    @Override
    public void empaquetarXML(Node nodeXML, Document doc)
    {
        Element unidadXML = doc.createElement("Funcionario");
        nodeXML.appendChild(unidadXML);
        if(run != -1)
        {
            Element elementoRun = doc.createElement("run");
            elementoRun.appendChild(doc.createTextNode(String.valueOf(run)));
            unidadXML.appendChild(elementoRun);
        }
        if(dv != -1)
        {
            Element elementoDv = doc.createElement("dv");
            elementoDv.appendChild(doc.createTextNode(String.valueOf(dv)));
            unidadXML.appendChild(elementoDv);
        }
        if(nombre != null)
        {
            Element elementoNombre = doc.createElement("nombre");
            elementoNombre.appendChild(doc.createTextNode(this.nombre));
            unidadXML.appendChild(elementoNombre);
        }
        if(apellidoPaterno != null)
        {
            Element elementoApellidoPaterno = doc.createElement("apellidoPaterno");
            elementoApellidoPaterno.appendChild(doc.createTextNode(this.apellidoPaterno));
            unidadXML.appendChild(elementoApellidoPaterno);
        }
        if(apellidoMaterno != null)
        {
            Element elementoApellidoMaterno = doc.createElement("apellidoMaterno");
            elementoApellidoMaterno.appendChild(doc.createTextNode(this.apellidoMaterno));
            unidadXML.appendChild(elementoApellidoMaterno);
        }
        if(fechaNacimiento != null)
        {
            Element elementoFechaNacimiento = doc.createElement("fechaNacimiento");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            elementoFechaNacimiento.appendChild(doc.createTextNode(sdf.format(fechaNacimiento)));
            unidadXML.appendChild(elementoFechaNacimiento);
        }
        if(correo != null)
        {
            Element elementoCorreo = doc.createElement("correo");
            elementoCorreo.appendChild(doc.createTextNode(this.correo));
            unidadXML.appendChild(elementoCorreo);
        }
        if(direccion != null)
        {
            Element elementoDireccion = doc.createElement("direccion");
            elementoDireccion.appendChild(doc.createTextNode(this.direccion));
            unidadXML.appendChild(elementoDireccion);
        }
        if(cargo != null)
        {
            Element elementoCargo = doc.createElement("cargo");
            elementoCargo.appendChild(doc.createTextNode(this.cargo));
            unidadXML.appendChild(elementoCargo);
        }
        if(habilitado != -1)
        {
            Element elementoHabilitado = doc.createElement("habilitado");
            elementoHabilitado.appendChild(doc.createTextNode(String.valueOf(habilitado)));
            unidadXML.appendChild(elementoHabilitado);
        }
        if(unidad != null)
        {
            unidad.empaquetarXML(unidadXML, doc);
        }
    }

    @Override
    public String toString() {
        return "FuncionarioDto{" + "run=" + run + ", dv=" + dv + ", nombre=" + nombre + ", apellidoPaterno=" + apellidoPaterno + ", apellidoMaterno=" + apellidoMaterno + ", fechaNacimiento=" + fechaNacimiento + ", correo=" + correo + ", direccion=" + direccion + ", cargo=" + cargo + ", habilitado=" + habilitado + ", idUnidad=" + ((unidad == null)?unidad:unidad.toString()) + '}';
    }
    
}
