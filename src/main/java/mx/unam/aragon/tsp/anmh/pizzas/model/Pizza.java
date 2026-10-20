package mx.unam.aragon.tsp.anmh.pizzas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pizzas")
public class Pizza {
    @Id
    private String id;
    private String nombre;
    private String ingredientes;
    private double precio;

    public Pizza() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getIngredientes() { return ingredientes; }
    public void setIngredientes(String ingredientes) { this.ingredientes = ingredientes; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}
