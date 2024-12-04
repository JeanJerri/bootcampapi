package br.com.squadra.bootcampapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "TB_MUNICIPIO")
@Data
public class Municipio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MUNICIPIO_SEQ")
    @SequenceGenerator(name = "MUNICIPIO_SEQ", sequenceName = "SEQUENCE_MUNICIPIO", allocationSize = 1)
    @Column(name = "CODIGO_MUNICIPIO", length = 9, nullable = false, unique = true)
    private Long codigoMunicipio;

//    @Column(name = "CODIGO_UF", length = 9, nullable = false)
//    private Long codigoUf;

    @Column(name = "NOME", length = 256)
    private String nome;

    @Column(name = "STATUS", length = 3)
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "CODIGO_UF")
    @ToString.Exclude
//    @JsonIgnore
    private Uf uf;

    @OneToMany(mappedBy = "municipio")
//    @ToString.Exclude
    @JsonIgnore
    private List<Bairro> bairros;

    @Deprecated
    public Municipio() {
    }
}
