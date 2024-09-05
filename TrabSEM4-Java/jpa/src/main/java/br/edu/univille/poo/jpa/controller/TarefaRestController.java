package br.edu.univille.poo.jpa.controller;

import br.edu.univille.poo.jpa.entidade.Tarefa;
import br.edu.univille.poo.jpa.servico.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController()
@RequestMapping("api/tarefa")
public class TarefaRestController {


        @Autowired
        private TarefaService tarefaService;

        @GetMapping
        public List<Tarefa> obterTodasTarefas() {
            return tarefaService.obterTodasTarefas();
        }

        @GetMapping("/{id}")
        public ResponseEntity<Tarefa> obterTarefaPorId(@PathVariable Long id) {
            var opt = tarefaService.obterTarefaPorId(id);
            return opt.map(tarefa -> new ResponseEntity<>(tarefa, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        @PostMapping
        public ResponseEntity<?> inserirTarefa(@RequestBody Tarefa tarefa) {
            try {
                Tarefa novaTarefa = tarefaService.inserirTarefa(tarefa);
                return new ResponseEntity<>(novaTarefa, HttpStatus.CREATED);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
            }
        }

        @PutMapping("/{id}")
        public ResponseEntity<Tarefa> atualizarTarefa(@PathVariable Long id, @RequestBody Tarefa tarefa) {
            try {
                Tarefa tarefaAtualizada = tarefaService.atualizarTarefa(id, tarefa);
                return new ResponseEntity<>(tarefaAtualizada, HttpStatus.OK);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletarTarefa(@PathVariable Long id) {
            try {
                tarefaService.deletarTarefa(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        @PutMapping("/{id}/finalizar")
        public ResponseEntity<Tarefa> finalizarTarefa(@PathVariable Long id) {
            try {
                Tarefa tarefaFinalizada = tarefaService.finalizarTarefa(id);
                return new ResponseEntity<>(tarefaFinalizada, HttpStatus.OK);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        @GetMapping("/nao-finalizadas")
        public List<Tarefa> obterTarefasNaoFinalizadas() {
            return tarefaService.obterTarefasNaoFinalizadas();
        }

        @GetMapping("/finalizadas")
        public List<Tarefa> obterTarefasFinalizadas() {
            return tarefaService.obterTarefasFinalizadas();
        }

        @GetMapping("/atrasadas")
        public List<Tarefa> obterTarefasAtrasadas() {
            return tarefaService.obterTarefasAtrasadas();
        }
    }
