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
import java.util.ArrayList;
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
                                        "count(*) AS \"Cantidad de permisos\" " +
                            "from sol_permiso sol join funcionario fun " +
                            "on sol.solicitante_run_sin_dv = fun.run_sin_dv " +
                            "where fun.run_sin_dv = " + run + " " +
                            "group by tipo_permiso, sol.estado " +
                            "order by tipo_permiso";
            System.out.println(sql);
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                System.out.println("entro");
                List<Object> filaTemp = new ArrayList<Object>();
                filaTemp.add(rs.getString("estado"));
                filaTemp.add(rs.getString("Tipo de Permiso"));
                filaTemp.add(rs.getInt("Cantidad de permisos"));
                filasTemp.add(filaTemp);
            }
            Antecedentes.setFilas(filasTemp);
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
