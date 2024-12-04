package br.com.squadra.bootcampapi.controller;

import br.com.squadra.bootcampapi.controller.dto.BairroDTO;
import br.com.squadra.bootcampapi.controller.dto.ErroResposta;
import br.com.squadra.bootcampapi.controller.mapper.BairroMapper;
import br.com.squadra.bootcampapi.model.Bairro;
import br.com.squadra.bootcampapi.model.Municipio;
import br.com.squadra.bootcampapi.repository.BairroRepository;
import br.com.squadra.bootcampapi.service.BairroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("bairro")
public class BairroController {

    private BairroService bairroService;
    private BairroRepository bairroRepository;

    public BairroController(BairroService bairroService, BairroRepository bairroRepository) {
        this.bairroService = bairroService;
        this.bairroRepository = bairroRepository;
    }

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody BairroDTO bairroDto){
        try {
            System.out.println("Bairro recebido: " + bairroDto);
            Bairro bairro = bairroService.salvar(bairroDto);
            System.out.println("Bairro salvo: " + bairro);
        } catch (IllegalArgumentException e) {
            var erroDTO = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }

        List<BairroDTO> bairrosDTO = bairroRepository.findAll().stream().
                map(BairroMapper::mapearParaBairro).toList();
        return ResponseEntity.ok(bairrosDTO);
    }

    @GetMapping
    public ResponseEntity<Object> buscar(
            @RequestParam(value = "codigoBairro", required = false) Long codigoBairro,
            @RequestParam(value = "codigoMunicipio", required = false) Long codigoMunicipio,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "status", required = false) Integer status
    ) {
        List<Bairro> bairros = new ArrayList<>();

        try {
            Bairro bairro = new Bairro();
            Municipio municipio = new Municipio();

            municipio.setCodigoMunicipio(codigoMunicipio);
            bairro.setCodigoBairro(codigoBairro);
            bairro.setMunicipio(municipio);
            bairro.setNome(nome);
            bairro.setStatus(status);
            System.out.println("Bairro recebido: " + municipio);

            return bairroService.pegar(bairros, BairroMapper.mapearParaBairro(bairro));
        } catch (IllegalArgumentException e) {
            var erroDTO = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @PutMapping
    public ResponseEntity<Object> atualizar(@RequestBody BairroDTO bairroDTO){
        try {
            System.out.println("Bairro recebido: " + bairroDTO);
            Bairro bairro = bairroService.alterar(bairroDTO);
            System.out.println("Bairro atualizado: " + bairro);
        } catch (IllegalArgumentException e) {
            var erroDTO = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }

        List<BairroDTO> bairrosDTO = bairroRepository.findAll().stream().
                map(BairroMapper::mapearParaBairro).toList();
        return ResponseEntity.ok(bairrosDTO);
    }
}
