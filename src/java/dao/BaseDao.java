/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author christian
 */
public interface BaseDao<T> {
    int insertar(T dto);
    int modificar(T dto);
    int eliminar(T dto);
    T buscar(T dto);
}
