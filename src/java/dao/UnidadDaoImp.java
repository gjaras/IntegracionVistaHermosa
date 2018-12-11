/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.FuncionarioDto;
import dto.UnidadDto;
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
public class UnidadDaoImp implements UnidadDao{

    @Override
    public int insertar(UnidadDto dto) {
        try
        {
            Connection con = Conexion.getConexion();
            String sqlString;
            sqlString = "{call PR_CREAR_UNIDAD(?,?,?,?,?,?)}";
            CallableStatement proc = con.prepareCall(sqlString);
            proc.registerOutParameter(1, java.sql.Types.INTEGER);
            proc.setString(2, dto.getNombre());
            proc.setString(3,dto.getDescripcion());
            proc.setString(4, dto.getDireccion());
            if(dto.getPadre() == null)
                proc.setNull(5, java.sql.Types.INTEGER);
            else
                proc.setInt(5, dto.getPadre().getId());
            if(dto.getJefe() == null)
                proc.setNull(6, java.sql.Types.INTEGER);
            else
                proc.setInt(6, dto.getJefe().getRun());
            proc.executeQuery();
            return proc.getInt(1);
        }catch(SQLException sqle)
        {
            System.out.println("UnidadDaoImp.insertar SQL con el procedure PR_CREAR_UNIDAD: "+sqle.getMessage());
            return sqle.getErrorCode();
        }
        catch(Exception e)
        {
            System.out.println("UnidadDaoImp.insertar Error: "+e.getMessage());
            return -1;
        }
    }

    @Override
    public int modificar(UnidadDto dto) {
        try
        {
            Connection con = Conexion.getConexion();
            String sqlString;
            sqlString = "{call PR_MODIFICAR_UNIDAD(?,?,?,?,?,?,?,?)}";
            CallableStatement proc = con.prepareCall(sqlString);
            proc.registerOutParameter(1, java.sql.Types.INTEGER);
            proc.setInt(2, dto.getId());
            proc.setString(3, dto.getNombre());
            proc.setString(4,dto.getDescripcion());
            proc.setString(5, dto.getDireccion());
            proc.setInt(6, dto.getHabilitado());
            if(dto.getPadre() == null)
                proc.setNull(7, java.sql.Types.INTEGER);
            else
                proc.setInt(7, dto.getPadre().getId());
            if(dto.getJefe() == null)
                proc.setNull(8, java.sql.Types.INTEGER);
            else
                proc.setInt(8, dto.getJefe().getRun());
            System.out.println(dto.toString());
            proc.executeQuery();
            return proc.getInt(1);
        }catch(SQLException sqle)
        {
            System.out.println("UnidadDaoImp.modificar Error SQL con el procedure PR_MODIFICAR_UNIDAD: "+sqle.getMessage());
            return sqle.getErrorCode();
        }
        catch(Exception e)
        {
            System.out.println("UnidadDaoImp.modificar Error: "+e.getMessage());
            return -1;
        }
    }

    @Override
    public int eliminar(UnidadDto dto) {
        try
        {
            Connection con = Conexion.getConexion();
            String sqlString;
            sqlString = "{call PR_ELIMINAR_UNIDAD(?,?)}";
            CallableStatement proc = con.prepareCall(sqlString);
            proc.registerOutParameter(1, java.sql.Types.INTEGER);
            proc.setInt(2, dto.getId());
            proc.executeQuery();
            return proc.getInt(1);
        }catch(SQLException sqle)
        {
            System.out.println("UnidadDaoImp.eliminar Error SQL con el procedure PR_MODIFICAR_UNIDAD: "+sqle.getMessage());
            return sqle.getErrorCode();
        }
        catch(Exception e)
        {
            System.out.println("UnidadDaoImp.eliminar Error: "+e.getMessage());
            return -1;
        }
    }

    @Override
    public UnidadDto buscar(UnidadDto dto) {
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "u.nombre_unidad"
                    + ",u.descripcion_unidad"
                    + ",u.direccion_unidad"
                    + ",u.habilitado"
                    + ",u.unidad_padre_id_unidad"
                    + ",pu.nombre_unidad as nombre_padre "
                    + ",u.jefe_unidad_run_sin_dv "
                    + ",f.nom_funcionario"
                    + ",f.ap_paterno"
                    + ",f.ap_materno "
                    + "FROM UNIDAD u "
                    + "left join UNIDAD pu ON u.unidad_padre_id_unidad = pu.id_unidad "
                    + "left join FUNCIONARIO f ON f.run_sin_dv = u.jefe_unidad_run_sin_dv "
                    + "WHERE u.id_unidad = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, dto.getId());
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                dto.setNombre(rs.getString("nombre_unidad"));
                dto.setDescripcion(rs.getString("descripcion_unidad"));
                dto.setDireccion(rs.getString("direccion_unidad"));
                dto.setHabilitado(rs.getInt("habilitado"));
                if(rs.getInt("unidad_padre_id_unidad") != 0)
                {
                    UnidadDto padre = new UnidadDto();
                    padre.setId(rs.getInt("jefe_unidad_run_sin_dv"));
                    padre.setNombre(rs.getString("nombre_padre"));
                    dto.setPadre(padre);
                }
                if(rs.getInt("jefe_unidad_run_sin_dv") != 0)
                {
                    FuncionarioDto jefe = new FuncionarioDto();
                    jefe.setRun(rs.getInt("jefe_unidad_run_sin_dv"));
                    jefe.setNombre(rs.getString("nom_funcionario"));
                    jefe.setApellidoPaterno(rs.getString("ap_paterno"));
                    jefe.setApellidoMaterno(rs.getString("ap_materno"));
                    dto.setJefe(jefe);
                }
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("Unidad.buscar Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("Unidad.buscar Error: "+ex.getMessage());
        }
        return dto;
    }

    @Override
    public LinkedList<UnidadDto> listarUnidades() {
        LinkedList<UnidadDto> unidades = new LinkedList<>();
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "u.id_unidad"
                    + ",u.nombre_unidad"
                    + ",u.descripcion_unidad"
                    + ",u.direccion_unidad"
                    + ",u.habilitado"
                    + ",u.unidad_padre_id_unidad"
                    + ",pu.nombre_unidad as nombre_padre "
                    + ",pu.descripcion_unidad as descripcion_padre"
                    + ",pu.direccion_unidad as direccion_padre"
                    + ",pu.habilitado as habilitado_padre"
                    + ",u.jefe_unidad_run_sin_dv "
                    + ",f.nom_funcionario"
                    + ",f.ap_paterno"
                    + ",f.ap_materno"
                    + ",f.fec_nacimiento"
                    + ",f.correo"
                    + ",f.direc_funcionario"
                    + ",f.cargo"
                    + ",f.habilitado as habilitado_funcionario "
                    + "FROM UNIDAD u "
                    + "left join UNIDAD pu ON u.unidad_padre_id_unidad = pu.id_unidad "
                    + "left join FUNCIONARIO f ON f.run_sin_dv = u.jefe_unidad_run_sin_dv ";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                UnidadDto dto = new UnidadDto();
                dto.setId(rs.getInt("id_unidad"));
                dto.setNombre(rs.getString("nombre_unidad"));
                dto.setDescripcion(rs.getString("descripcion_unidad"));
                dto.setDireccion(rs.getString("direccion_unidad"));
                dto.setHabilitado(rs.getInt("habilitado"));
                if(rs.getInt("unidad_padre_id_unidad") != 0)
                {
                    UnidadDto padre = new UnidadDto();
                    padre.setId(rs.getInt("jefe_unidad_run_sin_dv"));
                    padre.setNombre(rs.getString("nombre_padre"));
                    padre.setDescripcion(rs.getString("descripcion_padre"));
                    padre.setDireccion(rs.getString("direccion_padre"));
                    padre.setHabilitado(rs.getInt("habilitado_padre"));
                    dto.setPadre(padre);
                }
                if(rs.getInt("jefe_unidad_run_sin_dv") != 0)
                {
                    FuncionarioDto jefe = new FuncionarioDto();
                    jefe.setRun(rs.getInt("jefe_unidad_run_sin_dv"));
                    jefe.setNombre(rs.getString("nom_funcionario"));
                    jefe.setApellidoPaterno(rs.getString("ap_paterno"));
                    jefe.setApellidoMaterno(rs.getString("ap_materno"));
                    jefe.setFechaNacimiento(rs.getDate("fec_nacimiento"));
                    jefe.setCorreo(rs.getString("correo"));
                    jefe.setDireccion(rs.getString("direc_funcionario"));
                    jefe.setCargo(rs.getString("cargo"));
                    jefe.setHabilitado(rs.getInt("habilitado_funcionario"));
                    dto.setJefe(jefe);
                }
                unidades.add(dto);
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("Unidad.listarUnidades Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("Unidad.listarUnidades Error: "+ex.getMessage());
        }
        return unidades;
    }

    @Override
    public LinkedList<UnidadDto> listadoUnidadClaveValor() {
        LinkedList<UnidadDto> unidades = new LinkedList<>();
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "id_unidad"
                    + ",nombre_unidad "
                    + "FROM UNIDAD u";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                UnidadDto dto = new UnidadDto();
                dto.setId(rs.getInt("id_unidad"));
                dto.setNombre(rs.getString("nombre_unidad"));
                unidades.add(dto);
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("Unidad.listarUnidades Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("Unidad.listarUnidades Error: "+ex.getMessage());
        }
        return unidades;
    }

    @Override
    public LinkedList<UnidadDto> listadoUnidadesHijasClaveValor(int id) {
        LinkedList<UnidadDto> unidades = new LinkedList<>();
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT u.id_unidad, u.nombre_unidad " +
                "FROM unidad u " +
                "LEFT JOIN unidad pa on u.unidad_padre_id_unidad = pa.id_unidad " +
                "WHERE u.id_unidad = ? or u.unidad_padre_id_unidad = ? or pa.unidad_padre_id_unidad = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setInt(2, id);
            stmt.setInt(3, id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                UnidadDto dto = new UnidadDto();
                dto.setId(rs.getInt("id_unidad"));
                dto.setNombre(rs.getString("nombre_unidad"));
                unidades.add(dto);
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("Unidad.listarUnidades Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("Unidad.listarUnidades Error: "+ex.getMessage());
        }
        return unidades;
    }

    @Override
    public UnidadDto buscarPorIdParcial(int id) {
        UnidadDto dto = new UnidadDto();
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "nombre_unidad"
                    + ",descripcion_unidad"
                    + ",direccion_unidad"
                    + ",habilitado "
                    + "FROM UNIDAD "
                    + "WHERE id_unidad = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                dto.setId(id);
                dto.setNombre(rs.getString("nombre_unidad"));
                dto.setDescripcion(rs.getString("descripcion_unidad"));
                dto.setDireccion(rs.getString("direccion_unidad"));
                dto.setHabilitado(rs.getInt("habilitado"));
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("Unidad.buscar Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("Unidad.buscar Error: "+ex.getMessage());
        }
        return dto;
    }

    @Override
    public boolean nombreUnidadExiste(String nombre) {
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "Select * from unidad where UPPER(nombre_unidad) = UPPER(?)";
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
            System.out.println("Unidad.nombreUnidadExiste Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("Unidad.nombreUnidadExiste Error: "+ex.getMessage());
        }
        return false;
    }
    
}
