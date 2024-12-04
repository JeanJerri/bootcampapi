package br.com.squadra.bootcampapi.repository;

import br.com.squadra.bootcampapi.model.Municipio;
import br.com.squadra.bootcampapi.model.Uf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {
    Municipio findByCodigoMunicipio(Long codigoMunicipio);

    List<Municipio> findByUf(Uf uf);

    List<Municipio> findByNome(String nome);

    List<Municipio> findByStatus(Integer status);

    List<Municipio> findByUfAndNomeAndStatus(Uf uf, String nome, Integer status);

    List<Municipio> findByUfAndNome(Uf uf, String nome);

    List<Municipio> findByUfAndStatus(Uf uf, Integer status);

    List<Municipio> findByNomeAndStatus(String nome, Integer status);
}
