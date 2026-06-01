# 🗺️ APE 4 — Grafos: Mapa del Campus UTA

**Universidad Técnica de Ambato — Facultad de Ingeniería en Sistemas**
**Materia:** Estructura de Datos | **Paralelo:** 3ro B Software
**Autor:** Luis Manobanda

---

## 📋 Tabla de Contenidos

1. Objetivo
2. Descripción del Problema
3. Estructura del Grafo
4. Actividades Realizadas
5. Implementación
6. Conceptos Clave
7. Resultados Obtenidos
8. Análisis Comparativo
9. Compilación y Ejecución
10. Estructura del Proyecto
11. Autor

---

## 🎯 Objetivo

Implementar un grafo con lista de adyacencia en Java para representar las rutas
dentro del Campus Huachi de la Universidad Técnica de Ambato, y comparar el
comportamiento de los algoritmos BFS y Dijkstra para encontrar rutas entre
ubicaciones del campus.

---

## 📌 Descripción del Problema

El campus universitario tiene varias ubicaciones conectadas entre sí por caminos
con distintas distancias. El objetivo es encontrar rutas entre estas ubicaciones
usando dos criterios distintos:

- Menos paradas      → BFS
- Menor distancia    → Dijkstra

### Ubicaciones del Campus (Nodos)

  ID             Nombre
  -------------- -------------
  uta            Universidad
  fisei          FISEI
  idiomas        Idiomas
  biblioteca     Biblioteca
  estadio        Estadio
  comedor        Comedor

### Conexiones del Campus (Aristas)

  Origen         Destino        Distancia
  -------------- -------------- ----------
  Universidad    FISEI          50 m
  FISEI          Idiomas        40 m
  Idiomas        Biblioteca     30 m
  Biblioteca     Estadio        70 m
  Universidad    Comedor        20 m
  Comedor        Estadio        200 m

### Mapa visual del grafo

  Universidad (uta)
      │
      ├──[50]── FISEI ──[40]── Idiomas ──[30]── Biblioteca ──[70]── Estadio ###
      │                                                                  ▲
      └──[20]── Comedor ─────────────────────────────────[200]───────────┘

  Nota: Las aristas son no dirigidas (bidireccionales).

---

## ✅ Actividades Realizadas

### TODO 1 — agregarNodo() ✔️
- Crea un objeto Nodo con ID y nombre.
- Lo registra en el mapa nodos.
- Inicializa su lista de adyacencia vacía.

### TODO 2 — agregarArista() ✔️
- Agrega la arista origen → destino con su peso.
- Agrega la arista inversa destino → origen (grafo no dirigido).

### TODO 3 — bfs() ✔️
- Usa una Queue de caminos para explorar por niveles.
- Mantiene un Set de visitados para evitar ciclos.
- Retorna el primer camino que llega al destino (el de menos paradas).

### TODO 4 — dijkstra() ✔️
- Inicializa todas las distancias en Integer.MAX_VALUE (infinito).
- Usa una PriorityQueue ordenada por distancia acumulada.
- Actualiza distancias cuando encuentra caminos más cortos.
- Reconstruye el camino óptimo usando el mapa de anteriores.

---

## 💻 Implementación

### Estructura de clases

  APE4_Grafos
  ├── Nodo          → id, nombre
  ├── Arista        → destino, peso
  └── Grafo
      ├── nodos            (HashMap)
      ├── adyacencia       (HashMap de listas)
      ├── agregarNodo()
      ├── agregarArista()
      ├── bfs()
      ├── dijkstra()
      ├── mostrarRuta()
      └── mostrarAdyacencia()

### Método agregarNodo()

  public void agregarNodo(String id, String nombre) {
      nodos.put(id, new Nodo(id, nombre));
      adyacencia.put(id, new ArrayList<>());
  }

### Método agregarArista()

  public void agregarArista(String origen, String destino, int peso) {
      adyacencia.get(origen).add(new Arista(destino, peso));
      adyacencia.get(destino).add(new Arista(origen, peso));
  }

### Algoritmo BFS (fragmento clave)

  while (!cola.isEmpty()) {
      List<String> camino = cola.poll();
      String actual = camino.get(camino.size() - 1);

      if (actual.equals(fin)) return camino;

      for (Arista arista : adyacencia.get(actual)) {
          if (!visitados.contains(arista.destino)) {
              visitados.add(arista.destino);
              List<String> nuevoCamino = new ArrayList<>(camino);
              nuevoCamino.add(arista.destino);
              cola.add(nuevoCamino);
          }
      }
  }

### Algoritmo Dijkstra (fragmento clave)

  while (!cola.isEmpty()) {
      String actual = cola.poll();

      for (Arista arista : adyacencia.get(actual)) {
          int nuevaDistancia = distancias.get(actual) + arista.peso;

          if (nuevaDistancia < distancias.get(arista.destino)) {
              distancias.put(arista.destino, nuevaDistancia);
              anteriores.put(arista.destino, actual);
              cola.add(arista.destino);
          }
      }
  }

---

## 📚 Conceptos Clave

### BFS — Breadth-First Search

  Característica     Detalle
  ------------------ -----------------------------------
  Estrategia         Explora nivel por nivel (anchura)
  Estructura         Cola (Queue — FIFO)
  Objetivo           Ruta con menos paradas
  Considera pesos    No
  Complejidad        O(V + E)

  BFS garantiza la ruta con menos nodos intermedios,
  pero no necesariamente la más corta en distancia.

### Dijkstra

  Característica     Detalle
  ------------------ -------------------------------------------
  Estrategia         Greedy — procesa siempre el nodo más cercano
  Estructura         Cola de prioridad (PriorityQueue)
  Objetivo           Ruta con menor distancia total
  Considera pesos    Sí
  Complejidad        O((V + E) log V)

  Dijkstra garantiza la ruta óptima en distancia,
  considerando el peso de cada arista.

### Lista de Adyacencia

  uta        → [fisei (50m), comedor (20m)]
  fisei      → [uta (50m), idiomas (40m)]
  idiomas    → [fisei (40m), biblioteca (30m)]
  biblioteca → [idiomas (30m), estadio (70m)]
  estadio    → [biblioteca (70m), comedor (200m)]
  comedor    → [uta (20m), estadio (200m)]

---

## 📊 Resultados Obtenidos

### Salida en consola

  ╔══════════════════════════════════════════════════════╗
  ║        Universidad Tecnica de Ambato - FISEI         ║
  ║        APE 4 - Grafos: Mapa del Campus UTA           ║
  ║        Estructura de Datos - 3ro B Software          ║
  ║        Autor : Luis Manobanda                        ║
  ╚══════════════════════════════════════════════════════╝

  ╔════════════════════════════════════════════════════════╗
  ║  PASO 4 - BFS (Ruta con menos paradas)                 ║
  ╚════════════════════════════════════════════════════════╝

  ===== BFS =====
  (Ruta con menos paradas)
  Universidad (uta) -> Comedor (comedor) -> Estadio (estadio)
  Paradas   : 2
  Distancia : 220 m

  ╔════════════════════════════════════════════════════════╗
  ║  PASO 5 - Dijkstra (Ruta con menor distancia)          ║
  ╚════════════════════════════════════════════════════════╝

  ===== DIJKSTRA =====
  (Ruta con menor distancia total)
  Universidad (uta) -> FISEI (fisei) -> Idiomas (idiomas) -> Biblioteca (biblioteca) -> Estadio (estadio)
  Paradas   : 4
  Distancia : 190 m

---

## 🔍 Análisis Comparativo

  Criterio           BFS                       Dijkstra
  ------------------ ------------------------- ----------------------------------------
  Ruta encontrada    uta→comedor→estadio        uta→fisei→idiomas→biblioteca→estadio
  Paradas            2 paradas (menor)          4 paradas
  Distancia total    20+200 = 220 m             50+40+30+70 = 190 m (menor)
  Considera pesos    No                         Sí

### Conclusión

  - BFS elige la ruta por el Comedor porque llega al Estadio en solo 2 saltos.
  - Dijkstra elige la ruta por FISEI porque la distancia total (190 m)
    es menor que por el Comedor (220 m).

  Más paradas no siempre significa más distancia.
  La elección del algoritmo depende del problema a resolver.

---

## ⚙️ Compilación y Ejecución

  Requisitos: Java JDK 8 o superior

  Compilar:
    cd src
    javac APE4_Grafos.java

  Ejecutar:
    java APE4_Grafos

---

## 📁 Estructura del Proyecto

  Proyecto_APE4/
  │
  ├── src/
  │   └── APE4_Grafos.java       ← Código fuente completo y comentado
  │
  ├── captura/
  │   └── captura1.png           ← Captura de pantalla de la ejecución
  │
  ├── README-Grupo8.md           ← README original de la actividad
  └── README.md                  ← Documentación completa

---

## 👤 Autor

  Universidad  : Universidad Técnica de Ambato
  Nombre       : Luis Manobanda
  Paralelo     : 3ro B — Ingeniería en Software
  Materia      : Estructura de Datos
  Facultad     : FISEI
  
---

Universidad Técnica de Ambato — FISEI — Estructura de Datos — 2026