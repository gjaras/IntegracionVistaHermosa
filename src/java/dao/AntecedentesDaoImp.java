/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.AntecedentesDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import sql.Conexion;

/**
 *
 * @author thor
 */
public class AntecedentesDaoImp implements AntecedentesDao {
    
    
    public AntecedentesDto getAntecedentes(int run){
        AntecedentesDto Antecedentes = new AntecedentesDto();
        List<List<Object>> filasTemp = new ArrayList<List<Object>>();
        try
        {
            Connection con = Conexion.getConexion();
            String sql = "select  sol.estado, " +
                                        "tipo_permiso AS \"Tipo de Permiso\", " +
                                        "count(*) AS \"Cantidad de permisos\", " +
                                        "EXTRACT(YEAR from sol.fecha_inicio) as periodo " +
                            "from sol_permiso sol join funcionario fun " +
                            "on sol.solicitante_run_sin_dv = fun.run_sin_dv " +
                            "where fun.run_sin_dv = " + run + " " +
                            "group by tipo_permiso, sol.estado, EXTRACT(YEAR from sol.fecha_inicio) " +
                            "order by periodo desc, tipo_permiso asc";
            System.out.println(sql);
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                List<Object> filaTemp = new ArrayList<Object>();
                filaTemp.add(rs.getString("estado"));
                filaTemp.add(rs.getString("Tipo de Permiso"));
                filaTemp.add(rs.getInt("Cantidad de permisos"));
                filaTemp.add(rs.getInt("periodo"));
                filasTemp.add(filaTemp);
            }
            Antecedentes.setFilas(filasTemp);
            
            //Calculo de antiguedad
            int años_de_servicio = 1; //por ahora la antiguedad para todos sera 1
            
            //Se asignara el periodo en el cual el usuario pide las solicitudes
            //dia inicial
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, 0); // 11 = december
            cal.set(Calendar.DAY_OF_YEAR, 1);    
            Date start = cal.getTime();
            //ultimo dia
            cal.set(Calendar.MONTH, 11); // 11 = december
            cal.set(Calendar.DAY_OF_MONTH, 31);
            Date end = cal.getTime();  
            String fecha_inicio_actual = new SimpleDateFormat("yyyy/MM/dd").format(start);
            String fecha_termino_actual = new SimpleDateFormat("yyyy/MM/dd").format(end);
            //Ahora se buscara la informacion detallada sobre permisos restantes
            sql =   "select  sol.tipo_permiso, " +
                    "        count(sol.tipo_permiso) as cantidad " +
                    "from funcionario fun join sol_permiso sol " +
                    "ON fun.run_sin_dv = sol.solicitante_run_sin_dv " +
                    "where fun.run_sin_dv = 2 " +
                    "AND sol.fecha_inicio BETWEEN TO_DATE ('"+ fecha_inicio_actual +"', 'yyyy/mm/dd') " +
                    "AND TO_DATE ('" + fecha_termino_actual + "', 'yyyy/mm/dd') " +
                    "group by sol.tipo_permiso";
            System.out.println(sql);
            PreparedStatement stmt2 = con.prepareStatement(sql);
            ResultSet rs2 = stmt2.executeQuery();
            int feriados_anuales_solicitados =0;
            int administrativos_solicitados =0;
            //Aqui se obtendran los valores
            while(rs2.next())
            {
                System.out.println(rs2.getString("tipo_permiso"));
                System.out.println(""+rs2.getInt("cantidad"));
                if(rs2.getString("tipo_permiso").equals("Feriado Legal")){
                    feriados_anuales_solicitados = rs2.getInt("cantidad");
                }else if (rs2.getString("tipo_permiso").equals("Administrativo")){
                    administrativos_solicitados  = rs2.getInt("cantidad");
                    System.out.println(""+administrativos_solicitados);
                }
            }
            /*  CALCULO PARA FERIADOS ANUALES DISPONIBLES
                15 días hábiles para funcionarios con antigüedad entre 1 y 15 años de servicio.
                20 días hábiles para funcionarios con quince o más años de servicios y menos de veinte.
                25 días hábiles para funcionarios con veinte o más años de servicio.
            */
            if(años_de_servicio < 1)
                Antecedentes.setFeriados_anuales_restantes(0);
            else if(años_de_servicio < 15)
                Antecedentes.setFeriados_anuales_restantes(15-feriados_anuales_solicitados);
            else if (años_de_servicio < 20)
                Antecedentes.setFeriados_anuales_restantes(20-feriados_anuales_solicitados);
            else if (años_de_servicio >= 20)    
                Antecedentes.setFeriados_anuales_restantes(25-feriados_anuales_solicitados);
            
            Antecedentes.setPermisos_administrativos_restantes(6-administrativos_solicitados);
        }
        catch(SQLException sqlex)
        {
            System.out.println("Antecedentes.getAntecedentes Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("Antecedentes.getAntecedentes Error: "+ex.getMessage());
        }
        return Antecedentes;
    }

    @Override
    public int insertar(Object dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int modificar(Object dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int eliminar(Object dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object buscar(Object dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
