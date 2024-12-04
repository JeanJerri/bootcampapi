package br.com.squadra.bootcampapi.repository;

import br.com.squadra.bootcampapi.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    Pessoa findByCodigoPessoa(Long codigoPessoa);

    Pessoa findByLogin(String login);

    List<Pessoa> findByStatus(Integer status);

    Pessoa findByNomeAndSobrenome(String nome, String sobrenome);

    List<Pessoa> findByLoginAndStatus(String nome, Integer status);
}
