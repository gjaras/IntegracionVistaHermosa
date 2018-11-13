/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author christian
 */
public class Conexion {
    public static Connection getConexion() {
        Connection connection = null;
        try {
           String driverClassName = "oracle.jdbc.driver.OracleDriver";
           String driverUrl="jdbc:oracle:thin:@localhost:1521:XE";
           Class.forName(driverClassName);
           connection = DriverManager.getConnection(
                   driverUrl, "prueba","pass");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
