/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.FuncionarioDto;
import dto.UsuarioDto;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import sql.Conexion;

/**
 *
 * @author christian
 */
public class UsuarioDaoImp implements UsuarioDao{

    @Override
    public int insertar(UsuarioDto dto) {
        try
        {
            Connection con = Conexion.getConexion();
            String sqlString;
            sqlString = "{call PR_CREAR_USUARIO(?,?,?,?,?)}";
            CallableStatement proc = con.prepareCall(sqlString);
            proc.registerOutParameter(1, java.sql.Types.INTEGER);
            proc.setString(2, dto.getNombre());
            proc.setString(3,dto.getClave());
            proc.setString(4, dto.getTipoUsuario());
            proc.setInt(5, dto.getFuncionario().getRun());
            proc.executeQuery();
            return proc.getInt(1);
        }catch(SQLException sqle)
        {
            System.out.println("UsuarioDaoImp.insertar Error SQL con el procedure PR_CREAR_USUARIO: "+sqle.getMessage());
            //return sqle.getErrorCode();
            return -1;
        }
        catch(Exception e)
        {
            System.out.println("UsuarioDaoImp.insertar Error: "+e.getMessage());
            return -1;
        }
    }

    @Override
    public int modificar(UsuarioDto dto) {
        try
        {
            Connection con = Conexion.getConexion();
            String sqlString;
            sqlString = "{call PR_MODIFICAR_USUARIO(?,?,?,?,?,?)}";
            CallableStatement proc = con.prepareCall(sqlString);
            proc.registerOutParameter(1, java.sql.Types.INTEGER);
            proc.setInt(2, dto.getId());
            proc.setString(3, dto.getNombre());
            proc.setString(4,dto.getClave());
            proc.setString(5, dto.getTipoUsuario());
            proc.setInt(6, dto.getFuncionario().getRun());
            proc.executeQuery();
            return proc.getInt(1);
        }catch(SQLException sqle)
        {
            System.out.println("UsuarioDaoImp.modificar Error SQL con el procedure PR_MODIFICAR_USUARIO: "+sqle.getMessage());
            return sqle.getErrorCode();
        }
        catch(Exception e)
        {
            System.out.println("UsuarioDaoImp.modificar Error: "+e.getMessage());
            return -1;
        }
    }

    @Override
    public int eliminar(UsuarioDto dto) {
        try
        {
            Connection con = Conexion.getConexion();
            String sqlString;
            sqlString = "{call PR_ELIMINAR_USUARIO(?,?)}";
            CallableStatement proc = con.prepareCall(sqlString);
            proc.registerOutParameter(1, java.sql.Types.INTEGER);
            proc.setInt(2, dto.getId());
            proc.executeQuery();
            return proc.getInt(1);
        }catch(SQLException sqle)
        {
            System.out.println("UsuarioDaoImp.eliminar Error SQL con el procedure PR_ELIMINAR_USUARIO: "+sqle.getMessage());
            return sqle.getErrorCode();
        }
        catch(Exception e)
        {
            System.out.println("UsuarioDaoImp.eliminar Error: "+e.getMessage());
            return -1;
        }
    }

    @Override
    public UsuarioDto buscar(UsuarioDto dto) {
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "u.id_usuario"
                    + ",u.nombre_usuario"
                    + ",u.clave"
                    + ",u.tipo_usuario"
                    + ",u.funcionario_run_sin_dv"
                    + ",f.run_dv"
                    + ",f.nom_funcionario"
                    + ",f.ap_paterno"
                    + ",f.ap_materno"
                    + ",f.fec_nacimiento"
                    + ",f.correo"
                    + ",f.direc_funcionario"
                    + ",f.cargo"
                    + ",f.habilitado "
                    + "FROM USUARIO u "
                    + "left join FUNCIONARIO f ON f.run_sin_dv = u.funcionario_run_sin_dv "
                    + "WHERE id_usuario = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, dto.getId());
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                dto.setNombre(rs.getString("nombre_usuario"));
                dto.setClave(rs.getString("clave"));
                dto.setTipoUsuario(rs.getString("tipo_usuario"));
                if(rs.getInt("funcionario_run_sin_dv") != 0)
                {
                    FuncionarioDto funcionario = new FuncionarioDto();
                    funcionario.setRun(rs.getInt("funcionario_run_sin_dv"));
                    funcionario.setDv(rs.getInt("run_dv"));
                    funcionario.setNombre(rs.getString("nom_funcionario"));
                    funcionario.setApellidoPaterno(rs.getString("ap_paterno"));
                    funcionario.setApellidoMaterno(rs.getString("ap_materno"));
                    funcionario.setFechaNacimiento(rs.getDate("fec_nacimiento"));
                    funcionario.setCorreo(rs.getString("correo"));
                    funcionario.setDireccion(rs.getString("direc_funcionario"));
                    funcionario.setCargo(rs.getString("cargo"));
                    funcionario.setHabilitado(rs.getInt("habilitado"));
                    dto.setFuncionario(funcionario);
                }
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("UsuarioDaoImp.buscar Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("UsuarioDaoImp.buscar Error: "+ex.getMessage());
        }
        return dto;
    }

    @Override
    public LinkedList<UsuarioDto> listarUsuarios() {
        LinkedList<UsuarioDto> usuarios = new LinkedList<>();
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "u.id_usuario"
                    + ",u.nombre_usuario"
                    + ",u.clave"
                    + ",u.tipo_usuario"
                    + ",u.funcionario_run_sin_dv"
                    + ",f.run_dv"
                    + ",f.nom_funcionario"
                    + ",f.ap_paterno"
                    + ",f.ap_materno"
                    + ",f.fec_nacimiento"
                    + ",f.correo"
                    + ",f.direc_funcionario"
                    + ",f.cargo"
                    + ",f.habilitado "
                    + "FROM USUARIO u "
                    + "left join FUNCIONARIO f ON f.run_sin_dv = u.funcionario_run_sin_dv ";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                UsuarioDto dto = new UsuarioDto();
                dto.setId(rs.getInt("id_usuario"));
                dto.setNombre(rs.getString("nombre_usuario"));
                dto.setClave(rs.getString("clave"));
                dto.setTipoUsuario(rs.getString("tipo_usuario"));
                if(rs.getInt("funcionario_run_sin_dv") != 0)
                {
                    FuncionarioDto funcionario = new FuncionarioDto();
                    funcionario.setRun(rs.getInt("funcionario_run_sin_dv"));
                    funcionario.setDv(rs.getInt("run_dv"));
                    funcionario.setNombre(rs.getString("nom_funcionario"));
                    funcionario.setApellidoPaterno(rs.getString("ap_paterno"));
                    funcionario.setApellidoMaterno(rs.getString("ap_materno"));
                    funcionario.setFechaNacimiento(rs.getDate("fec_nacimiento"));
                    funcionario.setCorreo(rs.getString("correo"));
                    funcionario.setDireccion(rs.getString("direc_funcionario"));
                    funcionario.setCargo(rs.getString("cargo"));
                    funcionario.setHabilitado(rs.getInt("habilitado"));
                    dto.setFuncionario(funcionario);
                }
                usuarios.add(dto);
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("UsuarioDaoImp.listarUsuarios Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("UsuarioDaoImp.listarUsuarios Error: "+ex.getMessage());
        }
        return usuarios;
    }

    @Override
    public boolean usuarioExiste(String nombre) {
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT id_usuario FROM USUARIO WHERE nombre_usuario = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                return true;
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("UsuarioDaoImp.usuarioExiste Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("UsuarioDaoImp.usuarioExiste Error: "+ex.getMessage());
        }
        return false;
    }

    @Override
    public UsuarioDto autenticarUsuario(String nombre, String clave) {
        UsuarioDto dto = new UsuarioDto();
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT id_usuario ,nombre_usuario ,clave ,tipo_usuario, funcionario_run_sin_dv FROM USUARIO WHERE nombre_usuario = ? and clave = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, clave);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                
                dto.setId(rs.getInt("id_usuario"));
                dto.setNombre(rs.getString("nombre_usuario"));
                dto.setClave(rs.getString("clave"));
                dto.setTipoUsuario(rs.getString("tipo_usuario"));
                FuncionarioDto funcionario = new FuncionarioDto();
                funcionario.setRun(rs.getInt("funcionario_run_sin_dv"));
                dto.setFuncionario(new FuncionarioDaoImp().buscar(funcionario));
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("UsuarioDaoImp.usuarioExiste Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("UsuarioDaoImp.usuarioExiste Error: "+ex.getMessage());
        }
        return dto;
    }

    @Override
    public LinkedList<UsuarioDto> listarUsuariosWithParameters(String id, String tipo, String nombre) {
        LinkedList<UsuarioDto> usuarios = new LinkedList<>();
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "u.id_usuario"
                    + ",u.nombre_usuario"
                    + ",u.tipo_usuario"
                    + ",u.funcionario_run_sin_dv"
                    + ",f.run_dv "
                    + "FROM USUARIO u "
                    + "left join FUNCIONARIO f ON f.run_sin_dv = u.funcionario_run_sin_dv ";
            
            if(!id.isEmpty() || !tipo.isEmpty() || !nombre.isEmpty()){
                sql += "where ";
                if(!id.isEmpty()){
                    sql += "u.id_usuario = "+id;
                }
                if(!tipo.isEmpty()){
                    if(!id.isEmpty()){
                        sql += " and ";
                    }
                    sql += "u.tipo_usuario = '"+tipo+"'";
                }
                if(!nombre.isEmpty()){
                    if(!id.isEmpty() || !tipo.isEmpty()){
                        sql += " and ";
                    }
                    sql += "upper(u.nombre_usuario) like upper('%"+nombre+"%')";
                }    
            }
            System.out.println(sql);
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                UsuarioDto dto = new UsuarioDto();
                dto.setId(rs.getInt("id_usuario"));
                dto.setNombre(rs.getString("nombre_usuario"));
                dto.setTipoUsuario(rs.getString("tipo_usuario"));
                if(rs.getInt("funcionario_run_sin_dv") != 0)
                {
                    FuncionarioDto funcionario = new FuncionarioDto();
                    funcionario.setRun(rs.getInt("funcionario_run_sin_dv"));
                    funcionario.setDv(rs.getInt("run_dv"));
                    dto.setFuncionario(funcionario);
                }
                usuarios.add(dto);
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("UsuarioDaoImp.listarUsuariosWithParameters Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("UsuarioDaoImp.listarUsuariosWith Parameters Error: ");
            ex.printStackTrace();
        }
        return usuarios;
    }
}
