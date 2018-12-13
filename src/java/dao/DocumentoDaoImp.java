/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.DocumentoDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import sql.Conexion;


public class DocumentoDaoImp implements DocumentoDao {

    @Override
    public LinkedList<DocumentoDto> listarDocumentosByPermiso(int id_permiso) {
        LinkedList<DocumentoDto> documentos = new LinkedList<>();
        System.out.println(id_permiso);
        try
        {
            Connection con = Conexion.getConexion();
            String sql ="select * " +
                        "from documento " +
                        "where PERMISO_ID_PERMISO = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id_permiso);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                System.out.println("entro");
                DocumentoDto documentoTemp = new DocumentoDto();
                documentoTemp.setId_documento(rs.getInt("id_documento"));
                documentoTemp.setNombre_documento(rs.getString("nombre_documento"));
                documentoTemp.setFormato_documento(rs.getString("formato_documento"));
                documentoTemp.setDir(rs.getString("directorio_documento"));
                documentoTemp.setFecha_creacion(rs.getDate("fec_creacion"));
                documentoTemp.setId_permiso(rs.getInt("permiso_id_permiso")); 
                documentos.add(documentoTemp);
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("Documento.listarDocumentosByPermiso Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("Documento.listarDocumentosByPermiso Error: "+ex.getMessage());
        }
        return documentos;
    }
    
    @Override
    public int insertar(DocumentoDto dto) {
        return 0;
    }

    @Override
    public DocumentoDto buscar(DocumentoDto ddto) {
        DocumentoDto documento = new DocumentoDto();
        try
        {
            int id_documento = ddto.getId_documento();
            Connection con = Conexion.getConexion();
            String sql = "select * "
                        + "from documento "
                        + "where id_documento = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id_documento);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                documento.setId_documento(id_documento);
                documento.setNombre_documento(rs.getString("nombre_documento"));
                documento.setFormato_documento(rs.getString("formato_documento"));
                documento.setDir(rs.getString("directorio_documento"));
                documento.setFecha_creacion(rs.getDate("fec_creacion"));
                documento.setId_permiso(rs.getInt("permiso_id_permiso"));  
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("Documento.buscar Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("Documento.buscar Error: "+ex.getMessage());
        }
        return documento;
    }
    
    public DocumentoDto buscar(int id) {
        DocumentoDto documento = new DocumentoDto();
        try
        {
            int id_documento = id;
            Connection con = Conexion.getConexion();
            String sql = "select * "
                        + "from documento "
                        + "where id_documento = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id_documento);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                documento.setId_documento(id_documento);
                documento.setNombre_documento(rs.getString("nombre_documento"));
                documento.setFormato_documento(rs.getString("formato_documento"));
                documento.setDir(rs.getString("directorio_documento"));
                documento.setFecha_creacion(rs.getDate("fec_creacion"));
                documento.setId_permiso(rs.getInt("permiso_id_permiso"));  
            }
        }
        catch(SQLException sqlex)
        {
            System.out.println("Documento.buscar Error sql: "+sqlex.getMessage());
        }
        catch(Exception ex)
        {
            System.out.println("Documento.buscar Error: "+ex.getMessage());
        }
        return documento;
    }

    @Override
    public int modificar(DocumentoDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int eliminar(DocumentoDto dto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
