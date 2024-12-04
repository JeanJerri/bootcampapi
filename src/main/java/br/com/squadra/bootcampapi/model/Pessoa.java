package br.com.squadra.bootcampapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_PESSOA")
@Data
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PESSOA_SEQ")
    @SequenceGenerator(name = "PESSOA_SEQ", sequenceName = "SEQUENCE_PESSOA", allocationSize = 1)
    @Column(name = "CODIGO_PESSOA", length = 9, nullable = false, unique = true)
    private Long codigoPessoa;

    @Column(name = "NOME", length = 256, nullable = false)
    private String nome;

    @Column(name = "SOBRENOME", length = 256, nullable = false)
    private String sobrenome;

    @Column(name = "IDADE", length = 3, nullable = false)
    private Integer idade;

    @Column(name = "LOGIN", length = 50, nullable = false)
    private String login;

    @Column(name = "SENHA", length = 50, nullable = false)
    private String senha;

    @Column(name = "STATUS", length = 3, nullable = false)
    private Integer status;

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    //@JsonIgnore
    private List<Endereco> enderecos = new ArrayList<>();

    @Deprecated
    public Pessoa() {
    }
}

