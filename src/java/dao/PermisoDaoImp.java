/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.FuncionarioDto;
import dto.PermisoDto;
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
public class PermisoDaoImp implements PermisoDao{

    @Override
    public LinkedList<PermisoDto> buscarPermisos(int run) {
        LinkedList<PermisoDto> permisos = new LinkedList<>();
        try
        {
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
            while(rs.next())
            {
                PermisoDto dto = new PermisoDto();
                dto.setId(rs.getInt("id_permiso"));
                dto.setTipo(rs.getString("tipo_permiso"));
                dto.setEstado(rs.getInt("estado"));
                if(rs.wasNull())
                    dto.setEstado(2);
                dto.setFechaInicio(rs.getDate("fecha_inicio"));
                dto.setFechaTermino(rs.getDate("fecha_termino"));
                dto.setFechaSolicitud(rs.getDate("fecha_solicitud"));
                dto.setDescripcion(rs.getString("desc_permiso"));
                if(rs.getInt("solicitante_run_sin_dv") != 0)
                {
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
                if(rs.getInt("autorizante_run_sin_dv") != 0)
                {
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
        }
        catch(SQLException sqlex)
        {
            System.out.println("PermisoDaoImp.BuscarPermisos Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("PermisoDaoImp.BuscarPermisos Error: "+ex.getMessage());
        }
        return permisos;
    }
    
    public LinkedList<PermisoDto>listPermisos() {
        LinkedList<PermisoDto> permisos = new LinkedList<>();
        try
        {
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
            while(rs.next())
            {
                PermisoDto dto = new PermisoDto();
                dto.setId(rs.getInt("id_permiso"));
                dto.setTipo(rs.getString("tipo_permiso"));
                dto.setEstado(rs.getInt("estado"));
                if(rs.wasNull())
                    dto.setEstado(2);
                dto.setFechaInicio(rs.getDate("fecha_inicio"));
                dto.setFechaTermino(rs.getDate("fecha_termino"));
                dto.setFechaSolicitud(rs.getDate("fecha_solicitud"));
                dto.setDescripcion(rs.getString("desc_permiso"));
                if(rs.getInt("solicitante_run_sin_dv") != 0)
                {
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
                if(rs.getInt("autorizante_run_sin_dv") != 0)
                {
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
        }
        catch(SQLException sqlex)
        {
            System.out.println("PermisoDaoImp.BuscarPermisos Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("PermisoDaoImp.BuscarPermisos Error: "+ex.getMessage());
        }
        return permisos;
    }
}
