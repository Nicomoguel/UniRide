# UniRide — Resultados e Impacto

## ¿Qué es UniRide?

UniRide es una aplicación de **transporte compartido (raid) para estudiantes universitarios**. Conecta a conductores (`Driver`) con pasajeros (`Passenger`) que tienen rutas y horarios compatibles, para que puedan compartir el viaje hacia la universidad.

El sistema representa el mapa como un **grafo ponderado** de aproximadamente 140 nodos, calcula la **ruta más corta** del conductor con el algoritmo de **Dijkstra**, y decide qué pasajeros hacen *match* según su horario y la distancia a la ruta.

---

## Resultados del proyecto

### Funcionalidades logradas
- **Inicio de sesión de usuarios** con validación de matrícula y contraseña (`LoginPage`).
- **Persistencia de usuarios** en archivo de texto mediante serialización/deserialización (`DatabaseManager`).
- **Construcción automática del grafo** del mapa a partir de archivos de coordenadas (`ReadNodes`, `AddAllAdjacents`, `ReadAddColumns`, `ReadRemoveAdjacents`).
- **Cálculo de la ruta más corta** del conductor con Dijkstra (`Dijkstra`).
- **Emparejamiento (matchmaking)** entre conductor y pasajeros evaluando:
  - Compatibilidad de **horario** según la tolerancia del pasajero (`Schedule`).
  - **Distancia** del pasajero a la ruta del conductor contra la desviación máxima permitida (`MatchMaker`).
- **Interfaz gráfica** que muestra el mapa, la ruta del conductor (azul), los pasajeros (verde) y una tabla de resultados con el estado de cada match (`GUI`).

### Resultados de diseño
- Arquitectura de **clases pequeñas y especializadas**, cada una con una única responsabilidad.
- **Jerarquía de usuarios** basada en la clase abstracta `User`, extendida por `Driver` y `Passenger`.
- Aplicación de los **principios SOLID** (SRP, OCP, LSP, ISP, DIP).
- **Interfaz `UserRepository`** que define el contrato de persistencia implementado por `DatabaseManager`.
- **Documentación javadoc** completa y sin errores en todo el proyecto.

### Estados de resultado del matchmaking
| Estado | Significado |
|---|---|
| `MATCH` | Conductor y pasajero son compatibles |
| `REJECTED_SCHEDULE` | Rechazado por incompatibilidad de horario |
| `REJECTED_DISTANCE` | Rechazado porque la distancia excede el límite |

---

## Impacto que puede tener

### Impacto social
- **Movilidad estudiantil más accesible**: facilita que estudiantes sin auto lleguen a la universidad compartiendo viaje con compañeros que ya hacen la misma ruta.
- **Comunidad y confianza**: el sistema de puntos de confiabilidad (`userPoints`) y la verificación de usuario (`isUserVerified`) fomentan un entorno seguro entre estudiantes.

### Impacto ambiental
- **Menos autos en circulación**: al compartir un mismo vehículo, se reduce el número de trayectos individuales, disminuyendo la **huella de carbono** y el tráfico alrededor del campus.

### Impacto económico
- **Ahorro para los estudiantes**: se reparten los costos de gasolina y estacionamiento entre conductor y pasajeros.

### Impacto técnico / académico
- Demuestra la aplicación práctica de **estructuras de datos** (grafos), **algoritmos** (Dijkstra) y **principios de diseño de software** (SOLID) en un problema real.
- La arquitectura modular permite **extender el sistema** sin reescribirlo (nuevos tipos de usuario, otra fuente de datos, otra estrategia de matchmaking).

---

## Posibles mejoras a futuro
- Migrar la persistencia de archivo de texto a una **base de datos real** (aprovechando la interfaz `UserRepository`).
- Permitir el **registro de nuevos usuarios** desde la aplicación.
- Incorporar **matchmaking de varios conductores** simultáneamente.
- Mostrar **mapas reales** en lugar de un grafo estático.

---

*Proyecto desarrollado por HoodCodeDepartment.*
