package br.com.squadra.bootcampapi.controller.mapper;

import br.com.squadra.bootcampapi.controller.dto.*;
import br.com.squadra.bootcampapi.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class PessoaMapper {

    public static PessoaDTO mapearParaPessoa(Pessoa pessoa) {
        return new PessoaDTO(
                pessoa.getCodigoPessoa(),
                pessoa.getNome(),
                pessoa.getSobrenome(),
                pessoa.getIdade(),
                pessoa.getLogin(),
                pessoa.getSenha(),
                pessoa.getStatus(),
                List.of()
        );
    }


    public static PessoaDTO toDTO(Pessoa pessoa) {
        List<EnderecoDTO> enderecos = pessoa.getEnderecos().stream()
                .map(PessoaMapper::toDTO)
                .collect(Collectors.toList());

        return new PessoaDTO(
                pessoa.getCodigoPessoa(),
                pessoa.getNome(),
                pessoa.getSobrenome(),
                pessoa.getIdade(),
                pessoa.getLogin(),
                pessoa.getSenha(),
                pessoa.getStatus(),
                enderecos
        );
    }

    public static EnderecoDTO toDTO(Endereco endereco) {
        return new EnderecoDTO(
                endereco.getCodigoEndereco(),
                endereco.getPessoa().getCodigoPessoa(),
                endereco.getBairro().getCodigoBairro(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getCep(),
                toDTO(endereco.getBairro())
        );
    }

    public static BairroPessoaDTO toDTO(Bairro bairro) {
        return new BairroPessoaDTO(
                bairro.getCodigoBairro(),
                bairro.getMunicipio().getCodigoMunicipio(),
                bairro.getNome(),
                bairro.getStatus(),
                toDTO(bairro.getMunicipio())
        );
    }

    private static MunicipioPessoaDTO toDTO(Municipio municipio) {
        return new MunicipioPessoaDTO(
                municipio.getCodigoMunicipio(),
                municipio.getUf().getCodigoUF(),
                municipio.getNome(),
                municipio.getStatus(),
                toDTO(municipio.getUf())
        );
    }

    private static UfDTO toDTO(Uf uf) {
        return new UfDTO(
                uf.getCodigoUF(),
                uf.getSigla(),
                uf.getNome(),
                uf.getStatus()
        );
    }

}
