package br.com.squadra.bootcampapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "TB_UF")
@Data
public class Uf {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UF_SEQ")
    @SequenceGenerator(name = "UF_SEQ", sequenceName = "SEQUENCE_UF", allocationSize = 1)
    @Column(name = "CODIGO_UF", length = 9, nullable = false, unique = true)
    private Long codigoUF;

    @Column(name = "SIGLA", length = 3, nullable = false)
    private String sigla;

    @Column(name = "NOME", length = 60, nullable = false) //validar o tamanho do campo nas requisicoes
    private String nome;

    @Column(name = "STATUS", length = 3, nullable = false)
    private Integer status;

    @OneToMany(mappedBy = "uf", cascade = CascadeType.ALL, orphanRemoval = true)
//    @ToString.Exclude
    @JsonIgnore
    private List<Municipio> municipios;

    @Deprecated
    public Uf() {
    }
}
