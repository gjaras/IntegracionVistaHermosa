/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.UnidadDto;
import java.util.LinkedList;

/**
 *
 * @author christian
 */
public interface UnidadDao extends BaseDao<UnidadDto>{
    public LinkedList<UnidadDto> listarUnidades();
    public LinkedList<UnidadDto> listadoUnidadClaveValor();
    public LinkedList<UnidadDto> listadoUnidadesHijasClaveValor(int id);
    public UnidadDto buscarPorIdParcial(int id);
    public UnidadDto buscarPorJefe(int id);
    public boolean nombreUnidadExiste(String nombre);
}
