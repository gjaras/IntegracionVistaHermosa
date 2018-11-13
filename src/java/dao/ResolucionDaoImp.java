/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.FuncionarioDto;
import dto.PermisoDto;
import dto.ResolucionDto;
import dto.UnidadDto;
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
public class ResolucionDaoImp implements ResolucionDao{

    @Override
    public LinkedList<ResolucionDto> buscarResoluciones(int mes, int anno, int idUnidad) {
        LinkedList<ResolucionDto> resoluciones = new LinkedList<>();
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "r.id_resolucion"
                    + ",r.fec_resolucion"
                    + ",r.estado_resolucion"
                    + ",r.permiso_id_permiso"
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
                    + ",u.id_unidad"
                    + ",u.nombre_unidad"
                    + ",u.descripcion_unidad"
                    + ",u.direccion_unidad"
                    + ",u.habilitado as habilitado_unidad"
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
                    + ",r.resolvente_run_sin_dv "
                    + ",rvt.run_dv as run_dv_resolvente"
                    + ",rvt.nom_funcionario as nom_resolvente"
                    + ",rvt.ap_paterno as ap_paterno_resolvente"
                    + ",rvt.ap_materno as ap_materno_resolvente"
                    + ",rvt.fec_nacimiento as fec_nacimiento_resolvente"
                    + ",rvt.correo as correo_resolvente"
                    + ",rvt.direc_funcionario as direc_resolvente"
                    + ",rvt.cargo as cargo_resolvente"
                    + ",rvt.habilitado as habilitado_resolvente "
                    + "FROM resolucion r " 
                    + "JOIN sol_permiso p on r.permiso_id_permiso = p.id_permiso " 
                    + "JOIN funcionario s ON s.run_sin_dv = p.solicitante_run_sin_dv " 
                    + "JOIN funcionario a ON a.run_sin_dv = p.autorizante_run_sin_dv " 
                    + "LEFT JOIN funcionario rvt ON rvt.run_sin_dv = r.resolvente_run_sin_dv " 
                    + "JOIN unidad u on u.id_unidad = s.unidad_id_unidad " 
                    + "LEFT JOIN unidad pa on pa.id_unidad = u.unidad_padre_id_unidad " 
                    + "WHERE extract(MONTH FROM p.fecha_termino) = (? - 1) AND extract(YEAR FROM p.fecha_termino) = ? AND (s.unidad_id_unidad = ? OR u.unidad_padre_id_unidad = ? OR pa.unidad_padre_id_unidad = ?) "
                    + "ORDER BY u.id_unidad";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, mes);
            stmt.setInt(2, anno);
            stmt.setInt(3, idUnidad);
            stmt.setInt(4, idUnidad);
            stmt.setInt(5, idUnidad);
            ResultSet rs = stmt.executeQuery();
            UnidadDto unidadActual = new UnidadDto();
            while(rs.next())
            {
                ResolucionDto dto = new ResolucionDto();
                dto.setId(rs.getInt("id_resolucion"));
                dto.setFechaResolucion(rs.getDate("fec_resolucion"));
                dto.setEstado(rs.getInt("estado_resolucion"));
                if(rs.wasNull())
                {
                        dto.setEstado(2);
                }
                if(rs.getInt("permiso_id_permiso") != 0)
                {
                    PermisoDto permiso = new PermisoDto();
                    permiso.setId(rs.getInt("permiso_id_permiso"));
                    permiso.setTipo(rs.getString("tipo_permiso"));
                    permiso.setEstado(rs.getInt("estado"));
                    permiso.setFechaInicio(rs.getDate("fecha_inicio"));
                    permiso.setFechaTermino(rs.getDate("fecha_termino"));
                    permiso.setFechaSolicitud(rs.getDate("fecha_solicitud"));
                    permiso.setDescripcion(rs.getString("desc_permiso"));
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
                        if(rs.getInt("id_unidad") != 0)
                        {
                            if(unidadActual.getId() !=  rs.getInt("id_unidad")) // Para economizar la creacion de unidades repetidas, se asigna al solicitante a la misma unidad si ya ha sido creada
                            {
                                UnidadDto unidad = new UnidadDto();
                                unidad.setId(rs.getInt("id_unidad"));
                                unidad.setNombre(rs.getString("nombre_unidad"));
                                unidad.setDescripcion(rs.getString("descripcion_unidad"));
                                unidad.setDireccion(rs.getString("direccion_unidad"));
                                unidad.setHabilitado(rs.getInt("habilitado_unidad"));
                                unidadActual = unidad;
                                solicitante.setUnidad(unidad);
                            }
                            else
                            {
                                solicitante.setUnidad(unidadActual);
                            }
                        }
                        permiso.setSolicitante(solicitante);
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
                        permiso.setAutorizante(autorizante);

                    }
                    dto.setPermiso(permiso);
                }
                if(rs.getInt("resolvente_run_sin_dv") != 0)
                {
                    FuncionarioDto resolvente = new FuncionarioDto();
                    resolvente.setRun(rs.getInt("resolvente_run_sin_dv"));
                    resolvente.setDv(rs.getInt("run_dv_resolvente"));
                    resolvente.setNombre(rs.getString("nom_resolvente"));
                    resolvente.setApellidoPaterno(rs.getString("ap_paterno_resolvente"));
                    resolvente.setApellidoMaterno(rs.getString("ap_materno_resolvente"));
                    resolvente.setFechaNacimiento(rs.getDate("fec_nacimiento_resolvente"));
                    resolvente.setCorreo(rs.getString("correo_resolvente"));
                    resolvente.setDireccion(rs.getString("direc_resolvente"));
                    resolvente.setCargo(rs.getString("cargo_resolvente"));
                    resolvente.setHabilitado(rs.getInt("habilitado_resolvente"));
                    dto.setResolvente(resolvente);
                }
                resoluciones.add(dto);
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("ResolucionDaoImp.buscarResolucioness Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("ResolucionDaoImp.buscarResolucioness Error: "+ex.getMessage());
        }
        return resoluciones;
    }

    @Override
    public LinkedList<ResolucionDto> buscarResoluciones(int mes, int anno) {
        LinkedList<ResolucionDto> resoluciones = new LinkedList<>();
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "SELECT "
                    + "r.id_resolucion"
                    + ",r.fec_resolucion"
                    + ",r.estado_resolucion"
                    + ",r.permiso_id_permiso"
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
                    + ",u.id_unidad"
                    + ",u.nombre_unidad"
                    + ",u.descripcion_unidad"
                    + ",u.direccion_unidad"
                    + ",u.habilitado as habilitado_unidad"
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
                    + ",r.resolvente_run_sin_dv "
                    + ",rvt.run_dv as run_dv_resolvente"
                    + ",rvt.nom_funcionario as nom_resolvente"
                    + ",rvt.ap_paterno as ap_paterno_resolvente"
                    + ",rvt.ap_materno as ap_materno_resolvente"
                    + ",rvt.fec_nacimiento as fec_nacimiento_resolvente"
                    + ",rvt.correo as correo_resolvente"
                    + ",rvt.direc_funcionario as direc_resolvente"
                    + ",rvt.cargo as cargo_resolvente"
                    + ",rvt.habilitado as habilitado_resolvente "
                    + "FROM resolucion r " 
                    + "JOIN sol_permiso p on r.permiso_id_permiso = p.id_permiso " 
                    + "JOIN funcionario s ON s.run_sin_dv = p.solicitante_run_sin_dv " 
                    + "JOIN funcionario a ON a.run_sin_dv = p.autorizante_run_sin_dv " 
                    + "LEFT JOIN funcionario rvt ON rvt.run_sin_dv = r.resolvente_run_sin_dv " 
                    + "JOIN unidad u on u.id_unidad = s.unidad_id_unidad " 
                    + "LEFT JOIN unidad pa on pa.id_unidad = u.unidad_padre_id_unidad " 
                    + "WHERE extract(MONTH FROM p.fecha_termino) = (? - 1) AND extract(YEAR FROM p.fecha_termino) = ? "
                    + "ORDER BY u.id_unidad";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, mes);
            stmt.setInt(2, anno);
            ResultSet rs = stmt.executeQuery();
            
            UnidadDto unidadActual = new UnidadDto();
            while(rs.next())
            {
                ResolucionDto dto = new ResolucionDto();
                dto.setId(rs.getInt("id_resolucion"));
                dto.setFechaResolucion(rs.getDate("fec_resolucion"));
                dto.setEstado(rs.getInt("estado_resolucion"));
                if(rs.wasNull())
                {
                        dto.setEstado(2);
                }
                if(rs.getInt("permiso_id_permiso") != 0)
                {
                    PermisoDto permiso = new PermisoDto();
                    permiso.setId(rs.getInt("permiso_id_permiso"));
                    permiso.setTipo(rs.getString("tipo_permiso"));
                    permiso.setEstado(rs.getInt("estado"));
                    permiso.setFechaInicio(rs.getDate("fecha_inicio"));
                    permiso.setFechaTermino(rs.getDate("fecha_termino"));
                    permiso.setFechaSolicitud(rs.getDate("fecha_solicitud"));
                    permiso.setDescripcion(rs.getString("desc_permiso"));
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
                        if(rs.getInt("id_unidad") != 0)
                        {
                            if(unidadActual.getId() !=  rs.getInt("id_unidad")) // Para economizar la creacion de unidades repetidas, se asigna al solicitante a la misma unidad si ya ha sido creada
                            {
                                UnidadDto unidad = new UnidadDto();
                                unidad.setId(rs.getInt("id_unidad"));
                                unidad.setNombre(rs.getString("nombre_unidad"));
                                unidad.setDescripcion(rs.getString("descripcion_unidad"));
                                unidad.setDireccion(rs.getString("direccion_unidad"));
                                unidad.setHabilitado(rs.getInt("habilitado_unidad"));
                                unidadActual = unidad;
                                solicitante.setUnidad(unidad);
                            }
                            else
                            {
                                solicitante.setUnidad(unidadActual);
                            }
                        }
                        permiso.setSolicitante(solicitante);
                        
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
                        permiso.setAutorizante(autorizante);

                    }
                    dto.setPermiso(permiso);
                }
                if(rs.getInt("resolvente_run_sin_dv") != 0)
                {
                    FuncionarioDto resolvente = new FuncionarioDto();
                    resolvente.setRun(rs.getInt("resolvente_run_sin_dv"));
                    resolvente.setDv(rs.getInt("run_dv_resolvente"));
                    resolvente.setNombre(rs.getString("nom_resolvente"));
                    resolvente.setApellidoPaterno(rs.getString("ap_paterno_resolvente"));
                    resolvente.setApellidoMaterno(rs.getString("ap_materno_resolvente"));
                    resolvente.setFechaNacimiento(rs.getDate("fec_nacimiento_resolvente"));
                    resolvente.setCorreo(rs.getString("correo_resolvente"));
                    resolvente.setDireccion(rs.getString("direc_resolvente"));
                    resolvente.setCargo(rs.getString("cargo_resolvente"));
                    resolvente.setHabilitado(rs.getInt("habilitado_resolvente"));
                    dto.setResolvente(resolvente);
                }
                resoluciones.add(dto);
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("ResolucionDaoImp.buscarResolucioness Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("ResolucionDaoImp.buscarResolucioness Error: "+ex.getMessage());
        }
        return resoluciones;
    }
    
}
