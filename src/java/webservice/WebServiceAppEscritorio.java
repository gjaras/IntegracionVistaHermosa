/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import dao.FuncionarioDaoImp;
import dao.PermisoDaoImp;
import dao.ResolucionDaoImp;
import dao.UnidadDaoImp;
import dao.UsuarioDaoImp;
import dto.FuncionarioDto;
import dto.UnidadDto;
import dto.UsuarioDto;
import java.util.Date;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import xml.XmlSerializador;

/**
 *
 * @author christian
 */
@WebService(serviceName = "WebServiceAppEscritorio")
public class WebServiceAppEscritorio {

    @WebMethod(operationName = "listarUnidades")
    public String listarUnidades(){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Unidades").Serializar((List)new UnidadDaoImp().listarUnidades());
        }
        catch(Exception ex)
        {
            System.out.println("Error:"+ex.getMessage());
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "listadoUnidadClaveValor")
    public String listadoUnidadClaveValor(){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Unidades").Serializar((List)new UnidadDaoImp().listadoUnidadClaveValor());
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "listadoUnidadesHijasClaveValor")
    public String listadoUnidadesHijasClaveValor(@WebParam(name = "id") int id){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Unidades").Serializar((List)new UnidadDaoImp().listadoUnidadesHijasClaveValor(id));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "buscarUnidadPorIdParcial")
    public String buscarUnidadPorIdParcial(@WebParam(name = "id") int id){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Unidad").Serializar(new UnidadDaoImp().buscarPorIdParcial(id));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "nombreUnidadExiste")
    public String nombreUnidadExiste(@WebParam(name = "nombre") String nombre){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Respuesta").Serializar((new UnidadDaoImp().nombreUnidadExiste(nombre))?1:0);
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "insertarUnidad")
    public String insertarUnidad(@WebParam(name = "nombre") String nombre, @WebParam(name = "descripcion") String descripcion, @WebParam(name = "direccion") String direccion,@WebParam(name = "unidad_padre") int unidad_padre, @WebParam(name = "jefe_unidad") int jefe_unidad){
        String mensajeRetorno;
        UnidadDto unidad = new UnidadDto();
        unidad.setNombre(nombre);
        unidad.setDescripcion(descripcion);
        unidad.setDireccion(direccion);
        if(unidad_padre > 0)
        {
            UnidadDto padre = new UnidadDto();
            padre.setId(unidad_padre);
            unidad.setPadre(unidad);
        }
        if(jefe_unidad > 0)
        {
            FuncionarioDto jefe = new FuncionarioDto();
            jefe.setRun(jefe_unidad);
            unidad.setJefe(jefe);
        }
        try
        {
            mensajeRetorno = new XmlSerializador("Respuesta").Serializar(new UnidadDaoImp().insertar(unidad));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "modificarUnidad")
    public String modificarUnidad(@WebParam(name = "id_unidad") int id_unidad, @WebParam(name = "nombre") String nombre, @WebParam(name = "descripcion") String descripcion,@WebParam(name = "direccion") String direccion,@WebParam(name = "habilitado") boolean habilitado,@WebParam(name = "unidad_padre") int unidad_padre,@WebParam(name = "jefe_unidad") int jefe_unidad){
        String mensajeRetorno;
        UnidadDto unidad = new UnidadDto();
        unidad.setId(id_unidad);
        unidad.setNombre(nombre);
        unidad.setDescripcion(descripcion);
        unidad.setDireccion(direccion);
        unidad.setHabilitado(habilitado?1:0);
        if(unidad_padre > 0)
        {
            UnidadDto padre = new UnidadDto();
            padre.setId(unidad_padre);
            unidad.setPadre(unidad);
        }
        if(jefe_unidad > 0)
        {
            FuncionarioDto jefe = new FuncionarioDto();
            jefe.setRun(jefe_unidad);
            unidad.setJefe(jefe);
        }
        try
        {
            mensajeRetorno = new XmlSerializador("Respuesta").Serializar(new UnidadDaoImp().modificar(unidad));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "eliminarUnidad")
    public String eliminarUnidad(@WebParam(name = "id_unidad") int id_unidad){
        String mensajeRetorno;
        UnidadDto unidad = new UnidadDto();
        unidad.setId(id_unidad);
        try
        {
            mensajeRetorno = new XmlSerializador("Respuesta").Serializar(new UnidadDaoImp().insertar(unidad));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "buscarFuncionarioParcial")
    public String buscarFuncionarioParcial(@WebParam(name = "run") int run){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Funcionario").Serializar(new FuncionarioDaoImp().buscarFuncionarioParcial(run));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "buscarFuncionario")
    public String buscarFuncionario(@WebParam(name = "run") int run){
        String mensajeRetorno;
        FuncionarioDto dto = new FuncionarioDto();
        dto.setRun(run);
        try
        {
            mensajeRetorno = new XmlSerializador("Funcionario").Serializar(new FuncionarioDaoImp().buscar(dto));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "listarFuncionarios")
    public String listarFuncionarios(){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Funcionarios").Serializar((List)new FuncionarioDaoImp().listarFuncionarios());
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "listarFuncionariosDeUnidad")
    public String listarFuncionariosDeUnidad(@WebParam(name = "idUnidad") int idUnidad){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Funcionarios").Serializar((List)new FuncionarioDaoImp().listarFuncionariosDeUnidad(idUnidad));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "listarFuncionariosNoJefesClaveValor")
    public String listarFuncionariosNoJefesClaveValor(){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Funcionarios").Serializar((List)new FuncionarioDaoImp().listarFuncionariosNoJefesClaveValor());
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "listarFuncionariosNoJefesClaveValorExeptoUnidad")
    public String listarFuncionariosNoJefesClaveValorExeptoUnidad(@WebParam(name = "idUnidad") int idUnidad){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Funcionarios").Serializar((List)new FuncionarioDaoImp().listarFuncionariosNoJefesClaveValor(idUnidad));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "listarFuncionariosClaveValor")
    public String listarFuncionariosClaveValor(){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Funcionarios").Serializar((List)new FuncionarioDaoImp().listarFuncionariosClaveValor());
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "insertarFuncionario")
    public String insertarFuncionario(
                            @WebParam(name = "run") int run, 
                            @WebParam(name = "dv") int dv, 
                            @WebParam(name = "nombre") String nombre, 
                            @WebParam(name = "ap_pat") String ap_pat,
                            @WebParam(name = "ap_mat") String ap_mat, 
                            @WebParam(name = "nacimiento") Date nacimiento, 
                            @WebParam(name = "correo") String correo,
                            @WebParam(name = "direccion") String direccion, 
                            @WebParam(name = "cargo") String cargo,
                            @WebParam(name = "id_unidad") int id_unidad){
        String mensajeRetorno;
        FuncionarioDto funcionario = new FuncionarioDto();
        funcionario.setRun(run);
        funcionario.setDv(dv);
        funcionario.setNombre(nombre);
        funcionario.setApellidoPaterno(ap_pat);
        funcionario.setApellidoMaterno(ap_mat);
        funcionario.setCargo(cargo);
        funcionario.setCorreo(correo);
        funcionario.setFechaNacimiento(nacimiento);
        funcionario.setDireccion(direccion);
        UnidadDto unidad = new UnidadDto();
        unidad.setId(id_unidad);
        funcionario.setUnidad(unidad);
        try
        {
            mensajeRetorno = new XmlSerializador("Respuesta").Serializar(new FuncionarioDaoImp().insertar(funcionario));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    
    @WebMethod(operationName = "modificarFuncionario")
    public String modificarFuncionario(
                            @WebParam(name = "run") int run, 
                            @WebParam(name = "dv") int dv, 
                            @WebParam(name = "nombre") String nombre, 
                            @WebParam(name = "ap_pat") String ap_pat,
                            @WebParam(name = "ap_mat") String ap_mat, 
                            @WebParam(name = "nacimiento") Date nacimiento, 
                            @WebParam(name = "correo") String correo,
                            @WebParam(name = "direccion") String direccion, 
                            @WebParam(name = "cargo") String cargo,
                            @WebParam(name = "habilitado") boolean habilitado,
                            @WebParam(name = "id_unidad") int id_unidad){
        String mensajeRetorno;
        FuncionarioDto funcionario = new FuncionarioDto();
        funcionario.setRun(run);
        funcionario.setDv(dv);
        funcionario.setNombre(nombre);
        funcionario.setApellidoPaterno(ap_pat);
        funcionario.setApellidoMaterno(ap_mat);
        funcionario.setCargo(cargo);
        funcionario.setCorreo(correo);
        funcionario.setFechaNacimiento(nacimiento);
        funcionario.setDireccion(direccion);
        funcionario.setHabilitado(habilitado?1:0);
        UnidadDto unidad = new UnidadDto();
        unidad.setId(id_unidad);
        funcionario.setUnidad(unidad);
        try
        {
            mensajeRetorno = new XmlSerializador("Respuesta").Serializar(new FuncionarioDaoImp().modificar(funcionario));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "eliminarFuncionario")
    public String eliminarFuncionario(@WebParam(name = "run") int run){
        String mensajeRetorno;
        FuncionarioDto funcionario = new FuncionarioDto();
        funcionario.setRun(run);
        try
        {
            mensajeRetorno = new XmlSerializador("Respuesta").Serializar(new FuncionarioDaoImp().eliminar(funcionario));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    
    
    @WebMethod(operationName = "listarUsuarios")
    public String listarUsuarios(){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Usuarios").Serializar((List)new UsuarioDaoImp().listarUsuarios());
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "usuarioExiste")
    public String usuarioExiste(@WebParam(name = "nombre") String nombre){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Respuesta").Serializar((new UsuarioDaoImp().usuarioExiste(nombre))?1:0);
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "insertarUsuario")
    public String insertarUsuario(
                            @WebParam(name = "nombre") String nombre, 
                            @WebParam(name = "clave") String clave,
                            @WebParam(name = "tipo") String tipo,
                            @WebParam(name = "run") int run){
        String mensajeRetorno;
        UsuarioDto usuario = new UsuarioDto();
        usuario.setNombre(nombre);
        usuario.setClave(clave);
        usuario.setTipoUsuario(tipo);
        FuncionarioDto funcionario = new FuncionarioDto();
        funcionario.setRun(run);
        usuario.setFuncionario(funcionario);
        try
        {
            mensajeRetorno = new XmlSerializador("Respuesta").Serializar(new UsuarioDaoImp().insertar(usuario));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "modificarUsuario")
    public String modificarUsuario(
                            @WebParam(name = "id") int id, 
                            @WebParam(name = "nombre") String nombre, 
                            @WebParam(name = "clave") String clave,
                            @WebParam(name = "tipo") String tipo,
                            @WebParam(name = "run") int run){
        String mensajeRetorno;
        UsuarioDto usuario = new UsuarioDto();
        usuario.setId(id);
        usuario.setNombre(nombre);
        usuario.setClave(clave);
        usuario.setTipoUsuario(tipo);
        FuncionarioDto funcionario = new FuncionarioDto();
        funcionario.setRun(run);
        usuario.setFuncionario(funcionario);
        try
        {
            mensajeRetorno = new XmlSerializador("Respuesta").Serializar(new UsuarioDaoImp().modificar(usuario));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "eliminarUsuario")
    public String eliminarUsuario(@WebParam(name = "id") int id){
        String mensajeRetorno;
        UsuarioDto usuario = new UsuarioDto();
        usuario.setId(id);
        try
        {
            mensajeRetorno = new XmlSerializador("Respuesta").Serializar(new UsuarioDaoImp().eliminar(usuario));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "buscarUsuario")
    public String buscarUsuario(@WebParam(name = "id") int id){
        String mensajeRetorno;
        UsuarioDto usuario = new UsuarioDto();
        usuario.setId(id);
        try
        {
            mensajeRetorno = new XmlSerializador("Usuario").Serializar(new UsuarioDaoImp().buscar(usuario));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    @WebMethod(operationName = "autenticarUsuario")
    public String autenticarUsuario(@WebParam(name = "nombre") String nombre, @WebParam(name = "clave") String clave){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Usuario").Serializar(new UsuarioDaoImp().autenticarUsuario(nombre,clave));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    
    @WebMethod(operationName = "buscarPermisos")
    public String buscarPermisos(@WebParam(name = "run") int run){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Permisos").Serializar((List)new PermisoDaoImp().buscarPermisos(run));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    
    @WebMethod(operationName = "buscarResolucionesUnidadesSubHijas")
    public String buscarResolucionesUnidadesSubHijas(@WebParam(name = "mes") int mes,@WebParam(name = "anno") int anno,@WebParam(name = "idUnidad") int idUnidad){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Resoluciones").Serializar((List)new ResolucionDaoImp().buscarResoluciones(mes,anno,idUnidad));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    
    @WebMethod(operationName = "buscarResoluciones")
    public String buscarResoluciones(@WebParam(name = "mes") int mes, @WebParam(name = "anno") int anno){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Resoluciones").Serializar((List)new ResolucionDaoImp().buscarResoluciones(mes,anno));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    
    @WebMethod(operationName = "validarResolucion")
    public String validarResolucion(@WebParam(name = "idResolucion") int idResolucion, @WebParam(name = "runResolvente") int runResolvente){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Respuesta").Serializar(new ResolucionDaoImp().validarResolucion(idResolucion,runResolvente));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    
    @WebMethod(operationName = "invalidarResolucion")
    public String invalidarResolucion(@WebParam(name = "idResolucion") int idResolucion, @WebParam(name = "runResolvente") int runResolvente){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("Respuesta").Serializar(new ResolucionDaoImp().invalidarResolucion(idResolucion,runResolvente));
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    
    @WebMethod(operationName = "buscarPermisosAnuales")
    public String buscarPermisosAnuales(){
        String mensajeRetorno;
        try
        {
            mensajeRetorno = new XmlSerializador("").generarArchivoXmlAnualPermiso((List)new PermisoDaoImp().buscarPermisosAnuales());
        }
        catch(Exception ex)
        {
            mensajeRetorno = new XmlSerializador("").getDummyError(ex.getMessage());
        }
        return mensajeRetorno;
    }
    
}
