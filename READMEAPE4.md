# 🗺️ APE 4 — Grafos: Mapa del Campus UTA

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Estructura de Datos](https://img.shields.io/badge/Estructura%20de%20Datos-3ro%20B-blue?style=for-the-badge)
![UTA](https://img.shields.io/badge/UTA-FISEI-red?style=for-the-badge)
![Estado](https://img.shields.io/badge/Estado-Completado%20✓-brightgreen?style=for-the-badge)

**Universidad Técnica de Ambato — Facultad de Ingeniería en Sistemas**
**Materia:** Estructura de Datos | **Paralelo:** 3ro B Software
**Autor:** Luis Manobanda | **Grupo:** 8

</div>

---

## 📋 Tabla de Contenidos

1. [Objetivo](#-objetivo)
2. [Descripción del Problema](#-descripción-del-problema)
3. [Estructura del Grafo](#-estructura-del-grafo)
4. [Actividades Realizadas](#-actividades-realizadas)
5. [Implementación](#-implementación)
6. [Conceptos Clave](#-conceptos-clave)
7. [Resultados Obtenidos](#-resultados-obtenidos)
8. [Análisis Comparativo](#-análisis-comparativo)
9. [Compilación y Ejecución](#-compilación-y-ejecución)
10. [Estructura del Proyecto](#-estructura-del-proyecto)
11. [Autor](#-autor)

---

## 🎯 Objetivo

Implementar un **grafo con lista de adyacencia** en Java para representar las rutas dentro del Campus Huachi de la Universidad Técnica de Ambato, y comparar el comportamiento de los algoritmos **BFS** y **Dijkstra** para encontrar rutas entre ubicaciones del campus.

---

## 📌 Descripción del Problema

El campus universitario tiene varias ubicaciones conectadas entre sí por caminos con distintas distancias. El objetivo es encontrar rutas entre estas ubicaciones usando dos criterios distintos:

- **Menos paradas** → BFS
- **Menor distancia total** → Dijkstra

### Ubicaciones del Campus (Nodos)

| ID           | Nombre       |
|--------------|--------------|
| `uta`        | Universidad  |
| `fisei`      | FISEI        |
| `idiomas`    | Idiomas      |
| `biblioteca` | Biblioteca   |
| `estadio`    | Estadio      |
| `comedor`    | Comedor      |

### Conexiones del Campus (Aristas)

| Origen       | Destino      | Distancia |
|--------------|--------------|-----------|
| Universidad  | FISEI        | 50 m      |
| FISEI        | Idiomas      | 40 m      |
| Idiomas      | Biblioteca   | 30 m      |
| Biblioteca   | Estadio      | 70 m      |
| Universidad  | Comedor      | 20 m      |
| Comedor      | Estadio      | 200 m     |

### Mapa visual del grafo

```
Universidad (uta)
    │
    ├──[50]── FISEI ──[40]── Idiomas ──[30]── Biblioteca ──[70]── Estadio
    │                                                                  ▲
    └──[20]── Comedor ─────────────────────────────────[200]──────────┘
```

> **Nota:** Las aristas son **no dirigidas** (bidireccionales), es decir, se puede ir y volver por cualquier camino.

---

## ✅ Actividades Realizadas

### TODO 1 — `agregarNodo()` ✔️
Se implementó la creación de nodos en el grafo:
- Crea un objeto `Nodo` con ID y nombre.
- Lo registra en el mapa `nodos`.
- Inicializa su lista de adyacencia vacía.

### TODO 2 — `agregarArista()` ✔️
Se implementó la conexión bidireccional entre nodos:
- Agrega la arista `origen → destino` con su peso.
- Agrega la arista inversa `destino → origen` (grafo no dirigido).

### TODO 3 — `bfs()` ✔️
Se implementó el algoritmo **Breadth-First Search**:
- Usa una `Queue` de caminos para explorar por niveles.
- Mantiene un `Set` de visitados para evitar ciclos.
- Retorna el primer camino que llega al destino (el de menos paradas).

### TODO 4 — `dijkstra()` ✔️
Se implementó el algoritmo de **Dijkstra**:
- Inicializa todas las distancias en `Integer.MAX_VALUE` (infinito).
- Usa una `PriorityQueue` ordenada por distancia acumulada.
- Actualiza distancias cuando encuentra caminos más cortos.
- Reconstruye el camino óptimo usando el mapa de `anteriores`.

---

## 💻 Implementación

### Estructura de clases

```
APE4_Grafos
├── Nodo          → id, nombre
├── Arista        → destino, peso
└── Grafo
    ├── nodos           (HashMap)
    ├── adyacencia      (HashMap de listas)
    ├── agregarNodo()
    ├── agregarArista()
    ├── bfs()
    ├── dijkstra()
    └── mostrarRuta()
```

### Método `agregarNodo()`

```java
public void agregarNodo(String id, String nombre) {
    Nodo nuevoNodo = new Nodo(id, nombre);       // Crear nodo
    nodos.put(id, nuevoNodo);                    // Registrar en el mapa
    adyacencia.put(id, new ArrayList<>());       // Lista de vecinos vacía
}
```

### Método `agregarArista()`

```java
public void agregarArista(String origen, String destino, int peso) {
    adyacencia.get(origen).add(new Arista(destino, peso));  // origen → destino
    adyacencia.get(destino).add(new Arista(origen, peso));  // destino → origen
}
```

### Algoritmo BFS (fragmento clave)

```java
while (!cola.isEmpty()) {
    List<String> camino = cola.poll();             // Extraer camino actual
    String actual = camino.get(camino.size() - 1); // Último nodo del camino

    if (actual.equals(fin)) return camino;         // Destino encontrado

    for (Arista arista : adyacencia.get(actual)) {
        if (!visitados.contains(arista.destino)) {
            visitados.add(arista.destino);
            List<String> nuevoCamino = new ArrayList<>(camino);
            nuevoCamino.add(arista.destino);
            cola.add(nuevoCamino);                 // Agregar camino extendido
        }
    }
}
```

### Algoritmo Dijkstra (fragmento clave)

```java
while (!cola.isEmpty()) {
    String actual = cola.poll();                              // Menor distancia

    for (Arista arista : adyacencia.get(actual)) {
        int nuevaDistancia = distancias.get(actual) + arista.peso;

        if (nuevaDistancia < distancias.get(arista.destino)) {
            distancias.put(arista.destino, nuevaDistancia);  // Actualizar distancia
            anteriores.put(arista.destino, actual);           // Guardar anterior
            cola.add(arista.destino);                         // Re-encolar vecino
        }
    }
}
```

---

## 📚 Conceptos Clave

### 🔵 BFS — Breadth-First Search (Búsqueda en Amplitud)

| Característica   | Detalle                            |
|------------------|------------------------------------|
| Estrategia       | Explora nivel por nivel (anchura)  |
| Estructura       | Cola (Queue — FIFO)                |
| Objetivo         | Ruta con **menos paradas**         |
| Considera pesos  | No                                 |
| Complejidad      | O(V + E)                           |

> BFS garantiza encontrar la ruta con **menos nodos intermedios**, pero no necesariamente la más corta en distancia.

---

### 🟠 Dijkstra

| Característica   | Detalle                                          |
|------------------|--------------------------------------------------|
| Estrategia       | Greedy — procesa siempre el nodo más cercano     |
| Estructura       | Cola de prioridad (PriorityQueue)                |
| Objetivo         | Ruta con **menor distancia total**               |
| Considera pesos  | Sí                                               |
| Complejidad      | O((V + E) log V)                                 |

> Dijkstra garantiza encontrar la ruta **óptima en distancia**, considerando el peso de cada arista.

---

### 🟢 Lista de Adyacencia

Estructura utilizada para representar el grafo:

```
uta        → [(fisei, 50), (comedor, 20)]
fisei      → [(uta, 50), (idiomas, 40)]
idiomas    → [(fisei, 40), (biblioteca, 30)]
biblioteca → [(idiomas, 30), (estadio, 70)]
estadio    → [(biblioteca, 70), (comedor, 200)]
comedor    → [(uta, 20), (estadio, 200)]
```

**Ventajas:** eficiente en memoria para grafos dispersos, acceso directo a vecinos de cada nodo.

---

## 📊 Resultados Obtenidos

### Salida esperada en consola

```
===== BFS =====
(Ruta con menos paradas)
Universidad (uta) -> Comedor (comedor) -> Estadio (estadio)

===== DIJKSTRA =====
(Ruta con menor distancia total)
Universidad (uta) -> FISEI (fisei) -> Idiomas (idiomas) -> Biblioteca (biblioteca) -> Estadio (estadio)
```

---

## 🔍 Análisis Comparativo

| Criterio             | BFS                              | Dijkstra                                               |
|----------------------|----------------------------------|--------------------------------------------------------|
| Ruta encontrada      | uta → comedor → estadio          | uta → fisei → idiomas → biblioteca → estadio           |
| Número de paradas    | **2 paradas** (la menor)         | 4 paradas                                              |
| Distancia total      | 20 + 200 = **220 m**             | 50 + 40 + 30 + 70 = **190 m** (la menor)               |
| Considera pesos      | No                               | Sí                                                     |

### Conclusión

> Ambos algoritmos encuentran rutas válidas, pero con criterios distintos:
>
> - **BFS** prefiere ir por el **Comedor** porque llega al Estadio en solo 2 saltos.
> - **Dijkstra** prefiere el camino por **FISEI → Idiomas → Biblioteca** porque la distancia total (190 m) es menor que la ruta por el Comedor (220 m).
>
> Esto demuestra que **más paradas no siempre significa más distancia**, y viceversa. La elección del algoritmo depende completamente del problema a resolver.

---

## ⚙️ Compilación y Ejecución

### Requisitos
- Java JDK 8 o superior
- Terminal / CMD / VS Code

### Compilar

```bash
cd src
javac APE4_Grafos.java
```

### Ejecutar

```bash
java APE4_Grafos
```

---

## 📁 Estructura del Proyecto

```
Proyecto_APE4/
│
├── src/
│   └── APE4_Grafos.java       ← Código fuente completo y comentado
│
├── captura/
│   └── captura1.png           ← Captura de pantalla de la ejecución
│
├── README.md                  ← README original de la actividad (Grupo 8)
└── READMEAPE4.md              ← Este archivo — documentación completa
```

---

## 👤 Autor

| Campo           | Detalle                              |
|-----------------|--------------------------------------|
| **Nombre**      | Luis Manobanda                       |
| **Paralelo**    | 3ro B — Ingeniería en Software       |
| **Grupo**       | Grupo 8                              |
| **Materia**     | Estructura de Datos                  |
| **Facultad**    | FISEI                                |
| **Universidad** | Universidad Técnica de Ambato        |

---

*Universidad Técnica de Ambato — FISEI — Estructura de Datos — 2025*
