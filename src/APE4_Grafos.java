import java.util.*;

/**
 * Universidad Técnica de Ambato — FISEI
 * APE 4 — Grafos: Mapa del Campus UTA
 * Estructura de Datos — 3ro B Software
 * Autor: Luis Manobanda
 */
public class APE4_Grafos {

    // ═══════════════════════════════════════
    // Clase Nodo — representa una ubicación del campus
    // ═══════════════════════════════════════
    static class Nodo {
        String id;
        String nombre;

        public Nodo(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
    }

    // ═══════════════════════════════════════
    // Clase Arista — conexión entre dos nodos con distancia
    // ═══════════════════════════════════════
    static class Arista {
        String destino;
        int peso;

        public Arista(String destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    // ═══════════════════════════════════════
    // Clase Grafo — estructura principal
    // ═══════════════════════════════════════
    static class Grafo {

        Map<String, Nodo>         nodos      = new HashMap<>();
        Map<String, List<Arista>> adyacencia = new HashMap<>();

        // ═══════════════════════════════════
        // TODO 1 — Agregar nodo al grafo
        // ═══════════════════════════════════
        public void agregarNodo(String id, String nombre) {
            // Crear el nodo y registrarlo en el mapa
            nodos.put(id, new Nodo(id, nombre));
            // Inicializar lista de vecinos vacía
            adyacencia.put(id, new ArrayList<>());

            System.out.println("  [+] Nodo     : " + nombre + " (" + id + ")");
        }

        // ═══════════════════════════════════
        // TODO 2 — Agregar arista no dirigida (bidireccional)
        // ═══════════════════════════════════
        public void agregarArista(String origen, String destino, int peso) {
            // Conexión origen → destino
            adyacencia.get(origen).add(new Arista(destino, peso));
            // Conexión inversa destino → origen
            adyacencia.get(destino).add(new Arista(origen, peso));

            System.out.println("  [~] Arista   : "
                    + nodos.get(origen).nombre
                    + " <--[" + peso + "m]--> "
                    + nodos.get(destino).nombre);
        }

        // ═══════════════════════════════════
        // TODO 3 — BFS: ruta con menos paradas
        // ═══════════════════════════════════
        public List<String> bfs(String inicio, String fin) {

            // Cola FIFO — cada elemento es un camino completo
            Queue<List<String>> cola = new LinkedList<>();
            // Visitados para evitar ciclos
            Set<String> visitados = new HashSet<>();

            // Camino inicial con el nodo de partida
            List<String> caminoInicial = new ArrayList<>();
            caminoInicial.add(inicio);   // Agregar inicio al camino
            cola.add(caminoInicial);     // Encolar camino inicial
            visitados.add(inicio);       // Marcar inicio como visitado

            while (!cola.isEmpty()) {
                // Obtener el primer camino de la cola
                List<String> camino = cola.poll();
                // Nodo actual = último del camino
                String actual = camino.get(camino.size() - 1);

                // Si llegamos al destino, retornar camino
                if (actual.equals(fin)) return camino;

                // Explorar vecinos no visitados
                for (Arista arista : adyacencia.get(actual)) {
                    if (!visitados.contains(arista.destino)) {
                        visitados.add(arista.destino);
                        List<String> nuevoCamino = new ArrayList<>(camino);
                        nuevoCamino.add(arista.destino);
                        cola.add(nuevoCamino);
                    }
                }
            }
            return null;
        }

        // ═══════════════════════════════════
        // TODO 4 — Dijkstra: ruta con menor distancia
        // ═══════════════════════════════════
        public List<String> dijkstra(String inicio, String fin) {

            // Distancias mínimas desde el inicio a cada nodo
            Map<String, Integer> distancias = new HashMap<>();
            // Nodo anterior en el camino óptimo
            Map<String, String>  anteriores = new HashMap<>();

            // Cola de prioridad ordenada por menor distancia acumulada
            PriorityQueue<String> cola = new PriorityQueue<>(
                    Comparator.comparingInt(distancias::get)
            );

            // Inicializar todas las distancias en infinito
            for (String nodo : nodos.keySet()) {
                distancias.put(nodo, Integer.MAX_VALUE);
            }

            // Distancia al inicio = 0, agregar a la cola
            distancias.put(inicio, 0);
            cola.add(inicio);

            while (!cola.isEmpty()) {
                // Extraer nodo con menor distancia acumulada
                String actual = cola.poll();

                for (Arista arista : adyacencia.get(actual)) {
                    // Calcular nueva distancia pasando por el nodo actual
                    int nuevaDistancia = distancias.get(actual) + arista.peso;

                    // Si encontramos camino más corto al vecino, actualizar
                    if (nuevaDistancia < distancias.get(arista.destino)) {
                        distancias.put(arista.destino, nuevaDistancia);
                        anteriores.put(arista.destino, actual);
                        cola.add(arista.destino);
                    }
                }
            }

            // Reconstruir camino desde fin hacia inicio
            List<String> camino = new ArrayList<>();
            String actual = fin;
            while (actual != null) {
                camino.add(0, actual);
                actual = anteriores.get(actual);
            }
            return camino;
        }

        // ═══════════════════════════════════
        // Mostrar ruta con distancias entre paradas
        // ═══════════════════════════════════
        public void mostrarRuta(List<String> ruta) {
            if (ruta == null || ruta.size() <= 1) {
                System.out.println("  No existe ruta.");
                return;
            }

            int distanciaTotal = 0;
            StringBuilder sb = new StringBuilder("  ");

            for (int i = 0; i < ruta.size(); i++) {
                Nodo nodo = nodos.get(ruta.get(i));
                sb.append(nodo.nombre).append(" (").append(nodo.id).append(")");

                if (i < ruta.size() - 1) {
                    int peso = getPeso(ruta.get(i), ruta.get(i + 1));
                    distanciaTotal += peso;
                    sb.append(" -> ");
                }
            }

            System.out.println(sb);
            System.out.println("  Paradas   : " + (ruta.size() - 1));
            System.out.println("  Distancia : " + distanciaTotal + " m");
        }

        // Obtener peso de arista entre dos nodos
        private int getPeso(String origen, String destino) {
            for (Arista a : adyacencia.get(origen)) {
                if (a.destino.equals(destino)) return a.peso;
            }
            return 0;
        }

        // Mostrar lista de adyacencia completa
        public void mostrarAdyacencia() {
            for (String id : new TreeMap<>(nodos).keySet()) {
                System.out.print("  " + nodos.get(id).nombre + " -> ");
                List<Arista> vecinos = adyacencia.get(id);
                for (int i = 0; i < vecinos.size(); i++) {
                    Arista a = vecinos.get(i);
                    System.out.print(nodos.get(a.destino).nombre + " [" + a.peso + "m]");
                    if (i < vecinos.size() - 1) System.out.print(", ");
                }
                System.out.println();
            }
        }
    }

    // ─────────────────────────────────────
    // Helpers visuales para la consola
    // ─────────────────────────────────────
    static void separador() {
        System.out.println("  " + "-".repeat(56));
    }

    static void encabezado(String texto) {
        int ancho = 54;
        System.out.println("\n  ╔" + "═".repeat(ancho) + "╗");
        System.out.printf(  "  ║  %-" + (ancho-2) + "s║%n", texto);
        System.out.println("  ╚" + "═".repeat(ancho) + "╝");
    }

    // ═══════════════════════════════════════
    // MAIN
    // ═══════════════════════════════════════
    public static void main(String[] args) {

        // ── Portada ──────────────────────────────────────
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════════╗");
        System.out.println("  ║        Universidad Tecnica de Ambato - FISEI         ║");
        System.out.println("  ║        APE 4 - Grafos: Mapa del Campus UTA           ║");
        System.out.println("  ║        Estructura de Datos - 3ro B Software          ║");
        System.out.println("  ║        Autor : Luis Manobanda                        ║");
        System.out.println("  ╚══════════════════════════════════════════════════════╝");

        Grafo grafo = new Grafo();

        // ── Nodos ────────────────────────────────────────
        encabezado("PASO 1 - Agregar Nodos");
        grafo.agregarNodo("uta",        "Universidad");
        grafo.agregarNodo("fisei",      "FISEI");
        grafo.agregarNodo("idiomas",    "Idiomas");
        grafo.agregarNodo("biblioteca", "Biblioteca");
        grafo.agregarNodo("estadio",    "Estadio");
        grafo.agregarNodo("comedor",    "Comedor");

        // ── Aristas ──────────────────────────────────────
        encabezado("PASO 2 - Agregar Aristas (Rutas)");
        grafo.agregarArista("uta",        "fisei",      50);
        grafo.agregarArista("fisei",      "idiomas",    40);
        grafo.agregarArista("idiomas",    "biblioteca", 30);
        grafo.agregarArista("biblioteca", "estadio",    70);
        grafo.agregarArista("uta",        "comedor",    20);
        grafo.agregarArista("comedor",    "estadio",   200);

        // ── Lista de adyacencia ──────────────────────────
        encabezado("PASO 3 - Lista de Adyacencia");
        grafo.mostrarAdyacencia();

        // ── BFS ──────────────────────────────────────────
        encabezado("PASO 4 - BFS (Ruta con menos paradas)");
        System.out.println("  Origen  : Universidad (uta)");
        System.out.println("  Destino : Estadio (estadio)");
        separador();

        List<String> rutaBFS = grafo.bfs("uta", "estadio");

        System.out.println();
        System.out.println("  ===== BFS =====");
        System.out.println("  (Ruta con menos paradas)");
        grafo.mostrarRuta(rutaBFS);

        // ── Dijkstra ─────────────────────────────────────
        encabezado("PASO 5 - Dijkstra (Ruta con menor distancia)");
        System.out.println("  Origen  : Universidad (uta)");
        System.out.println("  Destino : Estadio (estadio)");
        separador();

        List<String> rutaDijkstra = grafo.dijkstra("uta", "estadio");

        System.out.println();
        System.out.println("  ===== DIJKSTRA =====");
        System.out.println("  (Ruta con menor distancia total)");
        grafo.mostrarRuta(rutaDijkstra);

        // ── Comparativa ──────────────────────────────────
        encabezado("PASO 6 - Comparativa Final");
        System.out.println("  Algoritmo   Paradas   Distancia");
        separador();
        System.out.printf("  %-12s %-9s %s%n", "BFS",      "2 saltos", "220 m  ->  uta -> comedor -> estadio");
        System.out.printf("  %-12s %-9s %s%n", "Dijkstra", "4 saltos", "190 m  ->  uta -> fisei -> idiomas -> biblioteca -> estadio");
        separador();
        System.out.println("  BFS      => menos paradas, mayor distancia");
        System.out.println("  Dijkstra => mas paradas,   menor distancia");
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════════╗");
        System.out.println("  ║                  Fin del programa                    ║");
        System.out.println("  ╚══════════════════════════════════════════════════════╝");
        System.out.println();
    }
}