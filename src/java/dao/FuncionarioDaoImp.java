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
public class FuncionarioDaoImp implements FuncionarioDao{

    @Override
    public int insertar(FuncionarioDto dto) {
        try
        {
            Connection con = Conexion.getConexion();
            String sqlString;
            sqlString = "{call PR_CREAR_FUNCIONARIO(?,?,?,?,?,?,?,?,?,?,?)}";
            CallableStatement proc = con.prepareCall(sqlString);
            proc.registerOutParameter(1, java.sql.Types.INTEGER);
            proc.setInt(2, dto.getRun());
            proc.setInt(3,dto.getDv());
            proc.setString(4, dto.getNombre());
            proc.setString(5, dto.getApellidoPaterno());
            proc.setString(6, dto.getApellidoMaterno());
            proc.setDate(7,new java.sql.Date(dto.getFechaNacimiento().getTime()));
            proc.setString(8, dto.getCorreo());
            proc.setString(9, dto.getDireccion());
            proc.setString(10, dto.getCargo());
            proc.setInt(11, dto.getUnidad().getId());
            proc.executeQuery();
            return proc.getInt(1);
        }catch(SQLException sqle)
        {
            System.out.println("FuncionarioDaoImp.insertar Error SQL con el procedure PR_CREAR_FUNCIONARIO: "+sqle.getMessage());
            return sqle.getErrorCode();
        }
        catch(Exception e)
        {
            System.out.println("FuncionarioDaoImp.insertar Error: "+e.getMessage());
            return -1;
        }
    }

    @Override
    public int modificar(FuncionarioDto dto) {
        try
        {
            Connection con = Conexion.getConexion();
            String sqlString;
            sqlString = "{call PR_MODIFICAR_FUNCIONARIO(?,?,?,?,?,?,?,?,?,?,?)}";
            CallableStatement proc = con.prepareCall(sqlString);
            proc.registerOutParameter(1, java.sql.Types.INTEGER);
            proc.setInt(2, dto.getRun());
            proc.setString(3, dto.getNombre());
            proc.setString(4, dto.getApellidoPaterno());
            proc.setString(5, dto.getApellidoMaterno());
            proc.setDate(6,new java.sql.Date(dto.getFechaNacimiento().getTime()));
            proc.setString(7, dto.getCorreo());
            proc.setString(8, dto.getDireccion());
            proc.setString(9, dto.getCargo());
            proc.setInt(10, dto.getHabilitado());
            proc.setInt(11, dto.getUnidad().getId());
            proc.executeQuery();
            return proc.getInt(1);
        }catch(SQLException sqle)
        {
            System.out.println("FuncionarioDaoImp.modificar Error SQL con el procedure PR_MODIFICAR_FUNCIONARIO: "+sqle.getMessage());
            return sqle.getErrorCode();
        }
        catch(Exception e)
        {
            System.out.println("FuncionarioDaoImp.modificar Error: "+e.getMessage());
            return -1;
        }
    }

    @Override
    public int eliminar(FuncionarioDto dto) {
        try
        {
            Connection con = Conexion.getConexion();
            String sqlString;
            sqlString = "{call PR_ELIMINAR_FUNCIONARIO(?,?)}";
            CallableStatement proc = con.prepareCall(sqlString);
            proc.registerOutParameter(1, java.sql.Types.INTEGER);
            proc.setInt(2, dto.getRun());
            proc.executeQuery();
            return proc.getInt(1);
        }catch(SQLException sqle)
        {
            System.out.println("FuncionarioDaoImp.eliminar Error SQL con el procedure PR_ELIMINAR_FUNCIONARIO: "+sqle.getMessage());
            return sqle.getErrorCode();
        }
        catch(Exception e)
        {
            System.out.println("FuncionarioDaoImp.eliminar Error: "+e.getMessage());
            return -1;
        }
    }

    @Override
    public FuncionarioDto buscar(FuncionarioDto dto) {
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "f.run_dv"
                    + ",f.nom_funcionario"
                    + ",f.ap_paterno"
                    + ",f.ap_materno"
                    + ",f.fec_nacimiento"
                    + ",f.correo"
                    + ",f.direc_funcionario"
                    + ",f.cargo"
                    + ",f.habilitado"
                    + ",f.unidad_id_unidad "
                    + ",u.nombre_unidad"
                    + ",u.descripcion_unidad"
                    + ",u.direccion_unidad"
                    + ",u.habilitado as habilitado_unidad "
                    + "FROM FUNCIONARIO f "
                    + "LEFT JOIN UNIDAD u ON u.id_unidad = f.unidad_id_unidad "
                    + "WHERE run_sin_dv = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, dto.getRun());
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                dto.setDv(rs.getInt("run_dv"));
                dto.setNombre(rs.getString("nom_funcionario"));
                dto.setApellidoPaterno(rs.getString("ap_paterno"));
                dto.setApellidoMaterno(rs.getString("ap_materno"));
                dto.setFechaNacimiento(rs.getDate("fec_nacimiento"));
                dto.setCorreo(rs.getString("correo"));
                dto.setDireccion(rs.getString("direc_funcionario"));
                dto.setCargo(rs.getString("cargo"));
                dto.setHabilitado(rs.getInt("habilitado"));
                if(rs.getInt("unidad_id_unidad") != 0)
                {
                    UnidadDto unidad = new UnidadDto();
                    unidad.setId(rs.getInt("unidad_id_unidad"));
                    unidad.setNombre(rs.getString("nombre_unidad"));
                    unidad.setDescripcion(rs.getString("descripcion_unidad"));
                    unidad.setDireccion(rs.getString("direccion_unidad"));
                    unidad.setHabilitado(rs.getInt("habilitado_unidad"));
                    dto.setUnidad(unidad);
                }
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("FuncionarioDaoImp.buscar Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("FuncionarioDaoImp.buscar Error: "+ex.getMessage());
        }
        return dto;
    }

    @Override
    public FuncionarioDto buscarFuncionarioParcial(int run) {
        FuncionarioDto dto = new FuncionarioDto();
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "run_dv"
                    + ",nom_funcionario"
                    + ",ap_paterno"
                    + ",ap_materno"
                    + ",fec_nacimiento"
                    + ",correo"
                    + ",direc_funcionario"
                    + ",cargo"
                    + ",habilitado "
                    + "FROM FUNCIONARIO "
                    + "WHERE run_sin_dv = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1,run);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                dto.setRun(run);
                dto.setDv(rs.getInt("run_dv"));
                dto.setNombre(rs.getString("nom_funcionario"));
                dto.setApellidoPaterno(rs.getString("ap_paterno"));
                dto.setApellidoMaterno(rs.getString("ap_materno"));
                dto.setFechaNacimiento(rs.getDate("fec_nacimiento"));
                dto.setCorreo(rs.getString("correo"));
                dto.setDireccion(rs.getString("direc_funcionario"));
                dto.setCargo(rs.getString("cargo"));
                dto.setHabilitado(rs.getInt("habilitado"));
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("FuncionarioDaoImp.buscarFuncionarioParcial Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("FuncionarioDaoImp.buscarFuncionarioParcial Error: "+ex.getMessage());
        }
        return dto;
    }

    @Override
    public LinkedList<FuncionarioDto> listarFuncionarios() {
        LinkedList<FuncionarioDto> funcionarios = new LinkedList<>();
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "f.run_sin_dv"
                    + ",f.run_dv"
                    + ",f.nom_funcionario"
                    + ",f.ap_paterno"
                    + ",f.ap_materno"
                    + ",f.fec_nacimiento"
                    + ",f.correo"
                    + ",f.direc_funcionario"
                    + ",f.cargo"
                    + ",f.habilitado"
                    + ",f.unidad_id_unidad "
                    + ",u.nombre_unidad"
                    + ",u.descripcion_unidad"
                    + ",u.direccion_unidad"
                    + ",u.habilitado as habilitado_unidad "
                    + "FROM FUNCIONARIO f "
                    + "LEFT JOIN UNIDAD u ON u.id_unidad = f.unidad_id_unidad ";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                FuncionarioDto dto = new FuncionarioDto();
                dto.setRun(rs.getInt("run_sin_dv"));
                dto.setDv(rs.getInt("run_dv"));
                dto.setNombre(rs.getString("nom_funcionario"));
                dto.setApellidoPaterno(rs.getString("ap_paterno"));
                dto.setApellidoMaterno(rs.getString("ap_materno"));
                dto.setFechaNacimiento(rs.getDate("fec_nacimiento"));
                dto.setCorreo(rs.getString("correo"));
                dto.setDireccion(rs.getString("direc_funcionario"));
                dto.setCargo(rs.getString("cargo"));
                dto.setHabilitado(rs.getInt("habilitado"));
                if(rs.getInt("unidad_id_unidad") != 0)
                {
                    UnidadDto unidad = new UnidadDto();
                    unidad.setId(rs.getInt("unidad_id_unidad"));
                    unidad.setNombre(rs.getString("nombre_unidad"));
                    unidad.setDescripcion(rs.getString("descripcion_unidad"));
                    unidad.setDireccion(rs.getString("direccion_unidad"));
                    unidad.setHabilitado(rs.getInt("habilitado_unidad"));
                    dto.setUnidad(unidad);
                }
                funcionarios.add(dto);
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("FuncionarioDaoImp.listarFuncionarios Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("FuncionarioDaoImp.listarFuncionarios Error: "+ex.getMessage());
        }
        return funcionarios;
    }

    @Override
    public LinkedList<FuncionarioDto> listarFuncionariosDeUnidad(int idUnidad) {
        
        LinkedList<FuncionarioDto> funcionarios = new LinkedList<>();
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "select "
                    + "f.run_sin_dv"
                    + ",f.run_dv "
                    + ",f.nom_funcionario "
                    + ",f.ap_paterno "
                    + ",f.ap_materno "
                    + ",f.fec_nacimiento "
                    + ",f.correo "
                    + ",f.direc_funcionario "
                    + ",f.cargo "
                    + ",f.habilitado"
                    + ",f.unidad_id_unidad "
                    + ",u.nombre_unidad"
                    + ",u.descripcion_unidad"
                    + ",u.direccion_unidad"
                    + ",u.habilitado as habilitado_unidad "
                + "from funcionario f "
                + "left join unidad u on u.id_unidad = f.unidad_id_unidad "
                + "left join unidad pa on u.unidad_padre_id_unidad = pa.id_unidad "
                + "where u.id_unidad = ? OR pa.id_unidad = ? OR pa.unidad_padre_id_unidad = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idUnidad);
            stmt.setInt(2, idUnidad);
            stmt.setInt(3, idUnidad);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                FuncionarioDto dto = new FuncionarioDto();
                dto.setRun(rs.getInt("run_sin_dv"));
                dto.setDv(rs.getInt("run_dv"));
                dto.setNombre(rs.getString("nom_funcionario"));
                dto.setApellidoPaterno(rs.getString("ap_paterno"));
                dto.setApellidoMaterno(rs.getString("ap_materno"));
                dto.setFechaNacimiento(rs.getDate("fec_nacimiento"));
                dto.setCorreo(rs.getString("correo"));
                dto.setDireccion(rs.getString("direc_funcionario"));
                dto.setCargo(rs.getString("cargo"));
                dto.setHabilitado(rs.getInt("habilitado"));
                if(rs.getInt("unidad_id_unidad") != 0)
                {
                    UnidadDto unidad = new UnidadDto();
                    unidad.setId(rs.getInt("unidad_id_unidad"));
                    unidad.setNombre(rs.getString("nombre_unidad"));
                    unidad.setDescripcion(rs.getString("descripcion_unidad"));
                    unidad.setDireccion(rs.getString("direccion_unidad"));
                    unidad.setHabilitado(rs.getInt("habilitado_unidad"));
                    dto.setUnidad(unidad);
                }
                funcionarios.add(dto);
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("FuncionarioDaoImp.listarFuncionarios Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("FuncionarioDaoImp.listarFuncionarios Error: "+ex.getMessage());
        }
        return funcionarios;
    }

    @Override
    public LinkedList<FuncionarioDto> listarFuncionariosNoJefesClaveValor() {
        LinkedList<FuncionarioDto> funcionarios = new LinkedList<>();
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "run_sin_dv"
                    + ",nom_funcionario"
                    + ",ap_paterno"
                    + ",ap_materno "
                    + "FROM funcionario f "
                    + "LEFT JOIN unidad u on u.jefe_unidad_run_sin_dv = F.run_sin_dv "
                    + "WHERE u.id_unidad IS NULL";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                FuncionarioDto dto = new FuncionarioDto();
                dto.setRun(rs.getInt("run_sin_dv"));
                dto.setNombre(rs.getString("nom_funcionario"));
                dto.setApellidoPaterno(rs.getString("ap_paterno"));
                dto.setApellidoMaterno(rs.getString("ap_materno"));
                funcionarios.add(dto);
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("FuncionarioDaoImp.listarFuncionariosNoJefesClaveValor Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("FuncionarioDaoImp.listarFuncionariosNoJefesClaveValor Error: "+ex.getMessage());
        }
        return funcionarios;
    }

    @Override
    public LinkedList<FuncionarioDto> listarFuncionariosNoJefesClaveValor(int idUnidad) {
        LinkedList<FuncionarioDto> funcionarios = new LinkedList<>();
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "run_sin_dv"
                    + ",nom_funcionario"
                    + ",ap_paterno"
                    + ",ap_materno "
                    + "FROM funcionario f "
                    + "LEFT JOIN unidad u on u.jefe_unidad_run_sin_dv = F.run_sin_dv "
                    + "WHERE u.id_unidad IS NULL OR u.id_unidad = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idUnidad);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                FuncionarioDto dto = new FuncionarioDto();
                dto.setRun(rs.getInt("run_sin_dv"));
                dto.setNombre(rs.getString("nom_funcionario"));
                dto.setApellidoPaterno(rs.getString("ap_paterno"));
                dto.setApellidoMaterno(rs.getString("ap_materno"));
                funcionarios.add(dto);
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("FuncionarioDaoImp.listarFuncionariosNoJefesClaveValor Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("FuncionarioDaoImp.listarFuncionariosNoJefesClaveValor Error: "+ex.getMessage());
        }
        return funcionarios;
    }

    @Override
    public LinkedList<FuncionarioDto> listarFuncionariosClaveValor() {
        LinkedList<FuncionarioDto> funcionarios = new LinkedList<>();
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "run_sin_dv"
                    + ",nom_funcionario"
                    + ",ap_paterno"
                    + ",ap_materno "
                    + "FROM funcionario";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                FuncionarioDto dto = new FuncionarioDto();
                dto.setRun(rs.getInt("run_sin_dv"));
                dto.setNombre(rs.getString("nom_funcionario"));
                dto.setApellidoPaterno(rs.getString("ap_paterno"));
                dto.setApellidoMaterno(rs.getString("ap_materno"));
                funcionarios.add(dto);
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("FuncionarioDaoImp.listarFuncionariosNoJefesClaveValor Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("FuncionarioDaoImp.listarFuncionariosNoJefesClaveValor Error: "+ex.getMessage());
        }
        return funcionarios;
    }
    
}
