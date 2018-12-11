/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import dao.PermisoDaoImp;
import dao.ResolucionDaoImp;
import dao.UnidadDaoImp;
import dto.PermisoDto;
import dto.ResolucionDto;
import dto.UnidadDto;
import java.util.LinkedList;

/**
 *
 * @author christian
 */
public class test {
    public static void main(String[] args){
        /*
        LinkedList<ResolucionDto> resoluciones;
        resoluciones = new ResolucionDaoImp().buscarResoluciones(10, 2018);
        for (ResolucionDto resolucion : resoluciones) {
            System.out.println("resultado: "+resolucion.toString());
        }
        */
        UnidadDto dto = new UnidadDto();
        dto.setId(51);
        dto.setNombre("real dummy");
        dto.setDireccion("somewhere asdf");
        dto.setDescripcion("asdf");
        UnidadDto padre = new UnidadDto();
        padre.setId(1);
        dto.setPadre(padre);
        int asdf = new UnidadDaoImp().modificar(dto);
        System.out.println(asdf);
        //PermisoDto dto = new PermisoDaoImp().buscarPermiso(2);
        //System.out.println("res: "+dto.toString());
    }
}
