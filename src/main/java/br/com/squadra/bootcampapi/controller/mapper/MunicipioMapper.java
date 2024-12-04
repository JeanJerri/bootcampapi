package br.com.squadra.bootcampapi.controller.mapper;

import br.com.squadra.bootcampapi.controller.dto.MunicipioDTO;
import br.com.squadra.bootcampapi.model.Municipio;

public class MunicipioMapper {

    public static MunicipioDTO mapearParaMunicipio(Municipio municipio) {
        return new MunicipioDTO(
                municipio.getCodigoMunicipio(),
                municipio.getUf().getCodigoUF(),
                municipio.getNome(),
                municipio.getStatus()
        );
    }
}
