/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.ReportePermisosFilaDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import sql.Conexion;

/**
 *
 * @author thor
 */
public class ReportePermisoFilaDaoImp {
    public LinkedList<ReportePermisosFilaDto> getReportePermisos(Date inicio, Date termino){
        LinkedList<ReportePermisosFilaDto> reportePermisos = new LinkedList<>();
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        String inicio_texto = df.format(inicio);
        String termino_texto = df.format(termino);
        try
        {
            
            Connection con = Conexion.getConexion();
            String sql = "select  nombre_unidad AS \"Unidad\", "
                        + "tipo_permiso AS \"Tipo de Permiso\", "
                        + "count(*) AS \"Cantidad de permisos\" "
                        + "from sol_permiso sol join funcionario fun "
                        + "on sol.solicitante_run_sin_dv = fun.run_sin_dv join unidad uni "
                        + "on fun.unidad_id_unidad = uni.id_unidad "
                        + "where   sol.fecha_solicitud >= TO_DATE('" + inicio_texto + "', 'YYYY-MM-DD')  AND "
                        + "        sol.fecha_solicitud <= TO_DATE('" + termino_texto + "', 'YYYY-MM-DD') "
                        + "group by tipo_permiso, uni.nombre_unidad "
                        + "order by nombre_unidad, tipo_permiso ";
            System.out.println(sql);
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                ReportePermisosFilaDto filaTemp = new ReportePermisosFilaDto();
                filaTemp.setUnidad(rs.getString("Unidad"));
                filaTemp.setTipo_permiso(rs.getString("Tipo de Permiso"));
                filaTemp.setCantidad(rs.getInt("Cantidad de permisos"));
                reportePermisos.add(filaTemp);
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("ReportePermisoFila.getReportePermisos Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("ReportePermisoFila.getReportePermisos Error: "+ex.getMessage());
        }
        return reportePermisos;
    }

}
