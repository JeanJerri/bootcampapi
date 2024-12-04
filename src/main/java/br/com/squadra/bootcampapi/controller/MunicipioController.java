package br.com.squadra.bootcampapi.controller;

import br.com.squadra.bootcampapi.controller.dto.ErroResposta;
import br.com.squadra.bootcampapi.controller.dto.MunicipioDTO;
import br.com.squadra.bootcampapi.controller.mapper.MunicipioMapper;
import br.com.squadra.bootcampapi.model.Municipio;
import br.com.squadra.bootcampapi.model.Uf;
import br.com.squadra.bootcampapi.repository.MunicipioRepository;
import br.com.squadra.bootcampapi.service.MunicipioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("municipio")
public class MunicipioController {

    private MunicipioService municipioService;
    private MunicipioRepository municipioRepository;

    public MunicipioController(MunicipioService municipioService, MunicipioRepository municipioRepository) {
        this.municipioService = municipioService;
        this.municipioRepository = municipioRepository;
    }

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody MunicipioDTO municipioDto){
        try {
            System.out.println("Municipio salvo: " + municipioDto);
            Municipio municipio = municipioService.salvar(municipioDto);
            System.out.println("Municipio recebido: " + municipio);
        } catch (IllegalArgumentException e) {
            var erroDTO = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }

        List<MunicipioDTO> municipiosDTO = municipioRepository.findAll().stream().
                map(MunicipioMapper::mapearParaMunicipio).toList();

        return ResponseEntity.ok(municipiosDTO);
    }

    @GetMapping
    public ResponseEntity<Object> buscar(
            @RequestParam(value = "codigoMunicipio", required = false) Long codigoMunicipio,
            @RequestParam(value = "codigoUF", required = false) Long codigoUF,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "status", required = false) Integer status
    ) {
        List<Municipio> municipios = new ArrayList<>();

        try {
            Municipio municipio = new Municipio();
            Uf uf = new Uf();

            uf.setCodigoUF(codigoUF);
            municipio.setCodigoMunicipio(codigoMunicipio);
            municipio.setUf(uf);
            municipio.setNome(nome);
            municipio.setStatus(status);
            System.out.println("Município recebido: " + municipio);

            return municipioService.pegar(municipios, MunicipioMapper.mapearParaMunicipio(municipio));
        } catch (IllegalArgumentException e) {
            var erroDTO = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @PutMapping
    public ResponseEntity<Object> atualizar(@RequestBody MunicipioDTO municipioDto){
        try {
            System.out.println("Municipio recebido: " + municipioDto);
            Municipio municipio = municipioService.alterar(municipioDto);
            System.out.println("Municipio atualizada: " + municipio);
        } catch (IllegalArgumentException e) {
            var erroDTO = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }

        List<MunicipioDTO> municipiosDTO = municipioRepository.findAll().stream().
                map(MunicipioMapper::mapearParaMunicipio).toList();
        return ResponseEntity.ok(municipiosDTO);
    }
}
