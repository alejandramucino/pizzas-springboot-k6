# CRUD de pizzas con MongoDB

La aplicación usa Java 21, Spring Boot y MongoDB.

## 1. Iniciar MongoDB en Docker

Si aún no tienes un contenedor MongoDB, ejecuta:

```bash
docker run -d --name mongo-pizzas -p 27017:27017 -v mongo_pizzas_data:/data/db mongo:7
```

Si ya utilizas MongoDB, **no crees otro contenedor**: comprueba con `docker ps` el puerto que expone y ajusta `MONGODB_URI` si es necesario.

## 2. Iniciar Spring Boot

Desde la carpeta `pizzas`, con Java 21 instalado:

```bash
./gradlew bootRun
```

Por defecto se conecta a `mongodb://localhost:27017/pizzas_db`. Puedes configurar otra conexión con la variable de entorno `MONGODB_URI`.

## 3. Probar los cuatro endpoints

**GET** — listar pizzas (puede devolver `[]` al inicio):

```bash
curl -i http://localhost:8080/api/pizzas
```

**POST** — crear una pizza (devuelve `201 Created` y un `id`):

```bash
curl -i -X POST http://localhost:8080/api/pizzas -H 'Content-Type: application/json' -d '{"nombre":"Hawaiana","ingredientes":"Jamón y piña","precio":150.0}'
```

**PUT** — actualizar una pizza (reemplaza `ID_DE_LA_PIZZA` por el `id` del POST):

```bash
curl -i -X PUT http://localhost:8080/api/pizzas/ID_DE_LA_PIZZA -H 'Content-Type: application/json' -d '{"nombre":"Hawaiana grande","ingredientes":"Jamón, piña y queso","precio":180.0}'
```

**DELETE** — eliminar esa pizza (devuelve `204 No Content`):

```bash
curl -i -X DELETE http://localhost:8080/api/pizzas/ID_DE_LA_PIZZA
```

Si un ID no existe, PUT y DELETE devuelven `404 Not Found`.

> Antes de una prueba de carga con k6, usa una base de datos de prueba: POST, PUT y DELETE modifican los datos.

## 4. Generar el reporte HTML con k6

Con Spring Boot funcionando y desde esta carpeta, ejecuta:

```bash
k6 run pizzas-test.js
```

El script crea una pizza temporal, la consulta, la actualiza y la elimina. Al terminar, genera `index.html` en esta misma carpeta. **No incluyas un reporte inventado**: entrega el archivo que produjo tu propia ejecución.

El script utiliza el reporter solicitado en clase y requiere acceso a GitHub para descargarlo la primera vez. Para apuntar a otro servidor puedes usar `k6 run -e BASE_URL=http://localhost:8081/api/pizzas pizzas-test.js`.

