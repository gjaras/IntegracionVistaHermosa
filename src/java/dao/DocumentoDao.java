/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.DocumentoDto;
import java.util.LinkedList;

/**
 *
 * @author thor
 */
public interface DocumentoDao extends BaseDao<DocumentoDto>{
    public LinkedList<DocumentoDto> listarDocumentosByPermiso(int id_permiso);
}
