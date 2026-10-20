import { htmlReport } from 'https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js';
import { check, fail } from 'k6';
import http from 'k6/http';

const baseUrl = __ENV.BASE_URL || 'http://localhost:8080/api/pizzas';
const jsonHeaders = { headers: { 'Content-Type': 'application/json' } };

// Prueba inicial: una usuaria virtual ejecuta una vez los cuatro métodos.
export const options = {
  vus: 1,
  iterations: 1,
  thresholds: {
    checks: ['rate==1'],
    http_req_failed: ['rate==0'],
  },
};

export default function () {
  const nuevaPizza = {
    nombre: 'Pizza prueba k6',
    ingredientes: 'Queso y tomate',
    precio: 120.0,
  };

  const creada = http.post(baseUrl, JSON.stringify(nuevaPizza), {
    ...jsonHeaders,
    tags: { name: 'POST /api/pizzas' },
  });
  const creacionCorrecta = check(creada, {
    'POST crea pizza (201)': (r) => r.status === 201,
  });
  if (!creacionCorrecta) fail('No se pudo crear la pizza: ' + creada.body);

  const pizzaId = creada.json('id');
  if (!pizzaId) fail('El POST no devolvió un id');

  const listado = http.get(baseUrl, { tags: { name: 'GET /api/pizzas' } });
  check(listado, {
    'GET consulta pizzas (200)': (r) => r.status === 200,
    'GET devuelve la pizza creada': (r) => r.status === 200 && r.body.includes(pizzaId),
  });

  const modificada = http.put(`${baseUrl}/${pizzaId}`, JSON.stringify({
    nombre: 'Pizza prueba k6 actualizada',
    ingredientes: 'Queso, tomate y champiñones',
    precio: 135.0,
  }), {
    ...jsonHeaders,
    tags: { name: 'PUT /api/pizzas/{id}' },
  });
  check(modificada, {
    'PUT actualiza pizza (200)': (r) => r.status === 200,
    'PUT conserva el id': (r) => r.status === 200 && r.json('id') === pizzaId,
  });

  const eliminada = http.del(`${baseUrl}/${pizzaId}`, null, {
    tags: { name: 'DELETE /api/pizzas/{id}' },
  });
  check(eliminada, {
    'DELETE elimina pizza (204)': (r) => r.status === 204,
  });
}

export function handleSummary(data) {
  return {
    'index.html': htmlReport(data),
  };
}
