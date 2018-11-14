/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.ResolucionDto;
import java.util.LinkedList;

/**
 *
 * @author christian
 */
public interface ResolucionDao {
    public LinkedList<ResolucionDto> buscarResoluciones( int mes, int anno, int idUnidad);
    public LinkedList<ResolucionDto> buscarResoluciones( int mes, int anno);
    public int validarResolucion(int idResolucion,int runResolvente);
    public int invalidarResolucion(int idResolucion,int runResolvente);
}
