package br.com.squadra.bootcampapi.controller.mapper;

import br.com.squadra.bootcampapi.controller.dto.UfDTO;
import br.com.squadra.bootcampapi.model.Uf;

public class UfMapper {

    public static UfDTO mapearParaUfDTO(Uf uf) {
        return new UfDTO(
                uf.getCodigoUF(),
                uf.getSigla(),
                uf.getNome(),
                uf.getStatus()
        );
    }
}
