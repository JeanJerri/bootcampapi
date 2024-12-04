package br.com.squadra.bootcampapi.controller;

import br.com.squadra.bootcampapi.controller.dto.ErroResposta;
import br.com.squadra.bootcampapi.controller.dto.PessoaDTO;
import br.com.squadra.bootcampapi.controller.mapper.PessoaMapper;
import br.com.squadra.bootcampapi.model.Pessoa;
import br.com.squadra.bootcampapi.repository.PessoaRepository;
import br.com.squadra.bootcampapi.service.PessoaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("pessoa")
public class PessoaController {

    private PessoaService pessoaService;
    private PessoaRepository pessoaRepository;

    public PessoaController(PessoaService pessoaService, PessoaRepository pessoaRepository) {
        this.pessoaService = pessoaService;
        this.pessoaRepository = pessoaRepository;
    }

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody PessoaDTO pessoaDto){

        try {
            System.out.println("Pessoa recebida: " + pessoaDto);
            Pessoa pessoa = pessoaService.salvar(pessoaDto);
            System.out.println("Pessoa salva: " + pessoa);
        } catch (IllegalArgumentException e) {
            var erroDTO = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }

        List<PessoaDTO> pessoasDTO = pessoaRepository.findAll().stream().
                map(PessoaMapper::mapearParaPessoa).toList();
        return ResponseEntity.ok(pessoasDTO);
    }

    @GetMapping
    public ResponseEntity<Object> buscar(
            @RequestParam(value = "codigoPessoa", required = false) Long codigoPessoa,
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "status", required = false) Integer status
    ) {
        List<Pessoa> pessoas = new ArrayList<>();

        try {
            Pessoa pessoa = new Pessoa();

            pessoa.setCodigoPessoa(codigoPessoa);
            pessoa.setLogin(login);
            pessoa.setStatus(status);
            System.out.println("Pessoa recebida: " + pessoa);

            return pessoaService.pegar(pessoas, PessoaMapper.mapearParaPessoa(pessoa));
        } catch (IllegalArgumentException e) {
            var erroDTO = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @PutMapping
    public ResponseEntity<Object> atualizar(@RequestBody PessoaDTO pessoaDto) {
        try {
            System.out.println("Pessoa recebida: " + pessoaDto);
            Pessoa pessoa = pessoaService.alterar(pessoaDto);
            System.out.println("Pessoa atualizada: " + pessoa);
        } catch (IllegalArgumentException e) {
            var erroDTO = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }

        List<PessoaDTO> pessoasDTO = pessoaRepository.findAll().stream().
                map(PessoaMapper::mapearParaPessoa).toList();
        return ResponseEntity.ok(pessoasDTO);
    }

}
