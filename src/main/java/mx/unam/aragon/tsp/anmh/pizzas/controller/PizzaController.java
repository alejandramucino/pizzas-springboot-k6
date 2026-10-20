package mx.unam.aragon.tsp.anmh.pizzas.controller;

import java.net.URI;
import java.util.List;
import mx.unam.aragon.tsp.anmh.pizzas.model.Pizza;
import mx.unam.aragon.tsp.anmh.pizzas.repository.PizzaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {
    private final PizzaRepository repository;

    public PizzaController(PizzaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Pizza> listar() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Pizza> crear(@RequestBody Pizza pizza) {
        pizza.setId(null);
        Pizza guardada = repository.save(pizza);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(guardada.getId()).toUri();
        return ResponseEntity.created(ubicacion).body(guardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pizza> actualizar(@PathVariable String id, @RequestBody Pizza pizza) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        pizza.setId(id);
        return ResponseEntity.ok(repository.save(pizza));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
