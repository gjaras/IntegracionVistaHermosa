/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.PermisoDto;
import java.util.LinkedList;

/**
 *
 * @author christian
 */
public interface PermisoDao extends BaseDao<PermisoDto>{
    public LinkedList<PermisoDto> buscarPermisos(int run);
    public LinkedList<PermisoDto> buscarPermisosAnuales();
    public PermisoDto buscarPermiso(int id);

}
