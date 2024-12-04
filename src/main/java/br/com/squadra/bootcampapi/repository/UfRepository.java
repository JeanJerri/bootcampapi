package br.com.squadra.bootcampapi.repository;

import br.com.squadra.bootcampapi.model.Uf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UfRepository extends JpaRepository<Uf, Long> {
    Uf findByCodigoUF(Long codigoUf);

    Uf findBySigla(String sigla);

    Uf findByNome(String nome);

    List<Uf> findByStatus(Integer status);
}
