package br.com.squadra.bootcampapi.service;

import br.com.squadra.bootcampapi.controller.dto.PessoaDTO;
import br.com.squadra.bootcampapi.controller.mapper.PessoaMapper;
import br.com.squadra.bootcampapi.model.Bairro;
import br.com.squadra.bootcampapi.model.Endereco;
import br.com.squadra.bootcampapi.model.Pessoa;
import br.com.squadra.bootcampapi.repository.BairroRepository;
import br.com.squadra.bootcampapi.repository.EnderecoRepository;
import br.com.squadra.bootcampapi.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private BairroRepository bairroRepository;

    public Pessoa salvar(PessoaDTO pessoaDTO) {

        //Validações da presença dos campos
        if (pessoaDTO.nome() == null || pessoaDTO.nome().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível salvar a Pessoa no banco de dados. Motivo: o campo nome é obrigatório.");
        }
        if (pessoaDTO.sobrenome() == null || pessoaDTO.sobrenome().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível salvar a Pessoa no banco de dados. Motivo: o campo sobrenome é obrigatório.");
        }
        if (pessoaDTO.idade() == null) {
            throw new IllegalArgumentException("Não foi possível salvar a Pessoa no banco de dados. Motivo: o campo idade é obrigatório.");
        }
        if (pessoaDTO.login() == null || pessoaDTO.login().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível salvar a Pessoa no banco de dados. Motivo: o campo login é obrigatório.");
        }
        if (pessoaDTO.senha() == null || pessoaDTO.senha().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível salvar a Pessoa no banco de dados. Motivo: o campo senha é obrigatório.");
        }
        if (pessoaDTO.status() == null) {
            throw new IllegalArgumentException("Não foi possível salvar a Pessoa no banco de dados. Motivo: o campo status é obrigatório.");
        }
        if (pessoaDTO.enderecos() == null || pessoaDTO.enderecos().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível salvar a Pessoa no banco de dados. Motivo: o campo enderecos é obrigatório, informe pelo menos 1 endereço.");
        }


        //Validações dos valores dos campos
        if (!(pessoaDTO.nome().matches("^[A-ZÀ-Ÿ ]+$")) || pessoaDTO.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo nome deve conter apenas letras em maiúsculo.");
        }
        if (!(pessoaDTO.sobrenome().matches("^[A-ZÀ-Ÿ ]+$")) || pessoaDTO.sobrenome().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo sobrenome deve conter apenas letras em maiúsculo.");
        }
        if (pessoaDTO.idade() < 0) {
            throw new IllegalArgumentException("O campo idade deve ser um número inteiro não negativo.");
        }
        if (pessoaDTO.login().length() < 8 || pessoaDTO.login().length() > 50 || !pessoaDTO.login().matches("^[a-z0-9\\-_*@.]+$")) {
            throw new IllegalArgumentException("O campo login deve conter entre 8 e 50 caracteres em letras minúsculas e sem acentos. " +
                    "Só são permitidos letras, números e os caracteres (.-_*@) e sem espaço.");
        }
        if (pessoaDTO.senha().length() < 4 || pessoaDTO.senha().length() > 50 || !pessoaDTO.senha().matches("^[a-z0-9]+$")) {
            throw new IllegalArgumentException("O campo senha deve conter entre 4 e 50 caracteres em letras minúsculas e sem acentos. " +
                    "Só são permitidos letras e números sem espaço.");
        }
        if (pessoaDTO.status() != 1 && pessoaDTO.status() != 2) {
            throw new IllegalArgumentException("O campo status deve ser informado apenas os número 1 (ativado) ou 2 (desativado) .");
        }


        //Validações de duplicidade
        if (pessoaRepository.findByLogin(pessoaDTO.login()) != null) {
            throw new IllegalArgumentException("Não foi possível salvar a Pessoa no banco de dados. Motivo: o login já existe em outro registro cadastrado no banco de dados.");
        }
        if (pessoaRepository.findByNomeAndSobrenome(pessoaDTO.nome(), pessoaDTO.sobrenome()) != null) {
            throw new IllegalArgumentException("Não foi possível salvar a Pessoa no banco de dados. Motivo: o nome e sobrenome já existem em um outro registro cadastrado no banco de dados.");
        }


        Pessoa pessoa = new Pessoa();
        pessoa.setNome(pessoaDTO.nome());
        pessoa.setSobrenome(pessoaDTO.sobrenome());
        pessoa.setIdade(pessoaDTO.idade());
        pessoa.setLogin(pessoaDTO.login());
        pessoa.setSenha(pessoaDTO.senha());
        pessoa.setStatus(pessoaDTO.status());
        pessoa.setEnderecos(
                pessoaDTO.enderecos().stream().map(
                        enderecoDTO -> {

                            //Validações da presença dos campos
                            if (enderecoDTO.codigoBairro() == null) {
                                throw new IllegalArgumentException("Não foi possível salvar o Endereço no banco de dados. Motivo: o campo codigoBairro no endereço é obrigatório.");
                            }
                            if (enderecoDTO.nomeRua() == null || enderecoDTO.nomeRua().isEmpty()) {
                                throw new IllegalArgumentException("Não foi possível salvar o Endereço no banco de dados. Motivo: o campo nomeRua no endereço é obrigatório.");
                            }
                            if (enderecoDTO.numero() == null || enderecoDTO.numero().isEmpty()) {
                                throw new IllegalArgumentException("Não foi possível salvar o Endereço no banco de dados. Motivo: o campo numero no endereço é obrigatório.");
                            }
                            if (enderecoDTO.complemento() == null || enderecoDTO.complemento().isEmpty()) {
                                throw new IllegalArgumentException("Não foi possível salvar o Endereço no banco de dados. Motivo: o campo complemento no endereço é obrigatório.");
                            }
                            if (enderecoDTO.cep() == null || enderecoDTO.cep().isEmpty()) {
                                throw new IllegalArgumentException("Não foi possível salvar o Endereço no banco de dados. Motivo: o campo cep no endereço é obrigatório.");
                            }


                            //Validações dos valores dos campos
                            if (!(String.valueOf(enderecoDTO.codigoBairro()).matches("^[0-9]+$"))) {
                                throw new IllegalArgumentException("O campo codigoBairro deve conter apenas números inteiros positivos.");
                            }
                            if (!(enderecoDTO.nomeRua().matches("^[A-ZÀ-Ÿ0-9 ]+$")) || enderecoDTO.nomeRua().trim().isEmpty()) {
                                throw new IllegalArgumentException("O campo nomeRua deve conter apenas letras em maiúsculo e números.");
                            }
                            if (!(enderecoDTO.numero().matches("^[A-Z0-9]+$")) || enderecoDTO.numero().trim().isEmpty()) {
                                throw new IllegalArgumentException("O campo numero deve conter apenas números e letras em maiúsculo sem acento.");
                            }
                            if (!(enderecoDTO.complemento().matches("^[A-ZÀ-Ÿ ]+$")) || enderecoDTO.complemento().trim().isEmpty()) {
                                throw new IllegalArgumentException("O campo complemento deve conter apenas letras em maiúsculo.");
                            }
                            if (!enderecoDTO.cep().matches("^\\d{5}-\\d{3}$")) {
                                throw new IllegalArgumentException("O campo CEP deve estar no formato '00000-000'.");
                            }


                            //Validações de existência
                            Bairro bairro = bairroRepository.findByCodigoBairro(enderecoDTO.codigoBairro());
                            if (bairro == null) {
                                throw new IllegalArgumentException("Não foi possível salvar o Endereço no banco de dados. Motivo: o bairro não está cadastrado.");
                            }


                            Endereco endereco = new Endereco();
                            endereco.setLogradouro(enderecoDTO.nomeRua());
                            endereco.setNumero(enderecoDTO.numero());
                            endereco.setComplemento(enderecoDTO.complemento());
                            endereco.setCep(enderecoDTO.cep());
                            endereco.setPessoa(pessoa);
                            endereco.setBairro(bairro);

                            return endereco;
                        }
                ).collect(Collectors.toList())
        );

        return pessoaRepository.save(pessoa);
    }

    @Transactional
    public Pessoa alterar(PessoaDTO pessoaDTO) {

        //Validações da presença dos campos
        if (pessoaDTO.codigoPessoa() == null) {
            throw new IllegalArgumentException("Não foi possível alterar o Pessoa no banco de dados. Motivo: o campo codigoPessoa é obrigatório.");
        }
        if (pessoaDTO.nome() == null || pessoaDTO.nome().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível alterar a Pessoa no banco de dados. Motivo: o campo nome é obrigatório.");
        }
        if (pessoaDTO.sobrenome() == null || pessoaDTO.sobrenome().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível alterar a Pessoa no banco de dados. Motivo: o campo sobrenome é obrigatório.");
        }
        if (pessoaDTO.idade() == null) {
            throw new IllegalArgumentException("Não foi possível alterar a Pessoa no banco de dados. Motivo: o campo idade é obrigatório.");
        }
        if (pessoaDTO.login() == null || pessoaDTO.login().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível alterar a Pessoa no banco de dados. Motivo: o campo login é obrigatório.");
        }
        if (pessoaDTO.senha() == null || pessoaDTO.senha().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível alterar a Pessoa no banco de dados. Motivo: o campo senha é obrigatório.");
        }
        if (pessoaDTO.status() == null) {
            throw new IllegalArgumentException("Não foi possível alterar a Pessoa no banco de dados. Motivo: o campo status é obrigatório.");
        }
        if (pessoaDTO.enderecos() == null || pessoaDTO.enderecos().isEmpty()) {
            throw new IllegalArgumentException("Não foi possível salvar a Pessoa no banco de dados. Motivo: o campo enderecos é obrigatório, informe pelo menos 1 endereço.");
        }

        //Validações dos valores dos campos
        if (!(String.valueOf(pessoaDTO.codigoPessoa()).matches("^[0-9]+$"))) {
            throw new IllegalArgumentException("O campo codigoPessoa deve conter apenas números inteiros positivos.");
        }
        if (!(pessoaDTO.nome().matches("^[A-ZÀ-Ÿ ]+$")) || pessoaDTO.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo nome deve conter apenas letras em maiúsculo.");
        }
        if (!(pessoaDTO.sobrenome().matches("^[A-ZÀ-Ÿ ]+$")) || pessoaDTO.sobrenome().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo sobrenome deve conter apenas letras em maiúsculo.");
        }
        if (pessoaDTO.idade() < 0) {
            throw new IllegalArgumentException("O campo idade deve ser um número inteiro não negativo.");
        }
        if (pessoaDTO.login().length() < 8 || pessoaDTO.login().length() > 50 || !pessoaDTO.login().matches("^[a-z0-9\\-_*@.]+$")) {
            throw new IllegalArgumentException("O campo login deve conter entre 8 e 50 caracteres em letras minúsculas e sem acentos. " +
                    "Só são permitidos letras, números e os caracteres (.-_*@) e sem espaço.");
        }
        if (pessoaDTO.senha().length() < 4 || pessoaDTO.senha().length() > 50 || !pessoaDTO.senha().matches("^[a-z0-9]+$")) {
            throw new IllegalArgumentException("O campo senha deve conter entre 4 e 50 caracteres em letras minúsculas e sem acentos. " +
                    "Só são permitidos letras e números sem espaço.");
        }
        if (pessoaDTO.status() != 1 && pessoaDTO.status() != 2) {
            throw new IllegalArgumentException("O campo status deve ser informado apenas os número 1 (ativado) ou 2 (desativado) .");
        }


        //Validações de existência
        Pessoa pessoaAntiga = pessoaRepository.findByCodigoPessoa(pessoaDTO.codigoPessoa());
        if (pessoaAntiga == null) {
            throw new IllegalArgumentException("Não foi possível alterar a Pessoa no banco de dados. Motivo: a Pessoa não está cadastrada.");
        }


        //Validações de duplicidade
        Pessoa pessoaRepetida = pessoaRepository.findByLogin(pessoaDTO.login());
        if (pessoaRepetida != null && !pessoaRepetida.getCodigoPessoa().equals(pessoaDTO.codigoPessoa())) {
            throw new IllegalArgumentException("Não foi possível alterar a Pessoa no banco de dados. Motivo: o login já existe em outro registro cadastrado no banco de dados.");
        }
        pessoaRepetida = pessoaRepository.findByNomeAndSobrenome(pessoaDTO.nome(), pessoaDTO.sobrenome());
        if (pessoaRepetida != null && !pessoaRepetida.getCodigoPessoa().equals(pessoaDTO.codigoPessoa())) {
            throw new IllegalArgumentException("Não foi possível alterar a Pessoa no banco de dados. Motivo: o nome e sobrenome já existem em um outro registro cadastrado no banco de dados.");
        }

//        //Precisa verificar se os endereços também são iguais
//        //Validação de status ativado/desativado
//        if (
//                pessoaDTO.nome().equals(pessoaAntiga.getNome()) &&
//                pessoaDTO.sobrenome().equals(pessoaAntiga.getSobrenome()) &&
//                pessoaDTO.idade().equals(pessoaAntiga.getIdade()) &&
//                pessoaDTO.login().equals(pessoaAntiga.getLogin()) &&
//                pessoaDTO.senha().equals(pessoaAntiga.getSenha()) &&
//                pessoaDTO.status().equals(pessoaAntiga.getStatus())
//        ) {
//            if (pessoaDTO.status() == 1) {
//                throw new IllegalArgumentException("Não foi possível alterar o status do Bairro no banco de dados, pois o Bairro já está ativo.");
//            } else {
//                throw new IllegalArgumentException("Não foi possível alterar o status do Bairro no banco de dados, pois o Bairro já está inativo.");
//            }
//        }


        pessoaAntiga.setNome(pessoaDTO.nome());
        pessoaAntiga.setSobrenome(pessoaDTO.sobrenome());
        pessoaAntiga.setIdade(pessoaDTO.idade());
        pessoaAntiga.setLogin(pessoaDTO.login());
        pessoaAntiga.setSenha(pessoaDTO.senha());
        pessoaAntiga.setStatus(pessoaDTO.status());


        List<Endereco> enderecosNovosEAtualizados = pessoaDTO.enderecos().stream().map(
                enderecoDTO -> {

                    //Validações da presença dos campos
                    //O campo de endereço não é obrigatório devido à regra de negócio
                    if (enderecoDTO.codigoPessoa() == null) {
                        throw new IllegalArgumentException("Não foi possível alterar o Endereço no banco de dados. Motivo: o campo codigoPessoa no endereço é obrigatório.");
                    }
                    if (enderecoDTO.codigoBairro() == null) {
                        throw new IllegalArgumentException("Não foi possível alterar o Endereço no banco de dados. Motivo: o campo codigoBairro no endereço é obrigatório.");
                    }
                    if (enderecoDTO.nomeRua() == null || enderecoDTO.nomeRua().isEmpty()) {
                        throw new IllegalArgumentException("Não foi possível alterar o Endereço no banco de dados. Motivo: o campo nomeRua no endereço é obrigatório.");
                    }
                    if (enderecoDTO.numero() == null || enderecoDTO.numero().isEmpty()) {
                        throw new IllegalArgumentException("Não foi possível alterar o Endereço no banco de dados. Motivo: o campo numero no endereço é obrigatório.");
                    }
                    if (enderecoDTO.complemento() == null || enderecoDTO.complemento().isEmpty()) {
                        throw new IllegalArgumentException("Não foi possível alterar o Endereço no banco de dados. Motivo: o campo complemento no endereço é obrigatório.");
                    }
                    if (enderecoDTO.cep() == null || enderecoDTO.cep().isEmpty()) {
                        throw new IllegalArgumentException("Não foi possível alterar o Endereço no banco de dados. Motivo: o campo cep no endereço é obrigatório.");
                    }


                    //Validações dos valores dos campos
                    if (enderecoDTO.codigoEndereco() != null && !(String.valueOf(enderecoDTO.codigoEndereco()).matches("^[0-9]+$"))) {
                        throw new IllegalArgumentException("O campo codigoEndereco deve conter apenas números inteiros positivos.");
                    }
                    if (!(String.valueOf(enderecoDTO.codigoPessoa()).matches("^[0-9]+$"))) {
                        throw new IllegalArgumentException("O campo codigoPessoa deve conter apenas números inteiros positivos.");
                    }
                    if (!(String.valueOf(enderecoDTO.codigoBairro()).matches("^[0-9]+$"))) {
                        throw new IllegalArgumentException("O campo codigoBairro deve conter apenas números inteiros positivos.");
                    }
                    if (!(enderecoDTO.nomeRua().matches("^[A-ZÀ-Ÿ0-9 ]+$")) || enderecoDTO.nomeRua().trim().isEmpty()) {
                        throw new IllegalArgumentException("O campo nomeRua deve conter apenas letras em maiúsculo e números.");
                    }
                    if (!(enderecoDTO.numero().matches("^[A-Z0-9]+$")) || enderecoDTO.numero().trim().isEmpty()) {
                        throw new IllegalArgumentException("O campo numero deve conter apenas números e letras em maiúsculo sem acento.");
                    }
                    if (!(enderecoDTO.complemento().matches("^[A-ZÀ-Ÿ ]+$")) || enderecoDTO.complemento().trim().isEmpty()) {
                        throw new IllegalArgumentException("O campo complemento deve conter apenas letras em maiúsculo.");
                    }
                    if (!enderecoDTO.cep().matches("^\\d{5}-\\d{3}$")) {
                        throw new IllegalArgumentException("O campo CEP deve estar no formato '00000-000'.");
                    }


                    //Validações de existência
                    if (enderecoDTO.codigoEndereco() != null) {
                        Endereco endereco = enderecoRepository.findByCodigoEndereco(enderecoDTO.codigoEndereco());
                        if (endereco == null) {
                            throw new IllegalArgumentException("Não foi possível alterar o Endereço no banco de dados. Motivo: o endereço não está cadastrado.");
                        }
                    }
                    if (!(enderecoDTO.codigoPessoa().equals(pessoaDTO.codigoPessoa()))) {
                        throw new IllegalArgumentException("Não foi possível alterar o Endereço no banco de dados. Motivo: o codigoPessoa no endereço não corresponde ao codigoPessoa informado em pessoa.");
                    }
                    Bairro bairro = bairroRepository.findByCodigoBairro(enderecoDTO.codigoBairro());
                    if (bairro == null) {
                        throw new IllegalArgumentException("Não foi possível alterar o Endereço no banco de dados. Motivo: o bairro não está cadastrado.");
                    }


                    //Testar se é possível ter duplicidade de endereços
                    //Validações de duplicidade

                    Endereco endereco = enderecoDTO.codigoEndereco() != null
                            ? enderecoRepository.findByCodigoEndereco(enderecoDTO.codigoEndereco())
                            : new Endereco();
        //            O código acima é o mesmo que o código abaixo:
        //            Endereco endereco = new Endereco();
        //            if (enderecoDTO.codigoEndereco() != null) {
        //                enderecoRepository.findByCodigoEndereco(enderecoDTO.codigoEndereco());
        //            }

                    endereco.setLogradouro(enderecoDTO.nomeRua());
                    endereco.setNumero(enderecoDTO.numero());
                    endereco.setComplemento(enderecoDTO.complemento());
                    endereco.setCep(enderecoDTO.cep());
                    endereco.setPessoa(pessoaAntiga);
                    endereco.setBairro(bairro);

                    return endereco;
                }).toList();

        List<Endereco> enderecosNoBanco = pessoaAntiga.getEnderecos(); //pega apenas a referência e as demais alteraçõe feitas nessa lista refletem na pessoaAntiga
        List<Endereco> enderecosAtualizados = enderecosNovosEAtualizados.stream().filter(
                endereco -> endereco.getCodigoEndereco() != null
        ).toList();

        enderecosNoBanco.removeIf(
            enderecoNoBanco -> {
                boolean enderecoPresente = enderecosAtualizados.stream().anyMatch(
                    enderecoAtualizado ->
                        enderecoAtualizado.getCodigoEndereco().equals(enderecoNoBanco.getCodigoEndereco())
                );

                if (!enderecoPresente) {
                    enderecoRepository.delete(enderecoNoBanco);
                }

                return !enderecoPresente;
            }
        );

        enderecosNoBanco.clear();
        enderecosNoBanco.addAll(enderecosNovosEAtualizados);

        return pessoaRepository.save(pessoaAntiga);
    }

    public ResponseEntity<Object> pegar(List<Pessoa> pessoas, PessoaDTO pessoaDTO) {

        Pessoa resultado = null;


        //Sem parâmetros
        if (pessoaDTO.status() == null && pessoaDTO.codigoPessoa() == null && pessoaDTO.login() == null) {
            return ResponseEntity.ok(pessoaRepository.findAll().stream().map(PessoaMapper::mapearParaPessoa).toList());
        }


        //Validações dos valores dos campos
        if (pessoaDTO.codigoPessoa() != null && !(String.valueOf(pessoaDTO.codigoPessoa()).matches("^[0-9]+$"))) {
            throw new IllegalArgumentException("O campo codigoPessoa deve conter apenas números inteiros positivos.");
        }
        if (pessoaDTO.login() != null && !pessoaDTO.login().matches("^[a-z0-9\\-_*@.]+$")) {
            throw new IllegalArgumentException("O campo login deve conter apenas letras minúsculas sem acentos, números e os caracteres (.-_*@) e sem espaço.");
        }
        if (pessoaDTO.status() != null && pessoaDTO.status() != 1 && pessoaDTO.status() != 2) {
            throw new IllegalArgumentException("O campo status deve ser informado apenas os número 1 (ativado) ou 2 (desativado) .");
        }


        //Apenas 1 parâmetro
        if (pessoaDTO.codigoPessoa() != null) {
            resultado = pessoaRepository.findByCodigoPessoa(pessoaDTO.codigoPessoa());
        }
        if (pessoaDTO.login() != null && pessoaDTO.codigoPessoa() == null && pessoaDTO.status() == null) {
            Pessoa pessoa = pessoaRepository.findByLogin(pessoaDTO.login());
            if (pessoa != null) {
                pessoas.add(pessoa);
            }
            return ResponseEntity.ok(pessoas.stream().map(PessoaMapper::mapearParaPessoa).toList());
        }
        if (pessoaDTO.status() != null && pessoaDTO.codigoPessoa() == null && pessoaDTO.login() == null) {
            pessoas = pessoaRepository.findByStatus(pessoaDTO.status());
            return ResponseEntity.ok(pessoas.stream().map(PessoaMapper::mapearParaPessoa).toList());
        }


        //Parâmetros combinados
        if (pessoaDTO.status() != null && pessoaDTO.login() != null && pessoaDTO.codigoPessoa() == null) {
            pessoas = pessoaRepository.findByLoginAndStatus(pessoaDTO.login(), pessoaDTO.status());
            return ResponseEntity.ok(pessoas.stream().map(PessoaMapper::mapearParaPessoa).toList());
        }


        // Verificação se os campos estão conforme o UF encontrado para pesquisas com mais de 1 parâmetro
        if (resultado == null) {
            return ResponseEntity.ok(pessoas);
        } else {
            if (pessoaDTO.codigoPessoa() != null && !pessoaDTO.codigoPessoa().equals(resultado.getCodigoPessoa())) {
                return ResponseEntity.ok(pessoas);
            }
            if (pessoaDTO.login() != null && !pessoaDTO.login().equals(resultado.getLogin())) {
                return ResponseEntity.ok(pessoas);
            }
            if (pessoaDTO.status() != null && !pessoaDTO.status().equals(resultado.getStatus())) {
                return ResponseEntity.ok(pessoas);
            }
        }

        return ResponseEntity.ok(PessoaMapper.toDTO(resultado));
    }
}
