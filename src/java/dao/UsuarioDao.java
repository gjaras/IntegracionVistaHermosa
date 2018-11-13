/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.UsuarioDto;
import java.util.LinkedList;

/**
 *
 * @author christian
 */
public interface UsuarioDao extends BaseDao<UsuarioDto>{
    public LinkedList<UsuarioDto> listarUsuarios();
    public boolean usuarioExiste(String nombre);
    public UsuarioDto autenticarUsuario(String nombre, String clave);
}
