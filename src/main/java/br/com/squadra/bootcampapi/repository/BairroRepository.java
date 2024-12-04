package br.com.squadra.bootcampapi.repository;

import br.com.squadra.bootcampapi.model.Bairro;
import br.com.squadra.bootcampapi.model.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BairroRepository extends JpaRepository<Bairro, Long> {

    Bairro findByCodigoBairro(Long codigoBairro);

    List<Bairro> findByMunicipio(Municipio municipio);

    List<Bairro> findByNome(String nome);

    List<Bairro> findByStatus(Integer status);

    List<Bairro> findByMunicipioAndNomeAndStatus(Municipio municipio, String nome, Integer status);

    List<Bairro> findByMunicipioAndNome(Municipio municipio, String nome);

    List<Bairro> findByMunicipioAndStatus(Municipio municipio, Integer status);

    List<Bairro> findByNomeAndStatus(String nome, Integer status);
}
