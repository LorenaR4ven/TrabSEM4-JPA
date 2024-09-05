package br.edu.univille.poo.jpa.entidade;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Data
@NoArgsConstructor
@Entity
public class Tarefa {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false)
    private boolean finalizado;

    @Column(nullable = false)
    private LocalDate dataPrevistaFinalizacao;

    private LocalDate dataFinalizacao;

    // Getters e Setters

}
