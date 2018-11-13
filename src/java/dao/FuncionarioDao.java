/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.FuncionarioDto;
import java.util.LinkedList;

/**
 *
 * @author christian
 */
public interface FuncionarioDao extends BaseDao<FuncionarioDto>{
    public FuncionarioDto buscarFuncionarioParcial(int run);
    public LinkedList<FuncionarioDto> listarFuncionarios();
    public LinkedList<FuncionarioDto> listarFuncionariosDeUnidad(int idUnidad);
    public LinkedList<FuncionarioDto> listarFuncionariosNoJefesClaveValor();
    public LinkedList<FuncionarioDto> listarFuncionariosNoJefesClaveValor(int idUnidad);
    public LinkedList<FuncionarioDto> listarFuncionariosClaveValor();
}
