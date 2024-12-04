package br.com.squadra.bootcampapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "TB_BAIRRO")
@Data
public class Bairro {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BAIRRO_SEQ")
    @SequenceGenerator(name = "BAIRRO_SEQ", sequenceName = "SEQUENCE_BAIRRO", allocationSize = 1)
    @Column(name = "CODIGO_BAIRRO", length = 9, nullable = false, unique = true)
    private Long codigoBairro;

//    @Column(name = "CODIGO_MUNICIPIO", length = 9, nullable = false)
//    private Long codigoMunicipio;

    @Column(name = "NOME", length = 256, nullable = false)
    private String nome;

    @Column(name = "STATUS", length = 3)
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "CODIGO_MUNICIPIO") //Em JoinColumn, colocar o nome do atributo/variável que referencia a classe atual (Bairro) na outra classe (Municipio)
    @ToString.Exclude
    private Municipio municipio;

    @OneToMany(mappedBy = "bairro") //Em mappedBy, colocar o nome do atributo/variável que referencia a classe atual (Bairro) na outra classe (Endereco)
//    @ToString.Exclude
    @JsonIgnore
    private List<Endereco> enderecos;

    @Deprecated
    public Bairro() {
    }
}
