package mx.unam.aragon.tsp.anmh.pizzas.repository;

import mx.unam.aragon.tsp.anmh.pizzas.model.Pizza;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PizzaRepository extends MongoRepository<Pizza, String> {}
