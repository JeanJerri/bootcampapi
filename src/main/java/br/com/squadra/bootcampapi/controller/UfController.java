package br.com.squadra.bootcampapi.controller;

import br.com.squadra.bootcampapi.controller.dto.ErroResposta;
import br.com.squadra.bootcampapi.controller.dto.UfDTO;
import br.com.squadra.bootcampapi.controller.mapper.UfMapper;
import br.com.squadra.bootcampapi.model.Uf;
import br.com.squadra.bootcampapi.repository.UfRepository;
import br.com.squadra.bootcampapi.service.UfService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("uf")
public class UfController {

    private UfService ufService;
    private UfRepository ufRepository;

    public UfController(UfService ufService, UfRepository ufRepository) {
        this.ufService = ufService;
        this.ufRepository = ufRepository;
    }

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody UfDTO ufDto){
        try {
            System.out.println("UF recebida: " + ufDto);
            Uf uf = ufService.salvar(ufDto);
            System.out.println("UF salva: " + uf);
        } catch (IllegalArgumentException e) {
            var erroDTO = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }

        return ResponseEntity.ok(ufRepository.findAll());
    }

    @GetMapping
    public ResponseEntity<Object> buscar(
            @RequestParam(value = "codigoUF", required = false) Long codigoUF,
            @RequestParam(value = "sigla", required = false) String sigla,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "status", required = false) Integer status
    ) {
        List<Uf> ufs = new ArrayList<>();

        try {
            Uf uf = new Uf();

            uf.setCodigoUF(codigoUF);
            uf.setSigla(sigla);
            uf.setNome(nome);
            uf.setStatus(status);
            System.out.println("UF recebida: " + uf);

            return ufService.pegar(ufs, UfMapper.mapearParaUfDTO(uf));
        } catch (IllegalArgumentException e) {
            var erroDTO = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @PutMapping()
    public ResponseEntity<Object> atualizar(@RequestBody UfDTO ufDto){
        try {
            System.out.println("UF recebida: " + ufDto);
            Uf uf = ufService.alterar(ufDto);
            System.out.println("UF atualizada: " + uf);
        } catch (IllegalArgumentException e) {
            var erroDTO = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }

        return ResponseEntity.ok(ufRepository.findAll());
    }

    @DeleteMapping
    public ResponseEntity<Object> deletar(@RequestParam(value = "codigoUF", required = true) Long codigoUF){
        try {
            System.out.println("UF recebida: " + codigoUF);
            ufService.deletar(codigoUF);
        } catch (IllegalArgumentException e) {
            var erroDTO = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }

        return ResponseEntity.ok(ufRepository.findAll());
    }

}