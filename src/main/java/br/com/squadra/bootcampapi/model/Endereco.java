package br.com.squadra.bootcampapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "TB_ENDERECO")
@Data
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENDERECO_SEQ")
    @SequenceGenerator(name = "ENDERECO_SEQ", sequenceName = "SEQUENCE_ENDERECO", allocationSize = 1)
    @Column(name = "CODIGO_ENDERECO", length = 9, nullable = false, unique = true)
    private Long codigoEndereco;

    @Column(name = "NOME_RUA", length = 256, nullable = false)
    private String logradouro;

    @Column(name = "NUMERO", length = 10, nullable = false)
    private String numero;

    @Column(name = "COMPLEMENTO", length = 20)
    private String complemento;

    @Column(name = "CEP", length = 10, nullable = false)
    private String cep;

    @ManyToOne
    @JoinColumn(name = "CODIGO_PESSOA", nullable = false) //Em JoinColumn, colocar o nome do atributo/variável que referencia a classe atual (Endereco) na outra classe (Pessoa)
    @ToString.Exclude
    @JsonIgnore
    private Pessoa pessoa;

    @ManyToOne
    @JoinColumn(name = "CODIGO_BAIRRO", nullable = false) //Em JoinColumn, colocar o nome do atributo/variável que referencia a classe atual (Endereco) na outra classe (Bairro)
    @ToString.Exclude
    //@JsonIgnore
    private Bairro bairro;

    @Deprecated
    public Endereco() {
    }
}
