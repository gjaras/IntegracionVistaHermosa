/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.FuncionarioDto;
import dto.PermisoDto;
import dto.UnidadDto;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import sql.Conexion;

/**
 *
 * @author christian
 */
public class PermisoDaoImp implements PermisoDao {

    @Override
    public int insertar(PermisoDto pdto) {
        try {
            Connection con = Conexion.getConexion();
            String sqlString;
            sqlString = "{call PR_CREAR_PERMISO(?,?,?,?,?,?)}";
            CallableStatement proc = con.prepareCall(sqlString);
            proc.registerOutParameter(1, java.sql.Types.INTEGER);
            proc.setString(2, pdto.getTipo());
            proc.setDate(3, new java.sql.Date(pdto.getFechaInicio().getTime()));
            proc.setDate(4, new java.sql.Date(pdto.getFechaTermino().getTime()));
            proc.setString(5, pdto.getDescripcion());
            proc.setInt(6, pdto.getSolicitante().getRun());
            proc.executeQuery();
            int result = proc.getInt(1);
            if(result == -1){
                return result;
            }else{
                return buscar(pdto).getId();
            }
        } catch (SQLException sqle) {
            System.out.println("UsuarioDaoImp.insertar Error SQL con el procedure PR_CREAR_USUARIO: " + sqle.getMessage());
            //return sqle.getErrorCode();
            return -1;
        } catch (Exception e) {
            System.out.println("UsuarioDaoImp.insertar Error: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public LinkedList<PermisoDto> buscarPermisos(int run) {
        LinkedList<PermisoDto> permisos = new LinkedList<>();
        try {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "p.id_permiso"
                    + ",p.tipo_permiso"
                    + ",p.estado"
                    + ",p.fecha_inicio"
                    + ",p.fecha_termino"
                    + ",p.fecha_solicitud"
                    + ",p.desc_permiso"
                    + ",p.solicitante_run_sin_dv"
                    + ",s.run_dv as run_dv_solicitante"
                    + ",s.nom_funcionario as nom_solicitante"
                    + ",s.ap_paterno as ap_paterno_solicitante"
                    + ",s.ap_materno as ap_materno_solicitante"
                    + ",s.fec_nacimiento as fec_nacimiento_solicitante"
                    + ",s.correo as correo_solicitante"
                    + ",s.direc_funcionario as direc_solicitante"
                    + ",s.cargo as cargo_solicitante"
                    + ",s.habilitado as habilitado_solicitante"
                    + ",p.autorizante_run_sin_dv "
                    + ",a.run_dv as run_dv_autorizante"
                    + ",a.nom_funcionario as nom_autorizante"
                    + ",a.ap_paterno as ap_paterno_autorizante"
                    + ",a.ap_materno as ap_materno_autorizante"
                    + ",a.fec_nacimiento as fec_nacimiento_autorizante"
                    + ",a.correo as correo_autorizante"
                    + ",a.direc_funcionario as direc_autorizante"
                    + ",a.cargo as cargo_autorizante"
                    + ",a.habilitado as habilitado_autorizante "
                    + "FROM sol_permiso p "
                    + "LEFT JOIN FUNCIONARIO s ON s.run_sin_dv = p.solicitante_run_sin_dv "
                    + "LEFT JOIN FUNCIONARIO a ON a.run_sin_dv = p.autorizante_run_sin_dv "
                    + "WHERE solicitante_run_sin_dv = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, run);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PermisoDto dto = new PermisoDto();
                dto.setId(rs.getInt("id_permiso"));
                dto.setTipo(rs.getString("tipo_permiso"));
                dto.setEstado(rs.getInt("estado"));
                if (rs.wasNull()) {
                    dto.setEstado(2);
                }
                dto.setFechaInicio(rs.getDate("fecha_inicio"));
                dto.setFechaTermino(rs.getDate("fecha_termino"));
                dto.setFechaSolicitud(rs.getDate("fecha_solicitud"));
                dto.setDescripcion(rs.getString("desc_permiso"));
                if (rs.getInt("solicitante_run_sin_dv") != 0) {
                    FuncionarioDto solicitante = new FuncionarioDto();
                    solicitante.setRun(rs.getInt("solicitante_run_sin_dv"));
                    solicitante.setDv(rs.getInt("run_dv_solicitante"));
                    solicitante.setNombre(rs.getString("nom_solicitante"));
                    solicitante.setApellidoPaterno(rs.getString("ap_paterno_solicitante"));
                    solicitante.setApellidoMaterno(rs.getString("ap_materno_solicitante"));
                    solicitante.setFechaNacimiento(rs.getDate("fec_nacimiento_solicitante"));
                    solicitante.setCorreo(rs.getString("correo_solicitante"));
                    solicitante.setDireccion(rs.getString("direc_solicitante"));
                    solicitante.setCargo(rs.getString("cargo_solicitante"));
                    solicitante.setHabilitado(rs.getInt("habilitado_solicitante"));
                    dto.setSolicitante(solicitante);
                }
                if (rs.getInt("autorizante_run_sin_dv") != 0) {
                    FuncionarioDto autorizante = new FuncionarioDto();
                    autorizante.setRun(rs.getInt("autorizante_run_sin_dv"));
                    autorizante.setDv(rs.getInt("run_dv_autorizante"));
                    autorizante.setNombre(rs.getString("nom_autorizante"));
                    autorizante.setApellidoPaterno(rs.getString("ap_paterno_autorizante"));
                    autorizante.setApellidoMaterno(rs.getString("ap_materno_autorizante"));
                    autorizante.setFechaNacimiento(rs.getDate("fec_nacimiento_autorizante"));
                    autorizante.setCorreo(rs.getString("correo_autorizante"));
                    autorizante.setDireccion(rs.getString("direc_autorizante"));
                    autorizante.setCargo(rs.getString("cargo_autorizante"));
                    autorizante.setHabilitado(rs.getInt("habilitado_autorizante"));
                    dto.setAutorizante(autorizante);

                }
                permisos.add(dto);
            }
        } catch (SQLException sqlex) {
            System.out.println("PermisoDaoImp.BuscarPermisos Error sql: " + sqlex.getMessage());
        } catch (Exception ex) {
            System.out.println("PermisoDaoImp.BuscarPermisos Error: " + ex.getMessage());
        }
        return permisos;
    }

    public LinkedList<PermisoDto> listPermisos() {
        LinkedList<PermisoDto> permisos = new LinkedList<>();
        try {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "p.id_permiso"
                    + ",p.tipo_permiso"
                    + ",p.estado"
                    + ",p.fecha_inicio"
                    + ",p.fecha_termino"
                    + ",p.fecha_solicitud"
                    + ",p.desc_permiso"
                    + ",p.solicitante_run_sin_dv"
                    + ",s.run_dv as run_dv_solicitante"
                    + ",s.nom_funcionario as nom_solicitante"
                    + ",s.ap_paterno as ap_paterno_solicitante"
                    + ",s.ap_materno as ap_materno_solicitante"
                    + ",s.fec_nacimiento as fec_nacimiento_solicitante"
                    + ",s.correo as correo_solicitante"
                    + ",s.direc_funcionario as direc_solicitante"
                    + ",s.cargo as cargo_solicitante"
                    + ",s.habilitado as habilitado_solicitante"
                    + ",p.autorizante_run_sin_dv "
                    + ",a.run_dv as run_dv_autorizante"
                    + ",a.nom_funcionario as nom_autorizante"
                    + ",a.ap_paterno as ap_paterno_autorizante"
                    + ",a.ap_materno as ap_materno_autorizante"
                    + ",a.fec_nacimiento as fec_nacimiento_autorizante"
                    + ",a.correo as correo_autorizante"
                    + ",a.direc_funcionario as direc_autorizante"
                    + ",a.cargo as cargo_autorizante"
                    + ",a.habilitado as habilitado_autorizante "
                    + "FROM sol_permiso p "
                    + "LEFT JOIN FUNCIONARIO s ON s.run_sin_dv = p.solicitante_run_sin_dv "
                    + "LEFT JOIN FUNCIONARIO a ON a.run_sin_dv = p.autorizante_run_sin_dv ";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PermisoDto dto = new PermisoDto();
                dto.setId(rs.getInt("id_permiso"));
                dto.setTipo(rs.getString("tipo_permiso"));
                dto.setEstado(rs.getInt("estado"));
                if (rs.wasNull()) {
                    dto.setEstado(2);
                }
                dto.setFechaInicio(rs.getDate("fecha_inicio"));
                dto.setFechaTermino(rs.getDate("fecha_termino"));
                dto.setFechaSolicitud(rs.getDate("fecha_solicitud"));
                dto.setDescripcion(rs.getString("desc_permiso"));
                if (rs.getInt("solicitante_run_sin_dv") != 0) {
                    FuncionarioDto solicitante = new FuncionarioDto();
                    solicitante.setRun(rs.getInt("solicitante_run_sin_dv"));
                    solicitante.setDv(rs.getInt("run_dv_solicitante"));
                    solicitante.setNombre(rs.getString("nom_solicitante"));
                    solicitante.setApellidoPaterno(rs.getString("ap_paterno_solicitante"));
                    solicitante.setApellidoMaterno(rs.getString("ap_materno_solicitante"));
                    solicitante.setFechaNacimiento(rs.getDate("fec_nacimiento_solicitante"));
                    solicitante.setCorreo(rs.getString("correo_solicitante"));
                    solicitante.setDireccion(rs.getString("direc_solicitante"));
                    solicitante.setCargo(rs.getString("cargo_solicitante"));
                    solicitante.setHabilitado(rs.getInt("habilitado_solicitante"));
                    dto.setSolicitante(solicitante);
                }
                if (rs.getInt("autorizante_run_sin_dv") != 0) {
                    FuncionarioDto autorizante = new FuncionarioDto();
                    autorizante.setRun(rs.getInt("autorizante_run_sin_dv"));
                    autorizante.setDv(rs.getInt("run_dv_autorizante"));
                    autorizante.setNombre(rs.getString("nom_autorizante"));
                    autorizante.setApellidoPaterno(rs.getString("ap_paterno_autorizante"));
                    autorizante.setApellidoMaterno(rs.getString("ap_materno_autorizante"));
                    autorizante.setFechaNacimiento(rs.getDate("fec_nacimiento_autorizante"));
                    autorizante.setCorreo(rs.getString("correo_autorizante"));
                    autorizante.setDireccion(rs.getString("direc_autorizante"));
                    autorizante.setCargo(rs.getString("cargo_autorizante"));
                    autorizante.setHabilitado(rs.getInt("habilitado_autorizante"));
                    dto.setAutorizante(autorizante);

                }
                permisos.add(dto);
            }
        } catch (SQLException sqlex) {
            System.out.println("PermisoDaoImp.BuscarPermisos Error sql: " + sqlex.getMessage());
        } catch (Exception ex) {
            System.out.println("PermisoDaoImp.BuscarPermisos Error: " + ex.getMessage());
        }
        return permisos;
    }

    @Override
    public LinkedList<PermisoDto> buscarPermisosAnuales() {
        LinkedList<PermisoDto> permisos = new LinkedList<>();
        try {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "p.id_permiso"
                    + ",p.tipo_permiso"
                    + ",p.estado"
                    + ",p.fecha_inicio"
                    + ",p.fecha_termino"
                    + ",p.fecha_solicitud"
                    + ",p.desc_permiso"
                    + ",p.solicitante_run_sin_dv"
                    + ",s.run_dv as run_dv_solicitante"
                    + ",s.nom_funcionario as nom_solicitante"
                    + ",s.ap_paterno as ap_paterno_solicitante"
                    + ",s.ap_materno as ap_materno_solicitante"
                    + ",s.fec_nacimiento as fec_nacimiento_solicitante"
                    + ",s.correo as correo_solicitante"
                    + ",s.direc_funcionario as direc_solicitante"
                    + ",s.cargo as cargo_solicitante"
                    + ",s.habilitado as habilitado_solicitante"
                    + ",s.unidad_id_unidad"
                    + ",u.nombre_unidad"
                    + ",u.descripcion_unidad"
                    + ",u.direccion_unidad"
                    + ",u.habilitado as habilitado_unidad "
                    + "FROM sol_permiso p "
                    + "LEFT JOIN FUNCIONARIO s ON s.run_sin_dv = p.solicitante_run_sin_dv "
                    + "LEFT JOIN UNIDAD u ON s.unidad_id_unidad = u.id_unidad "
                    + "WHERE p.fecha_termino BETWEEN ? AND ? ";
            PreparedStatement stmt = con.prepareStatement(sql);
            Calendar cal = Calendar.getInstance();
            int anno = cal.get(Calendar.YEAR);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            stmt.setDate(1, new java.sql.Date(sdf.parse((anno - 1) + "-09-01").getTime()));
            stmt.setDate(2, new java.sql.Date(sdf.parse(anno + "-08-31").getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PermisoDto dto = new PermisoDto();
                dto.setId(rs.getInt("id_permiso"));
                dto.setTipo(rs.getString("tipo_permiso"));
                dto.setEstado(rs.getInt("estado"));
                if (rs.wasNull()) {
                    dto.setEstado(2);
                }
                dto.setFechaInicio(rs.getDate("fecha_inicio"));
                dto.setFechaTermino(rs.getDate("fecha_termino"));
                dto.setFechaSolicitud(rs.getDate("fecha_solicitud"));
                dto.setDescripcion(rs.getString("desc_permiso"));
                if (rs.getInt("solicitante_run_sin_dv") != 0) {
                    FuncionarioDto solicitante = new FuncionarioDto();
                    solicitante.setRun(rs.getInt("solicitante_run_sin_dv"));
                    solicitante.setDv(rs.getInt("run_dv_solicitante"));
                    solicitante.setNombre(rs.getString("nom_solicitante"));
                    solicitante.setApellidoPaterno(rs.getString("ap_paterno_solicitante"));
                    solicitante.setApellidoMaterno(rs.getString("ap_materno_solicitante"));
                    solicitante.setFechaNacimiento(rs.getDate("fec_nacimiento_solicitante"));
                    solicitante.setCorreo(rs.getString("correo_solicitante"));
                    solicitante.setDireccion(rs.getString("direc_solicitante"));
                    solicitante.setCargo(rs.getString("cargo_solicitante"));
                    solicitante.setHabilitado(rs.getInt("habilitado_solicitante"));
                    if (rs.getInt("unidad_id_unidad") != 0) {
                        UnidadDto unidad = new UnidadDto();
                        unidad.setId(rs.getInt("unidad_id_unidad"));
                        unidad.setNombre(rs.getString("nombre_unidad"));
                        unidad.setDescripcion(rs.getString("descripcion_unidad"));
                        unidad.setDireccion(rs.getString("direccion_unidad"));
                        unidad.setHabilitado(rs.getInt("habilitado_unidad"));
                        solicitante.setUnidad(unidad);
                    }
                    dto.setSolicitante(solicitante);
                }
                permisos.add(dto);
            }
        } catch (SQLException sqlex) {
            System.out.println("PermisoDaoImp.BuscarPermisos Error sql: " + sqlex.getMessage());
        } catch (Exception ex) {
            System.out.println("PermisoDaoImp.BuscarPermisos Error: " + ex.getMessage());
        }
        return permisos;
    }

    @Override
    public int modificar(PermisoDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int eliminar(PermisoDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PermisoDto buscar(PermisoDto pdto) {
        try {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "p.id_permiso "
                    + "FROM SOL_PERMISO p "
                    + "WHERE p.tipo_permiso = ? "
                    + "AND p.fecha_inicio = ? "
                    + "AND p.fecha_termino = ? "
                    + "AND p.desc_permiso = ? "
                    + "AND p.solicitante_run_sin_dv = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, pdto.getTipo());
            stmt.setDate(2, new java.sql.Date(pdto.getFechaInicio().getTime()));
            stmt.setDate(3, new java.sql.Date(pdto.getFechaTermino().getTime()));
            stmt.setString(4, pdto.getDescripcion());
            stmt.setInt(5, pdto.getSolicitante().getRun());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PermisoDto retorno = new PermisoDto();
                retorno.setId(rs.getInt("id_permiso"));
                return retorno;
            }
        } catch (SQLException sqlex) {
            System.out.println("UsuarioDaoImp.buscar Error sql: " + sqlex.getMessage());
        } catch (Exception ex) {
            System.out.println("UsuarioDaoImp.buscar Error: " + ex.getMessage());
        }
        return null;
    }
}
