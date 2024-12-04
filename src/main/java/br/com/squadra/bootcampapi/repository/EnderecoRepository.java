package br.com.squadra.bootcampapi.repository;

import br.com.squadra.bootcampapi.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    Endereco findByCodigoEndereco(Long codigoEndereco);
}
