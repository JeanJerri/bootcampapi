package br.com.squadra.bootcampapi.controller.mapper;

import br.com.squadra.bootcampapi.controller.dto.BairroDTO;
import br.com.squadra.bootcampapi.model.Bairro;

public class BairroMapper {

    public static BairroDTO mapearParaBairro(Bairro bairro) {
        return new BairroDTO(
                bairro.getCodigoBairro(),
                bairro.getMunicipio().getCodigoMunicipio(),
                bairro.getNome(),
                bairro.getStatus()
        );
    }
}
