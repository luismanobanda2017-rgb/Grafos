import java.util.*;

/**
 * APE 4 — Grafos: Mapa del Campus UTA
 * Estructura de Datos — 3ro B Software
 * Autor: Luis Manobanda
 * Universidad Técnica de Ambato — FISEI
 *
 * Este programa implementa un grafo con lista de adyacencia
 * para representar rutas dentro del Campus Huachi de la UTA.
 * Se comparan los algoritmos BFS y Dijkstra para encontrar rutas.
 */
public class APE4_Grafos {

    // ═══════════════════════════════════════
    // Clase Nodo
    // Representa una ubicación dentro del campus
    // ═══════════════════════════════════════
    static class Nodo {
        String id;      // Identificador único del nodo (clave)
        String nombre;  // Nombre legible del lugar

        public Nodo(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
    }

    // ═══════════════════════════════════════
    // Clase Arista
    // Representa una conexión entre dos nodos con distancia (peso)
    // ═══════════════════════════════════════
    static class Arista {
        String destino; // ID del nodo destino
        int peso;       // Distancia o costo de la conexión

        public Arista(String destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    // ═══════════════════════════════════════
    // Clase Grafo
    // Estructura principal que contiene nodos y aristas
    // ═══════════════════════════════════════
    static class Grafo {

        // Mapa de nodos: clave = id del nodo, valor = objeto Nodo
        Map<String, Nodo> nodos = new HashMap<>();

        // Lista de adyacencia: clave = id del nodo, valor = lista de aristas salientes
        Map<String, List<Arista>> adyacencia = new HashMap<>();

        // ═══════════════════════════════════
        // TODO 1 — COMPLETADO
        // Agregar nodo al grafo
        // ═══════════════════════════════════
        public void agregarNodo(String id, String nombre) {

            // Crear el objeto Nodo con su ID y nombre
            Nodo nuevoNodo = new Nodo(id, nombre);

            // Registrar el nodo en el mapa de nodos
            nodos.put(id, nuevoNodo);

            // Inicializar la lista de adyacencia vacía para este nodo
            // (lista donde se guardarán sus vecinos/conexiones)
            adyacencia.put(id, new ArrayList<>());
        }

        // ═══════════════════════════════════
        // TODO 2 — COMPLETADO
        // Agregar arista no dirigida (bidireccional)
        // ═══════════════════════════════════
        public void agregarArista(String origen, String destino, int peso) {

            // Agregar conexión de origen → destino con el peso dado
            adyacencia.get(origen).add(new Arista(destino, peso));

            // Agregar conexión inversa destino → origen (grafo no dirigido)
            // Esto permite moverse en ambas direcciones entre los nodos
            adyacencia.get(destino).add(new Arista(origen, peso));
        }

        // ═══════════════════════════════════
        // TODO 3 — COMPLETADO — BFS
        // Ruta con menos paradas (sin considerar pesos)
        // Breadth-First Search: recorre por niveles (anchura)
        // ═══════════════════════════════════
        public List<String> bfs(String inicio, String fin) {

            // Cola para recorrer niveles — cada elemento es un camino completo
            Queue<List<String>> cola = new LinkedList<>();

            // Conjunto de nodos ya visitados para evitar ciclos
            Set<String> visitados = new HashSet<>();

            // Camino inicial que solo contiene el nodo de partida
            List<String> caminoInicial = new ArrayList<>();

            // Agregar nodo inicio al camino inicial
            caminoInicial.add(inicio);

            // Agregar el camino inicial a la cola de exploración
            cola.add(caminoInicial);

            // Marcar el nodo de inicio como visitado
            visitados.add(inicio);

            // Mientras haya caminos por explorar en la cola
            while (!cola.isEmpty()) {

                // Obtener el primer camino de la cola (FIFO)
                List<String> camino = cola.poll();

                // El nodo actual es el último del camino explorado
                String actual = camino.get(camino.size() - 1);

                // Si llegamos al destino, retornamos el camino encontrado
                if (actual.equals(fin)) {
                    return camino;
                }

                // Recorrer todos los vecinos del nodo actual
                for (Arista arista : adyacencia.get(actual)) {

                    // Verificar si el vecino NO fue visitado previamente
                    if (!visitados.contains(arista.destino)) {

                        // Marcar vecino como visitado para no procesarlo de nuevo
                        visitados.add(arista.destino);

                        // Crear nuevo camino copiando el camino actual
                        List<String> nuevoCamino = new ArrayList<>(camino);

                        // Agregar el vecino al nuevo camino
                        nuevoCamino.add(arista.destino);

                        // Agregar el nuevo camino extendido a la cola
                        cola.add(nuevoCamino);
                    }
                }
            }

            // Si se agotó la cola sin encontrar destino, no existe ruta
            return null;
        }

        // ═══════════════════════════════════
        // TODO 4 — COMPLETADO — Dijkstra
        // Ruta con menor distancia total (considera pesos)
        // ═══════════════════════════════════
        public List<String> dijkstra(String inicio, String fin) {

            // Mapa de distancias mínimas conocidas desde el inicio hacia cada nodo
            Map<String, Integer> distancias = new HashMap<>();

            // Mapa de nodo anterior en el camino óptimo (para reconstruir la ruta)
            Map<String, String> anteriores = new HashMap<>();

            // Cola de prioridad que siempre extrae el nodo con menor distancia acumulada
            PriorityQueue<String> cola = new PriorityQueue<>(
                    Comparator.comparingInt(distancias::get)
            );

            // Inicializar todas las distancias como "infinito" (Integer.MAX_VALUE)
            // Esto indica que aún no conocemos cómo llegar a esos nodos
            for (String nodo : nodos.keySet()) {
                distancias.put(nodo, Integer.MAX_VALUE);
            }

            // La distancia del nodo de inicio a sí mismo es 0
            distancias.put(inicio, 0);

            // Agregar el nodo de inicio a la cola de prioridad
            cola.add(inicio);

            // Procesar nodos mientras la cola no esté vacía
            while (!cola.isEmpty()) {

                // Obtener el nodo con la menor distancia acumulada
                String actual = cola.poll();

                // Explorar todas las aristas salientes del nodo actual
                for (Arista arista : adyacencia.get(actual)) {

                    // Calcular la nueva distancia pasando por el nodo actual
                    int nuevaDistancia = distancias.get(actual) + arista.peso;

                    // Si la nueva distancia es menor a la distancia conocida del vecino
                    if (nuevaDistancia < distancias.get(arista.destino)) {

                        // Actualizar la distancia mínima al vecino
                        distancias.put(arista.destino, nuevaDistancia);

                        // Guardar el nodo actual como anterior del vecino (para reconstruir ruta)
                        anteriores.put(arista.destino, actual);

                        // Agregar el vecino a la cola para procesar con la nueva distancia
                        cola.add(arista.destino);
                    }
                }
            }

            // ─────────────────────────────────
            // Reconstruir el camino óptimo
            // Se recorre el mapa de anteriores desde el fin hasta el inicio
            // ─────────────────────────────────
            List<String> camino = new ArrayList<>();

            String actual = fin;

            // Retroceder desde el nodo destino hasta el origen usando el mapa de anteriores
            while (actual != null) {
                camino.add(0, actual); // Insertar al inicio para mantener orden correcto
                actual = anteriores.get(actual);
            }

            return camino;
        }

        // ═══════════════════════════════════
        // Mostrar resultado en consola
        // Imprime la ruta con nombres legibles de los nodos
        // ═══════════════════════════════════
        public void mostrarRuta(List<String> ruta) {

            if (ruta == null) {
                System.out.println("No existe ruta");
                return;
            }

            for (int i = 0; i < ruta.size(); i++) {

                String idNodo = ruta.get(i);

                Nodo nodo = nodos.get(idNodo);

                // Mostrar nombre e ID del nodo
                System.out.print(nodo.nombre + " (" + nodo.id + ")");

                // Separar nodos con flecha excepto el último
                if (i < ruta.size() - 1) {
                    System.out.print(" -> ");
                }
            }

            System.out.println();
        }
    }

    // ═══════════════════════════════════════
    // MAIN — Punto de entrada del programa
    // ═══════════════════════════════════════
    public static void main(String[] args) {

        // Crear instancia del grafo
        Grafo grafo = new Grafo();

        // ─────────────────────────────────
        // NODOS — Ubicaciones del campus UTA
        // ─────────────────────────────────
        grafo.agregarNodo("uta",        "Universidad");
        grafo.agregarNodo("fisei",      "FISEI");
        grafo.agregarNodo("idiomas",    "Idiomas");
        grafo.agregarNodo("biblioteca", "Biblioteca");
        grafo.agregarNodo("estadio",    "Estadio");
        grafo.agregarNodo("comedor",    "Comedor");

        // ─────────────────────────────────
        // ARISTAS — Conexiones entre lugares
        // ─────────────────────────────────
        // Ruta larga pero con menos paradas intermedias hacia el estadio
        grafo.agregarArista("uta",       "fisei",      50);
        grafo.agregarArista("fisei",     "idiomas",    40);
        grafo.agregarArista("idiomas",   "biblioteca", 30);
        grafo.agregarArista("biblioteca","estadio",    70);

        // Ruta con menos paradas pero mayor distancia total
        // BFS preferirá esta (2 saltos vs 4 saltos)
        // Dijkstra preferirá la ruta larga (190 vs 220 de costo total)
        grafo.agregarArista("uta",     "comedor", 20);
        grafo.agregarArista("comedor", "estadio", 200);

        // ═══════════════════════════════════
        // PRUEBAS — Ejecutar ambos algoritmos
        // ═══════════════════════════════════

        System.out.println("===== BFS =====");
        System.out.println("(Ruta con menos paradas)");

        List<String> rutaBFS = grafo.bfs("uta", "estadio");
        grafo.mostrarRuta(rutaBFS);

        System.out.println("\n===== DIJKSTRA =====");
        System.out.println("(Ruta con menor distancia total)");

        List<String> rutaDijkstra = grafo.dijkstra("uta", "estadio");
        grafo.mostrarRuta(rutaDijkstra);
    }
}
