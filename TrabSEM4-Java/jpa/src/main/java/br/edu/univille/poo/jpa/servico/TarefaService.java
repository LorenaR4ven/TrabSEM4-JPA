package br.edu.univille.poo.jpa.servico;

import br.edu.univille.poo.jpa.entidade.Tarefa;
import br.edu.univille.poo.jpa.repositorio.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {
    @Autowired
    private TarefaRepository tarefaRepository;

    public List<Tarefa> obterTodasTarefas() {
        return tarefaRepository.findAll();
    }

    public Optional<Tarefa> obterTarefaPorId(Long id) {
        return tarefaRepository.findById(id);
    }

    @Transactional
    public Tarefa inserirTarefa(Tarefa tarefa) {
        validarTarefa(tarefa);
        tarefa.setId(0L);
        return tarefaRepository.save(tarefa);
    }

    @Transactional
    public Tarefa atualizarTarefa(Long id, Tarefa novaTarefa) {
        Tarefa tarefaExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (tarefaExistente.isFinalizado()) {
            throw new RuntimeException("Tarefa finalizada não pode ser modificada");
        }

        validarTarefa(novaTarefa);

        tarefaExistente.setTitulo(novaTarefa.getTitulo());
        tarefaExistente.setDescricao(novaTarefa.getDescricao());
        tarefaExistente.setDataPrevistaFinalizacao(novaTarefa.getDataPrevistaFinalizacao());
        tarefaExistente.setFinalizado(novaTarefa.isFinalizado());
        tarefaExistente.setDataFinalizacao(novaTarefa.getDataFinalizacao());

        return tarefaRepository.save(tarefaExistente);
    }

    @Transactional
    public void deletarTarefa(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (tarefa.isFinalizado()) {
            throw new RuntimeException("Tarefa finalizada não pode ser excluída");
        }

        tarefaRepository.deleteById(id);
    }

    public List<Tarefa> obterTarefasNaoFinalizadas() {
        return tarefaRepository.findByFinalizadoFalse();
    }

    public List<Tarefa> obterTarefasFinalizadas() {
        return tarefaRepository.findByFinalizadoTrue();
    }

    public List<Tarefa> obterTarefasAtrasadas() {
        return tarefaRepository.findByFinalizadoFalseAndDataPrevistaFinalizacaoBefore(LocalDate.now());
    }

    public List<Tarefa> obterTarefasNaoFinalizadasEntreDatas(LocalDate inicio, LocalDate fim) {
        return tarefaRepository.findByFinalizadoFalseAndDataPrevistaFinalizacaoBetween(inicio, fim);
    }

    @Transactional
    public Tarefa finalizarTarefa(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (tarefa.isFinalizado()) {
            throw new RuntimeException("Tarefa já está finalizada");
        }

        tarefa.setFinalizado(true);
        tarefa.setDataFinalizacao(LocalDate.now());

        return tarefaRepository.save(tarefa);
    }

    private void validarTarefa(Tarefa tarefa) {
        if (tarefa.getTitulo() == null || tarefa.getTitulo().length() < 5) {
            throw new RuntimeException("O título da tarefa deve conter pelo menos 5 caracteres");
        }

        if (tarefa.getDataPrevistaFinalizacao() == null) {
            throw new RuntimeException("A data prevista de finalização é obrigatória");
        }
    }
}
