/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.AntecedentesDto;

/**
 *
 * @author thor
 */
public interface AntecedentesDao extends BaseDao{
    public AntecedentesDto getAntecedentes(int run);
}
