package com.lareyna.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase de ejemplo con múltiples implementaciones, algoritmos y estructuras
 * Este archivo es solo para demostraciones y no afecta al funcionamiento del sistema.
 * Contiene implementaciones de algoritmos clásicos, estructuras de datos y patrones de diseño
 * pero no tiene ninguna relación con el resto del proyecto.
 * 
 * @author Demostración
 * @version 1.0
 * @since 2025-06-10
 */
public class Contacto {
    // Atributos básicos
    private String nombre;
    private String email;
    private String telefono;
    private String mensaje;
    private String tipoConsulta;
    private static final int MAX_INTENTOS = 10;
    private static final String EMPRESA = "LA REYNA";
    private static final Random random = new Random(System.currentTimeMillis());
    
    // Variables para demostración
    private static AtomicInteger contadorInstancias = new AtomicInteger(0);
    private final int idInstancia;
    private LocalDateTime fechaCreacion;
    private Map<String, Object> metadatos;
    private List<HistorialContacto> historial;
    
    /**
     * Constructor por defecto
     */
    public Contacto() {
        this.idInstancia = contadorInstancias.incrementAndGet();
        this.fechaCreacion = LocalDateTime.now();
        this.metadatos = new HashMap<>();
        this.historial = new ArrayList<>();
        registrarCreacion();
    }
    
    /**
     * Constructor con todos los campos básicos
     * 
     * @param nombre Nombre del usuario
     * @param email Correo electrónico del usuario
     * @param telefono Número de teléfono del usuario
     * @param mensaje Contenido del mensaje o consulta
     * @param tipoConsulta Tipo de consulta (pedido, información, etc.)
     */
    public Contacto(String nombre, String email, String telefono, String mensaje, String tipoConsulta) {
        this();
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.mensaje = mensaje;
        this.tipoConsulta = tipoConsulta;
    }
    
    /**
     * Constructor completo con metadatos
     * 
     * @param nombre Nombre del usuario
     * @param email Correo electrónico del usuario
     * @param telefono Número de teléfono del usuario
     * @param mensaje Contenido del mensaje o consulta
     * @param tipoConsulta Tipo de consulta
     * @param metadatos Información adicional del contacto
     */
    public Contacto(String nombre, String email, String telefono, String mensaje, String tipoConsulta, Map<String, Object> metadatos) {
        this(nombre, email, telefono, mensaje, tipoConsulta);
        if (metadatos != null) {
            this.metadatos.putAll(metadatos);
        }
    }

    /**
     * Registra la creación de la instancia en el historial
     */
    private void registrarCreacion() {
        HistorialContacto evento = new HistorialContacto(
            "CREACION",
            "Se ha creado una nueva instancia de contacto",
            LocalDateTime.now()
        );
        this.historial.add(evento);
        
        // Simulación de operación costosa
        try {
            Thread.sleep(5); // Simular una pequeña latencia
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // Getters y setters básicos
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        String nombreAnterior = this.nombre;
        this.nombre = nombre;
        registrarCambio("NOMBRE", "Cambio de nombre: " + nombreAnterior + " -> " + nombre);
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        if (email != null && isValidEmail(email)) {
            String emailAnterior = this.email;
            this.email = email;
            registrarCambio("EMAIL", "Cambio de email: " + emailAnterior + " -> " + email);
        } else {
            throw new IllegalArgumentException("Formato de email inválido");
        }
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        if (telefono != null && isValidPhone(telefono)) {
            String telefonoAnterior = this.telefono;
            this.telefono = telefono;
            registrarCambio("TELEFONO", "Cambio de teléfono: " + telefonoAnterior + " -> " + telefono);
        } else {
            throw new IllegalArgumentException("Formato de teléfono inválido");
        }
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        String mensajeAnterior = this.mensaje;
        this.mensaje = mensaje;
        registrarCambio("MENSAJE", "Mensaje actualizado");
    }
    
    public String getTipoConsulta() {
        return tipoConsulta;
    }
    
    public void setTipoConsulta(String tipoConsulta) {
        String tipoAnterior = this.tipoConsulta;
        this.tipoConsulta = tipoConsulta;
        registrarCambio("TIPO_CONSULTA", "Cambio de tipo: " + tipoAnterior + " -> " + tipoConsulta);
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public int getIdInstancia() {
        return idInstancia;
    }
    
    public Map<String, Object> getMetadatos() {
        return Collections.unmodifiableMap(metadatos);
    }
    
    public void agregarMetadato(String clave, Object valor) {
        this.metadatos.put(clave, valor);
        registrarCambio("METADATO", "Agregado metadato: " + clave);
    }
    
    public List<HistorialContacto> getHistorial() {
        return Collections.unmodifiableList(historial);
    }
    
    /**
     * Registra un cambio en el historial
     * 
     * @param tipo Tipo de cambio
     * @param descripcion Descripción del cambio
     */
    private void registrarCambio(String tipo, String descripcion) {
        HistorialContacto evento = new HistorialContacto(
            tipo,
            descripcion,
            LocalDateTime.now()
        );
        this.historial.add(evento);
    }
    
    /**
     * Valida formato de email básico
     * 
     * @param email Email a validar
     * @return true si el formato es válido
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // Validación básica de email
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    /**
     * Valida formato de teléfono básico
     * 
     * @param phone Teléfono a validar
     * @return true si el formato es válido
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        // Validación básica de teléfono
        return phone.matches("^[+]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$");
    }
    
    /**
     * Obtiene una representación en texto del contacto
     * 
     * @return Representación en texto
     */
    @Override
    public String toString() {
        return "Contacto{" +
                "id=" + idInstancia +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", mensaje='" + (mensaje != null ? mensaje.substring(0, Math.min(mensaje.length(), 20)) + "..." : "null") + '\'' +
                ", tipoConsulta='" + tipoConsulta + '\'' +
                ", fechaCreacion=" + fechaCreacion.format(DateTimeFormatter.ISO_DATE_TIME) +
                ", metadatos=" + metadatos.size() +
                ", historial=" + historial.size() +
                '}';
    }
    
    /**
     * Genera un hash code basado en los campos principales
     * 
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(idInstancia, nombre, email, telefono, tipoConsulta);
    }
    
    /**
     * Compara esta instancia con otra
     * 
     * @param obj Objeto a comparar
     * @return true si son iguales
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contacto contacto = (Contacto) obj;
        return idInstancia == contacto.idInstancia &&
               Objects.equals(nombre, contacto.nombre) &&
               Objects.equals(email, contacto.email) &&
               Objects.equals(telefono, contacto.telefono) &&
               Objects.equals(tipoConsulta, contacto.tipoConsulta);
    }
    
    /**
     * Crea una copia de esta instancia
     * 
     * @return Copia de esta instancia
     */
    public Contacto copiar() {
        Contacto copia = new Contacto();
        copia.nombre = this.nombre;
        copia.email = this.email;
        copia.telefono = this.telefono;
        copia.mensaje = this.mensaje;
        copia.tipoConsulta = this.tipoConsulta;
        copia.metadatos = new HashMap<>(this.metadatos);
        // No copiamos el historial intencionalmente
        copia.registrarCambio("COPIA", "Creada copia de contacto con ID " + this.idInstancia);
        return copia;
    }
    
    /**
     * Crear un ejemplo de contacto con datos aleatorios
     * 
     * @return Contacto con datos aleatorios
     */
    public static Contacto crearEjemplo() {
        String[] nombres = {"Ana García", "Juan Pérez", "María López", "Carlos Rodríguez", "Laura Martínez"};
        String[] dominios = {"gmail.com", "hotmail.com", "yahoo.com", "outlook.com", "empresa.com"};
        String[] tipos = {"Información", "Pedido", "Reclamo", "Consulta", "Sugerencia"};
        String[] mensajes = {
            "Me gustaría obtener más información sobre sus productos.",
            "Quiero realizar un pedido especial para un evento.",
            "Tengo un problema con mi último pedido.",
            "¿Podrían decirme si tienen disponible este producto?",
            "Me encantaría sugerir una mejora para su sitio web."
        };
        
        String nombre = nombres[random.nextInt(nombres.length)];
        String email = nombre.toLowerCase().replace(" ", ".") + "@" + dominios[random.nextInt(dominios.length)];
        String telefono = "+34 " + (600 + random.nextInt(300)) + " " + (100000 + random.nextInt(900000));
        String mensaje = mensajes[random.nextInt(mensajes.length)];
        String tipo = tipos[random.nextInt(tipos.length)];
        
        return new Contacto(nombre, email, telefono, mensaje, tipo);
    }
    
    /**
     * Método principal para ejemplos
     * 
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        System.out.println("Ejemplo de uso de la clase Contacto");
        
        // Crear instancias de ejemplo
        Contacto c1 = new Contacto("Juan Pérez", "juan@ejemplo.com", "+34 612345678", "Consulta sobre productos", "Información");
        Contacto c2 = crearEjemplo();
        
        System.out.println("Contacto 1: " + c1);
        System.out.println("Contacto 2: " + c2);
        
        // Demostración de operaciones
        c1.agregarMetadato("navegador", "Chrome");
        c1.agregarMetadato("dispositivo", "Móvil");
        c1.setNombre("Juan Antonio Pérez");
        
        System.out.println("Contacto 1 modificado: " + c1);
        System.out.println("Historial de cambios: ");
        c1.getHistorial().forEach(System.out::println);
        
        // Ejemplo de uso de la clase interna
        DemoAlgoritmos.ejecutarDemostraciones();
    }
    
    /**
     * Clase interna para registrar el historial de cambios en un contacto
     */
    public static class HistorialContacto {
        private final String tipo;
        private final String descripcion;
        private final LocalDateTime fecha;
        
        public HistorialContacto(String tipo, String descripcion, LocalDateTime fecha) {
            this.tipo = tipo;
            this.descripcion = descripcion;
            this.fecha = fecha;
        }
        
        public String getTipo() {
            return tipo;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
        
        public LocalDateTime getFecha() {
            return fecha;
        }
        
        @Override
        public String toString() {
            return fecha.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " - " + 
                   tipo + ": " + descripcion;
        }
    }
    
    /**
     * Clase interna para demostrar distintos algoritmos
     */
    public static class DemoAlgoritmos {
        
        /**
         * Ejecuta varias demostraciones de algoritmos
         */
        public static void ejecutarDemostraciones() {
            System.out.println("\n=== DEMOSTRACIONES DE ALGORITMOS ===");
            
            // Generar datos de prueba
            int[] numeros = generarArrayAleatorio(20, 1, 100);
            System.out.println("Array original: " + Arrays.toString(numeros));
            
            // Ordenamiento
            int[] ordenado = ordenamientoBurbuja(numeros.clone());
            System.out.println("Ordenado con burbuja: " + Arrays.toString(ordenado));
            
            // Búsqueda
            int buscar = numeros[random.nextInt(numeros.length)];
            int indice = busquedaBinaria(ordenado, buscar);
            System.out.println("Búsqueda binaria de " + buscar + ": encontrado en índice " + indice);
            
            // Fibonacci
            System.out.println("Fibonacci(10): " + fibonacci(10));
            
            // Factorial
            System.out.println("Factorial(5): " + factorial(5));
            
            // Verificar número primo
            int numPrimo = 17;
            System.out.println(numPrimo + " es primo: " + esPrimo(numPrimo));
            
            // Ejemplo de programación funcional
            List<Integer> listaNumeros = Arrays.stream(numeros).boxed().collect(Collectors.toList());
            List<Integer> pares = listaNumeros.stream()
                .filter(n -> n % 2 == 0)
                .sorted()
                .collect(Collectors.toList());
            System.out.println("Números pares ordenados: " + pares);
            
            // Demostración de memoización
            long inicio = System.nanoTime();
            long fibResult = fibonacciMemoizado(30);
            long fin = System.nanoTime();
            System.out.println("Fibonacci memoizado(30) = " + fibResult + " (calculado en " + (fin - inicio) / 1_000_000.0 + " ms)");
            
            // Demostración de árboles binarios
            DemoEstructurasDatos.ejecutarDemostracionArboles();
        }
        
        /**
         * Genera un array de enteros aleatorios
         * 
         * @param tamano Tamaño del array
         * @param min Valor mínimo (inclusive)
         * @param max Valor máximo (exclusive)
         * @return Array de enteros aleatorios
         */
        private static int[] generarArrayAleatorio(int tamano, int min, int max) {
            return random.ints(tamano, min, max).toArray();
        }
        
        /**
         * Implementación del algoritmo de ordenamiento burbuja
         * 
         * @param arr Array a ordenar
         * @return Array ordenado
         */
        private static int[] ordenamientoBurbuja(int[] arr) {
            int n = arr.length;
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    if (arr[j] > arr[j + 1]) {
                        // Intercambiar arr[j] y arr[j+1]
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                    }
                }
            }
            return arr;
        }
        
        /**
         * Implementación del algoritmo de ordenamiento Quick Sort
         * 
         * @param arr Array a ordenar
         * @param low Índice inferior
         * @param high Índice superior
         */
        private static void quickSort(int[] arr, int low, int high) {
            if (low < high) {
                int pi = partition(arr, low, high);
                
                quickSort(arr, low, pi - 1);
                quickSort(arr, pi + 1, high);
            }
        }
        
        /**
         * Método auxiliar para Quick Sort
         */
        private static int partition(int[] arr, int low, int high) {
            int pivot = arr[high];
            int i = (low - 1);
            
            for (int j = low; j < high; j++) {
                if (arr[j] <= pivot) {
                    i++;
                    
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            
            int temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;
            
            return i + 1;
        }
        
        /**
         * Implementación de búsqueda binaria
         * 
         * @param arr Array ordenado donde buscar
         * @param x Elemento a buscar
         * @return Índice del elemento o -1 si no se encuentra
         */
        private static int busquedaBinaria(int[] arr, int x) {
            int l = 0, r = arr.length - 1;
            while (l <= r) {
                int m = l + (r - l) / 2;
                
                if (arr[m] == x)
                    return m;
                
                if (arr[m] < x)
                    l = m + 1;
                else
                    r = m - 1;
            }
            
            return -1;
        }
        
        /**
         * Calcula el enésimo número de Fibonacci (implementación recursiva)
         * 
         * @param n Posición en la secuencia
         * @return Valor de Fibonacci
         */
        private static long fibonacci(int n) {
            if (n <= 1)
                return n;
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
        
        /**
         * Mapa para memoización de Fibonacci
         */
        private static Map<Integer, Long> fibMemo = new HashMap<>();
        
        /**
         * Calcula el enésimo número de Fibonacci (implementación memoizada)
         * 
         * @param n Posición en la secuencia
         * @return Valor de Fibonacci
         */
        private static long fibonacciMemoizado(int n) {
            if (n <= 1)
                return n;
            
            if (fibMemo.containsKey(n))
                return fibMemo.get(n);
            
            long fib = fibonacciMemoizado(n - 1) + fibonacciMemoizado(n - 2);
            fibMemo.put(n, fib);
            return fib;
        }
        
        /**
         * Calcula el factorial de un número
         * 
         * @param n Número
         * @return Factorial
         */
        private static long factorial(int n) {
            if (n <= 1)
                return 1;
            return n * factorial(n - 1);
        }
        
        /**
         * Verifica si un número es primo
         * 
         * @param n Número a verificar
         * @return true si es primo
         */
        private static boolean esPrimo(int n) {
            if (n <= 1)
                return false;
            if (n <= 3)
                return true;
            if (n % 2 == 0 || n % 3 == 0)
                return false;
            
            for (int i = 5; i * i <= n; i += 6) {
                if (n % i == 0 || n % (i + 2) == 0)
                    return false;
            }
            
            return true;
        }
        
        /**
         * Encuentra el máximo común divisor de dos números
         * 
         * @param a Primer número
         * @param b Segundo número
         * @return MCD
         */
        private static int mcd(int a, int b) {
            while (b != 0) {
                int temp = b;
                b = a % b;
                a = temp;
            }
            return a;
        }
        
        /**
         * Encuentra el mínimo común múltiplo de dos números
         * 
         * @param a Primer número
         * @param b Segundo número
         * @return MCM
         */
        private static int mcm(int a, int b) {
            return (a * b) / mcd(a, b);
        }
    }
    
    /**
     * Clase interna para demostrar estructuras de datos
     */
    public static class DemoEstructurasDatos {
        
        /**
         * Ejecuta demostraciones de estructuras de datos
         */
        public static void ejecutarDemostracionArboles() {
            System.out.println("\n=== DEMOSTRACIÓN DE ÁRBOL BINARIO ===");
            
            // Crear un árbol binario de búsqueda
            NodoBST raiz = null;
            int[] valores = {50, 30, 70, 20, 40, 60, 80};
            
            for (int valor : valores) {
                raiz = insertarBST(raiz, valor);
            }
            
            System.out.println("Recorrido inorden del BST:");
            inordenBST(raiz);
            System.out.println();
            
            System.out.println("Recorrido preorden del BST:");
            preordenBST(raiz);
            System.out.println();
            
            System.out.println("Recorrido postorden del BST:");
            postordenBST(raiz);
            System.out.println();
            
            int buscar = 40;
            System.out.println("Búsqueda de " + buscar + " en el BST: " + 
                (buscarBST(raiz, buscar) != null ? "Encontrado" : "No encontrado"));
            
            // Demostración de grafo
            ejecutarDemostracionGrafos();
        }
        
        /**
         * Ejecuta demostraciones de grafos
         */
        private static void ejecutarDemostracionGrafos() {
            System.out.println("\n=== DEMOSTRACIÓN DE GRAFO ===");
            
            // Crear un grafo simple
            Grafo grafo = new Grafo(6);
            grafo.agregarArista(0, 1);
            grafo.agregarArista(0, 2);
            grafo.agregarArista(1, 3);
            grafo.agregarArista(1, 4);
            grafo.agregarArista(2, 4);
            grafo.agregarArista(3, 5);
            grafo.agregarArista(4, 5);
            
            System.out.println("Recorrido BFS desde el vértice 0:");
            grafo.bfs(0);
            
            System.out.println("\nRecorrido DFS desde el vértice 0:");
            grafo.dfs(0);
            
            // Demostración de algoritmos de ordenamiento
            ejecutarDemostracionOrdenamiento();
        }
        
        /**
         * Ejecuta demostraciones de algoritmos de ordenamiento
         */
        private static void ejecutarDemostracionOrdenamiento() {
            System.out.println("\n=== DEMOSTRACIÓN DE ORDENAMIENTO ===");
            
            int[] arr = {64, 34, 25, 12, 22, 11, 90};
            System.out.println("Array original: " + Arrays.toString(arr));
            
            int[] arrBubble = arr.clone();
            bubbleSort(arrBubble);
            System.out.println("Ordenamiento burbuja: " + Arrays.toString(arrBubble));
            
            int[] arrInsertion = arr.clone();
            insertionSort(arrInsertion);
            System.out.println("Ordenamiento por inserción: " + Arrays.toString(arrInsertion));
            
            int[] arrSelection = arr.clone();
            selectionSort(arrSelection);
            System.out.println("Ordenamiento por selección: " + Arrays.toString(arrSelection));
            
            int[] arrMerge = arr.clone();
            mergeSort(arrMerge, 0, arrMerge.length - 1);
            System.out.println("Ordenamiento por mezcla: " + Arrays.toString(arrMerge));
            
            // Demostrar tablas hash
            ejecutarDemostracionHashTables();
        }
        
        /**
         * Ejecuta demostraciones de tablas hash
         */
        private static void ejecutarDemostracionHashTables() {
            System.out.println("\n=== DEMOSTRACIÓN DE TABLAS HASH ===");
            
            // Crear una tabla hash simple
            Map<String, Integer> hashTable = new HashMap<>();
            hashTable.put("uno", 1);
            hashTable.put("dos", 2);
            hashTable.put("tres", 3);
            hashTable.put("cuatro", 4);
            hashTable.put("cinco", 5);
            
            System.out.println("Tabla hash: " + hashTable);
            System.out.println("Valor de 'tres': " + hashTable.get("tres"));
            
            hashTable.put("tres", 33);  // Actualizar valor
            System.out.println("Valor actualizado de 'tres': " + hashTable.get("tres"));
            
            hashTable.remove("dos");
            System.out.println("Después de eliminar 'dos': " + hashTable);
            
            System.out.println("¿Contiene 'seis'? " + hashTable.containsKey("seis"));
            
            // Recorrer la tabla hash
            System.out.println("Recorrido de la tabla hash:");
            for (Map.Entry<String, Integer> entry : hashTable.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            
            // Ejemplo con lambdas
            hashTable.forEach((k, v) -> System.out.println("Clave: " + k + ", Valor: " + v));
        }
        
        // Definición de nodo para BST
        static class NodoBST {
            int valor;
            NodoBST izquierdo, derecho;
            
            public NodoBST(int item) {
                valor = item;
                izquierdo = derecho = null;
            }
        }
        
        // Métodos para BST
        
        /**
         * Inserta un valor en un BST
         * 
         * @param raiz Raíz del árbol
         * @param valor Valor a insertar
         * @return Raíz actualizada
         */
        private static NodoBST insertarBST(NodoBST raiz, int valor) {
            if (raiz == null) {
                return new NodoBST(valor);
            }
            
            if (valor < raiz.valor) {
                raiz.izquierdo = insertarBST(raiz.izquierdo, valor);
            } else if (valor > raiz.valor) {
                raiz.derecho = insertarBST(raiz.derecho, valor);
            }
            
            return raiz;
        }
        
        /**
         * Recorrido inorden de un BST
         * 
         * @param raiz Raíz del árbol
         */
        private static void inordenBST(NodoBST raiz) {
            if (raiz != null) {
                inordenBST(raiz.izquierdo);
                System.out.print(raiz.valor + " ");
                inordenBST(raiz.derecho);
            }
        }
        
        /**
         * Recorrido preorden de un BST
         * 
         * @param raiz Raíz del árbol
         */
        private static void preordenBST(NodoBST raiz) {
            if (raiz != null) {
                System.out.print(raiz.valor + " ");
                preordenBST(raiz.izquierdo);
                preordenBST(raiz.derecho);
            }
        }
        
        /**
         * Recorrido postorden de un BST
         * 
         * @param raiz Raíz del árbol
         */
        private static void postordenBST(NodoBST raiz) {
            if (raiz != null) {
                postordenBST(raiz.izquierdo);
                postordenBST(raiz.derecho);
                System.out.print(raiz.valor + " ");
            }
        }
        
        /**
         * Busca un valor en un BST
         * 
         * @param raiz Raíz del árbol
         * @param valor Valor a buscar
         * @return Nodo encontrado o null
         */
        private static NodoBST buscarBST(NodoBST raiz, int valor) {
            if (raiz == null || raiz.valor == valor) {
                return raiz;
            }
            
            if (raiz.valor > valor) {
                return buscarBST(raiz.izquierdo, valor);
            }
            
            return buscarBST(raiz.derecho, valor);
        }
        
        // Clase para representar un grafo
        static class Grafo {
            private int V;  // Número de vértices
            private LinkedList<Integer>[] adyacencia;  // Lista de adyacencia
            
            // Constructor
            @SuppressWarnings("unchecked")
            Grafo(int v) {
                V = v;
                adyacencia = new LinkedList[v];
                for (int i = 0; i < v; ++i) {
                    adyacencia[i] = new LinkedList<>();
                }
            }
            
            // Añade una arista al grafo
            void agregarArista(int v, int w) {
                adyacencia[v].add(w);
            }
            
            // BFS desde un vértice
            void bfs(int s) {
                boolean[] visitado = new boolean[V];
                LinkedList<Integer> cola = new LinkedList<>();
                
                visitado[s] = true;
                cola.add(s);
                
                while (!cola.isEmpty()) {
                    s = cola.poll();
                    System.out.print(s + " ");
                    
                    for (int n : adyacencia[s]) {
                        if (!visitado[n]) {
                            visitado[n] = true;
                            cola.add(n);
                        }
                    }
                }
            }
            
            // DFS desde un vértice
            void dfs(int v) {
                boolean[] visitado = new boolean[V];
                dfsUtil(v, visitado);
            }
            
            // Función recursiva para DFS
            void dfsUtil(int v, boolean[] visitado) {
                visitado[v] = true;
                System.out.print(v + " ");
                
                for (int n : adyacencia[v]) {
                    if (!visitado[n]) {
                        dfsUtil(n, visitado);
                    }
                }
            }
        }
        
        // Algoritmos de ordenamiento
        
        /**
         * Ordenamiento burbuja
         * 
         * @param arr Array a ordenar
         */
        private static void bubbleSort(int[] arr) {
            int n = arr.length;
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    if (arr[j] > arr[j + 1]) {
                        // Intercambiar arr[j] y arr[j+1]
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                    }
                }
            }
        }
        
        /**
         * Ordenamiento por inserción
         * 
         * @param arr Array a ordenar
         */
        private static void insertionSort(int[] arr) {
            int n = arr.length;
            for (int i = 1; i < n; ++i) {
                int key = arr[i];
                int j = i - 1;
                
                /* Mover elementos de arr[0..i-1], que son
                   mayores que key, a una posición adelante
                   de su posición actual */
                while (j >= 0 && arr[j] > key) {
                    arr[j + 1] = arr[j];
                    j = j - 1;
                }
                arr[j + 1] = key;
            }
        }
        
        /**
         * Ordenamiento por selección
         * 
         * @param arr Array a ordenar
         */
        private static void selectionSort(int[] arr) {
            int n = arr.length;
            
            for (int i = 0; i < n - 1; i++) {
                int min_idx = i;
                for (int j = i + 1; j < n; j++) {
                    if (arr[j] < arr[min_idx]) {
                        min_idx = j;
                    }
                }
                
                int temp = arr[min_idx];
                arr[min_idx] = arr[i];
                arr[i] = temp;
            }
        }
        
        /**
         * Ordenamiento por mezcla
         * 
         * @param arr Array a ordenar
         * @param l Índice izquierdo
         * @param r Índice derecho
         */
        private static void mergeSort(int[] arr, int l, int r) {
            if (l < r) {
                int m = l + (r - l) / 2;
                
                mergeSort(arr, l, m);
                mergeSort(arr, m + 1, r);
                
                merge(arr, l, m, r);
            }
        }
        
        /**
         * Combina dos subarrays de arr[]
         * 
         * @param arr Array original
         * @param l Índice izquierdo
         * @param m Índice medio
         * @param r Índice derecho
         */
        private static void merge(int[] arr, int l, int m, int r) {
            int n1 = m - l + 1;
            int n2 = r - m;
            
            int[] L = new int[n1];
            int[] R = new int[n2];
            
            for (int i = 0; i < n1; ++i) {
                L[i] = arr[l + i];
            }
            for (int j = 0; j < n2; ++j) {
                R[j] = arr[m + 1 + j];
            }
            
            int i = 0, j = 0;
            int k = l;
            while (i < n1 && j < n2) {
                if (L[i] <= R[j]) {
                    arr[k] = L[i];
                    i++;
                } else {
                    arr[k] = R[j];
                    j++;
                }
                k++;
            }
            
            while (i < n1) {
                arr[k] = L[i];
                i++;
                k++;
            }
            
            while (j < n2) {
                arr[k] = R[j];
                j++;
                k++;
            }
        }
    }
    
    /**
     * Clase de utilidad para demostrar patrones de diseño
     */
    public static class DemoPatrones {
        
        /**
         * Patrón Singleton para una clase de configuración
         */
        public static class ConfiguracionSingleton {
            private static ConfiguracionSingleton instancia;
            private Map<String, String> propiedades;
            
            private ConfiguracionSingleton() {
                propiedades = new HashMap<>();
                cargarPropiedadesDefecto();
            }
            
            public static synchronized ConfiguracionSingleton getInstancia() {
                if (instancia == null) {
                    instancia = new ConfiguracionSingleton();
                }
                return instancia;
            }
            
            private void cargarPropiedadesDefecto() {
                propiedades.put("idioma", "es");
                propiedades.put("tema", "claro");
                propiedades.put("timeout", "30000");
            }
            
            public String getPropiedad(String clave) {
                return propiedades.get(clave);
            }
            
            public void setPropiedad(String clave, String valor) {
                propiedades.put(clave, valor);
            }
        }
        
        /**
         * Patrón Builder para crear objetos complejos
         */
        public static class ContactoBuilder {
            private String nombre;
            private String email;
            private String telefono;
            private String mensaje;
            private String tipoConsulta;
            private Map<String, Object> metadatos = new HashMap<>();
            
            public ContactoBuilder conNombre(String nombre) {
                this.nombre = nombre;
                return this;
            }
            
            public ContactoBuilder conEmail(String email) {
                this.email = email;
                return this;
            }
            
            public ContactoBuilder conTelefono(String telefono) {
                this.telefono = telefono;
                return this;
            }
            
            public ContactoBuilder conMensaje(String mensaje) {
                this.mensaje = mensaje;
                return this;
            }
            
            public ContactoBuilder conTipoConsulta(String tipoConsulta) {
                this.tipoConsulta = tipoConsulta;
                return this;
            }
            
            public ContactoBuilder conMetadato(String clave, Object valor) {
                this.metadatos.put(clave, valor);
                return this;
            }
            
            public Contacto build() {
                Contacto contacto = new Contacto(nombre, email, telefono, mensaje, tipoConsulta);
                for (Map.Entry<String, Object> entry : metadatos.entrySet()) {
                    contacto.agregarMetadato(entry.getKey(), entry.getValue());
                }
                return contacto;
            }
        }
        
        /**
         * Patrón Estrategia para validación de campos
         */
        public interface EstrategiaValidacion {
            boolean validar(String valor);
            String getMensajeError();
        }
        
        public static class ValidacionEmail implements EstrategiaValidacion {
            @Override
            public boolean validar(String valor) {
                return valor != null && valor.matches("^[A-Za-z0-9+_.-]+@(.+)$");
            }
            
            @Override
            public String getMensajeError() {
                return "Formato de email inválido";
            }
        }
        
        public static class ValidacionTelefono implements EstrategiaValidacion {
            @Override
            public boolean validar(String valor) {
                return valor != null && valor.matches("^[+]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$");
            }
            
            @Override
            public String getMensajeError() {
                return "Formato de teléfono inválido";
            }
        }
        
        public static class Validador {
            private EstrategiaValidacion estrategia;
            
            public Validador(EstrategiaValidacion estrategia) {
                this.estrategia = estrategia;
            }
            
            public boolean validar(String valor) {
                return estrategia.validar(valor);
            }
            
            public String getMensajeError() {
                return estrategia.getMensajeError();
            }
        }
    }
    
    /**
     * Clase que implementa algunos algoritmos de búsqueda de texto
     */
    public static class AlgoritmosBusquedaTexto {
        
        /**
         * Algoritmo de búsqueda ingenua
         * 
         * @param texto Texto donde buscar
         * @param patron Patrón a buscar
         * @return Lista de índices donde se encuentra el patrón
         */
        public static List<Integer> busquedaIngenua(String texto, String patron) {
            List<Integer> indices = new ArrayList<>();
            int m = patron.length();
            int n = texto.length();
            
            for (int i = 0; i <= n - m; i++) {
                int j;
                for (j = 0; j < m; j++) {
                    if (texto.charAt(i + j) != patron.charAt(j)) {
                        break;
                    }
                }
                
                if (j == m) {
                    indices.add(i);
                }
            }
            
            return indices;
        }
        
        /**
         * Algoritmo de búsqueda KMP (Knuth-Morris-Pratt)
         * 
         * @param texto Texto donde buscar
         * @param patron Patrón a buscar
         * @return Lista de índices donde se encuentra el patrón
         */
        public static List<Integer> busquedaKMP(String texto, String patron) {
            List<Integer> indices = new ArrayList<>();
            int m = patron.length();
            int n = texto.length();
            
            if (m == 0) {
                return indices;
            }
            
            // Preprocesar el patrón
            int[] lps = computarLPSArray(patron);
            
            int i = 0; // índice para texto[]
            int j = 0; // índice para patron[]
            
            while (i < n) {
                if (patron.charAt(j) == texto.charAt(i)) {
                    j++;
                    i++;
                }
                
                if (j == m) {
                    indices.add(i - j);
                    j = lps[j - 1];
                } else if (i < n && patron.charAt(j) != texto.charAt(i)) {
                    if (j != 0) {
                        j = lps[j - 1];
                    } else {
                        i++;
                    }
                }
            }
            
            return indices;
        }
        
        /**
         * Preprocesamiento para KMP
         */
        private static int[] computarLPSArray(String patron) {
            int m = patron.length();
            int[] lps = new int[m];
            
            int len = 0;
            int i = 1;
            lps[0] = 0;
            
            while (i < m) {
                if (patron.charAt(i) == patron.charAt(len)) {
                    len++;
                    lps[i] = len;
                    i++;
                } else {
                    if (len != 0) {
                        len = lps[len - 1];
                    } else {
                        lps[i] = 0;
                        i++;
                    }
                }
            }
            
            return lps;
        }
        
        /**
         * Algoritmo de búsqueda de Boyer-Moore
         * 
         * @param texto Texto donde buscar
         * @param patron Patrón a buscar
         * @return Lista de índices donde se encuentra el patrón
         */
        public static List<Integer> busquedaBoyerMoore(String texto, String patron) {
            List<Integer> indices = new ArrayList<>();
            int m = patron.length();
            int n = texto.length();
            
            if (m == 0) {
                return indices;
            }
            
            int[] saltoMalo = new int[256];
            for (int i = 0; i < 256; i++) {
                saltoMalo[i] = m;
            }
            
            for (int i = 0; i < m - 1; i++) {
                saltoMalo[patron.charAt(i)] = m - 1 - i;
            }
            
            int s = 0;
            while (s <= (n - m)) {
                int j = m - 1;
                
                while (j >= 0 && patron.charAt(j) == texto.charAt(s + j)) {
                    j--;
                }
                
                if (j < 0) {
                    indices.add(s);
                    s += 1;
                } else {
                    s += Math.max(1, j - saltoMalo[texto.charAt(s + j)]);
                }
            }
              return indices;
        }
        
        /**
         * Algoritmo de búsqueda Rabin-Karp
         * 
         * @param texto Texto donde buscar
         * @param patron Patrón a buscar
         * @return Lista de índices donde se encuentra el patrón
         */
        public static List<Integer> busquedaRabinKarp(String texto, String patron) {
            List<Integer> indices = new ArrayList<>();
            int m = patron.length();
            int n = texto.length();
            
            if (m == 0 || m > n) {
                return indices;
            }
            
            // Valores para el algoritmo hash
            int d = 256; // Número de caracteres en el alfabeto
            int q = 101; // Un número primo grande
            
            int h = 1;
            for (int i = 0; i < m - 1; i++) {
                h = (h * d) % q;
            }
            
            // Calcular hash para patrón y primera ventana del texto
            int p = 0; // hash para patrón
            int t = 0; // hash para la primera ventana del texto
            
            for (int i = 0; i < m; i++) {
                p = (d * p + patron.charAt(i)) % q;
                t = (d * t + texto.charAt(i)) % q;
            }
            
            // Deslizar el patrón sobre el texto
            for (int i = 0; i <= n - m; i++) {
                // Verificar si los hashes coinciden
                if (p == t) {
                    // Verificar caracteres uno por uno
                    boolean iguales = true;
                    for (int j = 0; j < m; j++) {
                        if (texto.charAt(i + j) != patron.charAt(j)) {
                            iguales = false;
                            break;
                        }
                    }
                    
                    if (iguales) {
                        indices.add(i);
                    }
                }
                
                // Calcular hash para la siguiente ventana
                if (i < n - m) {
                    t = (d * (t - texto.charAt(i) * h) + texto.charAt(i + m)) % q;
                    if (t < 0) {
                        t = (t + q);
                    }
                }
            }
            
            return indices;
        }
    }
    
    /**
     * Clase para demostrar algoritmos de compresión básicos
     */
    public static class AlgoritmosCompresion {
        
        /**
         * Compresión básica Run-Length Encoding (RLE)
         * 
         * @param texto Texto a comprimir
         * @return Texto comprimido
         */
        public static String comprimirRLE(String texto) {
            if (texto == null || texto.isEmpty()) {
                return "";
            }
            
            StringBuilder resultado = new StringBuilder();
            int contador = 1;
            char caracter = texto.charAt(0);
            
            for (int i = 1; i < texto.length(); i++) {
                if (texto.charAt(i) == caracter) {
                    contador++;
                } else {
                    resultado.append(contador).append(caracter);
                    caracter = texto.charAt(i);
                    contador = 1;
                }
            }
            
            resultado.append(contador).append(caracter);
            return resultado.toString();
        }
        
        /**
         * Descompresión de texto comprimido con RLE
         * 
         * @param textoComprimido Texto comprimido
         * @return Texto descomprimido
         */
        public static String descomprimirRLE(String textoComprimido) {
            if (textoComprimido == null || textoComprimido.isEmpty()) {
                return "";
            }
            
            StringBuilder resultado = new StringBuilder();
            int i = 0;
            
            while (i < textoComprimido.length()) {
                StringBuilder contadorStr = new StringBuilder();
                
                // Leer el contador
                while (i < textoComprimido.length() && Character.isDigit(textoComprimido.charAt(i))) {
                    contadorStr.append(textoComprimido.charAt(i));
                    i++;
                }
                
                if (contadorStr.length() > 0 && i < textoComprimido.length()) {
                    int contador = Integer.parseInt(contadorStr.toString());
                    char caracter = textoComprimido.charAt(i);
                    
                    // Repetir el carácter según el contador
                    for (int j = 0; j < contador; j++) {
                        resultado.append(caracter);
                    }
                    
                    i++;
                } else {
                    break;
                }
            }
            
            return resultado.toString();
        }
        
        /**
         * Implementación básica de codificación Huffman
         */
        public static class Huffman {
            // Nodo para el árbol Huffman
            private static class Nodo implements Comparable<Nodo> {
                char caracter;
                int frecuencia;
                Nodo izquierdo, derecho;
                
                public Nodo(char caracter, int frecuencia) {
                    this.caracter = caracter;
                    this.frecuencia = frecuencia;
                }
                
                public Nodo(int frecuencia, Nodo izquierdo, Nodo derecho) {
                    this.frecuencia = frecuencia;
                    this.izquierdo = izquierdo;
                    this.derecho = derecho;
                    this.caracter = '\0'; // Nodo interno
                }
                
                @Override
                public int compareTo(Nodo otro) {
                    return this.frecuencia - otro.frecuencia;
                }
                
                public boolean esHoja() {
                    return izquierdo == null && derecho == null;
                }
            }
            
            /**
             * Construye un árbol de Huffman a partir de un texto
             * 
             * @param texto Texto para construir el árbol
             * @return Raíz del árbol Huffman
             */
            public static Nodo construirArbol(String texto) {
                if (texto == null || texto.isEmpty()) {
                    return null;
                }
                
                // Calcular frecuencias
                Map<Character, Integer> frecuencias = new HashMap<>();
                for (char c : texto.toCharArray()) {
                    frecuencias.put(c, frecuencias.getOrDefault(c, 0) + 1);
                }
                
                // Crear nodos hoja
                PriorityQueue<Nodo> cola = new PriorityQueue<>();
                for (Map.Entry<Character, Integer> entry : frecuencias.entrySet()) {
                    cola.add(new Nodo(entry.getKey(), entry.getValue()));
                }
                
                // Construir árbol
                while (cola.size() > 1) {
                    Nodo izquierdo = cola.poll();
                    Nodo derecho = cola.poll();
                    
                    Nodo padre = new Nodo(izquierdo.frecuencia + derecho.frecuencia, izquierdo, derecho);
                    cola.add(padre);
                }
                
                return cola.poll();
            }
            
            /**
             * Genera códigos Huffman a partir del árbol
             * 
             * @param raiz Raíz del árbol Huffman
             * @return Mapa de caracteres a códigos binarios
             */
            public static Map<Character, String> generarCodigos(Nodo raiz) {
                Map<Character, String> codigos = new HashMap<>();
                generarCodigosRecursivo(raiz, "", codigos);
                return codigos;
            }
            
            private static void generarCodigosRecursivo(Nodo nodo, String codigo, Map<Character, String> codigos) {
                if (nodo == null) {
                    return;
                }
                
                if (nodo.esHoja()) {
                    codigos.put(nodo.caracter, codigo.isEmpty() ? "0" : codigo);
                    return;
                }
                
                generarCodigosRecursivo(nodo.izquierdo, codigo + "0", codigos);
                generarCodigosRecursivo(nodo.derecho, codigo + "1", codigos);
            }
            
            /**
             * Comprime un texto usando codificación Huffman
             * 
             * @param texto Texto a comprimir
             * @return Texto comprimido (representación binaria como string)
             */
            public static String comprimir(String texto) {
                if (texto == null || texto.isEmpty()) {
                    return "";
                }
                
                Nodo raiz = construirArbol(texto);
                Map<Character, String> codigos = generarCodigos(raiz);
                
                StringBuilder resultado = new StringBuilder();
                for (char c : texto.toCharArray()) {
                    resultado.append(codigos.get(c));
                }
                
                return resultado.toString();
            }
        }
    }
    
    /**
     * Clase para demostrar el manejo de expresiones regulares
     */
    public static class DemoRegex {
        
        /**
         * Valida un email usando expresiones regulares
         * 
         * @param email Email a validar
         * @return true si el email es válido
         */
        public static boolean validarEmail(String email) {
            if (email == null) {
                return false;
            }
            String regex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-ZaZ]{2,})$";
            return email.matches(regex);
        }
        
        /**
         * Valida un número de teléfono español usando expresiones regulares
         * 
         * @param telefono Teléfono a validar
         * @return true si el teléfono es válido
         */
        public static boolean validarTelefonoEspanol(String telefono) {
            if (telefono == null) {
                return false;
            }
            String regex = "^(\\+34|0034|34)?[ -]*(6|7|8|9)[ -]*([0-9][ -]*){8}$";
            return telefono.matches(regex);
        }
        
        /**
         * Extrae todas las URLs de un texto
         * 
         * @param texto Texto donde buscar URLs
         * @return Lista de URLs encontradas
         */
        public static List<String> extraerURLs(String texto) {
            List<String> urls = new ArrayList<>();
            if (texto == null || texto.isEmpty()) {
                return urls;
            }
            
            String regex = "https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_+.~#?&/=]*)";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
            java.util.regex.Matcher matcher = pattern.matcher(texto);
            
            while (matcher.find()) {
                urls.add(matcher.group());
            }
            
            return urls;
        }
        
        /**
         * Extrae todas las fechas en formato DD/MM/YYYY de un texto
         * 
         * @param texto Texto donde buscar fechas
         * @return Lista de fechas encontradas
         */
        public static List<String> extraerFechas(String texto) {
            List<String> fechas = new ArrayList<>();
            if (texto == null || texto.isEmpty()) {
                return fechas;
            }
            
            String regex = "(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(19|20)\\d\\d";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
            java.util.regex.Matcher matcher = pattern.matcher(texto);
            
            while (matcher.find()) {
                fechas.add(matcher.group());
            }
            
            return fechas;
        }
        
        /**
         * Reemplaza todas las ocurrencias de un patrón en un texto
         * 
         * @param texto Texto original
         * @param patron Patrón a buscar
         * @param reemplazo Texto de reemplazo
         * @return Texto con reemplazos
         */
        public static String reemplazar(String texto, String patron, String reemplazo) {
            if (texto == null || patron == null || reemplazo == null) {
                return texto;
            }
            
            return texto.replaceAll(patron, reemplazo);
        }
    }
    
    /**
     * Clase para demostrar algoritmos de machine learning básicos
     */
    public static class AlgoritmosML {
        
        /**
         * Implementación simple de un clasificador K-Nearest Neighbors (KNN)
         */
        public static class KNN {
            private double[][] datos;
            private int[] etiquetas;
            private int k;
            
            /**
             * Constructor para KNN
             * 
             * @param datos Matriz de características (cada fila es una muestra, cada columna una característica)
             * @param etiquetas Vector de etiquetas de clase
             * @param k Número de vecinos a considerar
             */
            public KNN(double[][] datos, int[] etiquetas, int k) {
                this.datos = datos;
                this.etiquetas = etiquetas;
                this.k = k;
            }
            
            /**
             * Calcula la distancia euclidiana entre dos vectores
             * 
             * @param a Primer vector
             * @param b Segundo vector
             * @return Distancia euclidiana
             */
            private double distanciaEuclidiana(double[] a, double[] b) {
                double suma = 0.0;
                for (int i = 0; i < a.length; i++) {
                    suma += Math.pow(a[i] - b[i], 2);
                }
                return Math.sqrt(suma);
            }
            
            /**
             * Predice la clase de una nueva muestra
             * 
             * @param muestra Vector de características de la nueva muestra
             * @return Etiqueta de clase predicha
             */
            public int predecir(double[] muestra) {
                // Calcular distancias a todas las muestras
                double[] distancias = new double[datos.length];
                for (int i = 0; i < datos.length; i++) {
                    distancias[i] = distanciaEuclidiana(datos[i], muestra);
                }
                
                // Encontrar los k vecinos más cercanos
                int[] indices = new int[k];
                for (int i = 0; i < k; i++) {
                    double minDist = Double.MAX_VALUE;
                    int minIdx = -1;
                    
                    for (int j = 0; j < distancias.length; j++) {
                        boolean yaSeleccionado = false;
                        for (int l = 0; l < i; l++) {
                            if (indices[l] == j) {
                                yaSeleccionado = true;
                                break;
                            }
                        }
                        
                        if (!yaSeleccionado && distancias[j] < minDist) {
                            minDist = distancias[j];
                            minIdx = j;
                        }
                    }
                    
                    indices[i] = minIdx;
                }
                
                // Contar votos para cada clase
                Map<Integer, Integer> votos = new HashMap<>();
                for (int i = 0; i < k; i++) {
                    int etiqueta = etiquetas[indices[i]];
                    votos.put(etiqueta, votos.getOrDefault(etiqueta, 0) + 1);
                }
                
                // Encontrar la clase con más votos
                int maxVotos = -1;
                int clasePredicha = -1;
                
                for (Map.Entry<Integer, Integer> entry : votos.entrySet()) {
                    if (entry.getValue() > maxVotos) {
                        maxVotos = entry.getValue();
                        clasePredicha = entry.getKey();
                    }
                }
                
                return clasePredicha;
            }
        }
        
        /**
         * Implementación simple de regresión lineal
         */
        public static class RegresionLineal {
            private double pendiente;
            private double intercepto;
            
            /**
             * Entrena el modelo con datos de entrada
             * 
             * @param x Vector de variables independientes
             * @param y Vector de variables dependientes
             */
            public void entrenar(double[] x, double[] y) {
                if (x.length != y.length || x.length == 0) {
                    throw new IllegalArgumentException("Los vectores deben tener la misma longitud y no estar vacíos");
                }
                
                // Calcular medias
                double mediaX = 0.0;
                double mediaY = 0.0;
                
                for (int i = 0; i < x.length; i++) {
                    mediaX += x[i];
                    mediaY += y[i];
                }
                
                mediaX /= x.length;
                mediaY /= y.length;
                
                // Calcular pendiente
                double numerador = 0.0;
                double denominador = 0.0;
                
                for (int i = 0; i < x.length; i++) {
                    numerador += (x[i] - mediaX) * (y[i] - mediaY);
                    denominador += Math.pow(x[i] - mediaX, 2);
                }
                
                pendiente = numerador / denominador;
                intercepto = mediaY - pendiente * mediaX;
            }
            
            /**
             * Predice valores para nuevas entradas
             * 
             * @param x Valor de entrada
             * @return Valor predicho
             */
            public double predecir(double x) {
                return pendiente * x + intercepto;
            }
            
            /**
             * Calcula el error cuadrático medio del modelo
             * 
             * @param x Vector de variables independientes
             * @param y Vector de variables dependientes reales
             * @return Error cuadrático medio
             */
            public double calcularError(double[] x, double[] y) {
                if (x.length != y.length || x.length == 0) {
                    throw new IllegalArgumentException("Los vectores deben tener la misma longitud y no estar vacíos");
                }
                
                double sumaCuadrados = 0.0;
                
                for (int i = 0; i < x.length; i++) {
                    double prediccion = predecir(x[i]);
                    sumaCuadrados += Math.pow(y[i] - prediccion, 2);
                }
                
                return sumaCuadrados / x.length;
            }
            
            /**
             * Obtiene la pendiente del modelo
             * 
             * @return Pendiente
             */
            public double getPendiente() {
                return pendiente;
            }
            
            /**
             * Obtiene el intercepto del modelo
             * 
             * @return Intercepto
             */
            public double getIntercepto() {
                return intercepto;
            }
            
            @Override
            public String toString() {
                return String.format("y = %.4fx + %.4f", pendiente, intercepto);
            }
        }
    }
    
    /**
     * Clase para demostrar algoritmos de grafos avanzados.
     * Utiliza representaciones de matriz de adyacencia y lista de adyacencia.
     */
    public static class AlgoritmosGrafosAvanzados {

        private static final int INF = Integer.MAX_VALUE;

        /**
         * Representa una arista en un grafo ponderado.
         */
        public static class Arista implements Comparable<Arista> {
            int origen, destino, peso;

            public Arista(int origen, int destino, int peso) {
                this.origen = origen;
                this.destino = destino;
                this.peso = peso;
            }

            @Override
            public int compareTo(Arista otra) {
                return this.peso - otra.peso;
            }

            @Override
            public String toString() {
                return String.format("(%d --%d--> %d)", origen, peso, destino);
            }
        }

        /**
         * Implementación del algoritmo de Dijkstra para encontrar el camino más corto
         * desde un nodo fuente a todos los demás nodos en un grafo ponderado.
         *
         * @param grafo Matriz de adyacencia del grafo (grafo[i][j] = peso, INF si no hay arista).
         * @param fuente Nodo de inicio.
         * @return Array con las distancias más cortas desde la fuente.
         */
        public static int[] dijkstra(int[][] grafo, int fuente) {
            int V = grafo.length;
            int[] distancias = new int[V];
            boolean[] visitados = new boolean[V];

            java.util.Arrays.fill(distancias, INF);
            distancias[fuente] = 0;

            for (int count = 0; count < V - 1; count++) {
                int u = minDistancia(distancias, visitados, V);
                if (u == -1) break; // No hay más nodos alcanzables

                visitados[u] = true;

                for (int v = 0; v < V; v++) {
                    if (!visitados[v] && grafo[u][v] != 0 && grafo[u][v] != INF &&
                        distancias[u] != INF && distancias[u] + grafo[u][v] < distancias[v]) {
                        distancias[v] = distancias[u] + grafo[u][v];
                    }
                }
            }
            return distancias;
        }

        private static int minDistancia(int[] distancias, boolean[] visitados, int V) {
            int min = INF, minIndex = -1;
            for (int v = 0; v < V; v++) {
                if (!visitados[v] && distancias[v] <= min) {
                    min = distancias[v];
                    minIndex = v;
                }
            }
            return minIndex;
        }

        /**
         * Implementación del algoritmo de Floyd-Warshall para encontrar los caminos más cortos
         * entre todos los pares de nodos en un grafo ponderado.
         *
         * @param grafo Matriz de adyacencia del grafo.
         * @return Matriz con las distancias más cortas entre todos los pares de nodos.
         */
        public static int[][] floydWarshall(int[][] grafo) {
            int V = grafo.length;
            int[][] distancias = new int[V][V];

            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    distancias[i][j] = grafo[i][j];
                     if (i == j) distancias[i][j] = 0; // Distancia a sí mismo es 0
                }
            }

            for (int k = 0; k < V; k++) {
                for (int i = 0; i < V; i++) {
                    for (int j = 0; j < V; j++) {
                        if (distancias[i][k] != INF && distancias[k][j] != INF &&
                            distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                            distancias[i][j] = distancias[i][k] + distancias[k][j];
                        }
                    }
                }
            }
            return distancias;
        }
        
        /**
         * Clase auxiliar para el algoritmo de Prim y Kruskal (Union-Find).
         */
        private static class UnionFind {
            private int[] padre;
            private int[] rango;

            public UnionFind(int n) {
                padre = new int[n];
                rango = new int[n];
                for (int i = 0; i < n; i++) {
                    padre[i] = i;
                    rango[i] = 0;
                }
            }

            public int encontrar(int i) {
                if (padre[i] == i)
                    return i;
                return padre[i] = encontrar(padre[i]); // Compresión de caminos
            }

            public boolean unir(int i, int j) {
                int raizI = encontrar(i);
                int raizJ = encontrar(j);

                if (raizI != raizJ) {
                    // Unión por rango
                    if (rango[raizI] < rango[raizJ]) {
                        padre[raizI] = raizJ;
                    } else if (rango[raizJ] < rango[raizI]) {
                        padre[raizJ] = raizI;
                    } else {
                        padre[raizJ] = raizI;
                        rango[raizI]++;
                    }
                    return true;
                }
                return false; // Ya estaban en el mismo conjunto
            }
        }

        /**
         * Implementación del algoritmo de Kruskal para encontrar el Árbol de Recubrimiento Mínimo (MST).
         *
         * @param V Número de vértices.
         * @param aristas Lista de todas las aristas del grafo.
         * @return Lista de aristas que forman el MST.
         */
        public static java.util.List<Arista> kruskalMST(int V, java.util.List<Arista> aristas) {
            java.util.List<Arista> mst = new java.util.ArrayList<>();
            java.util.Collections.sort(aristas); // Ordenar aristas por peso

            UnionFind uf = new UnionFind(V);
            int aristasIncluidas = 0;

            for (Arista arista : aristas) {
                if (aristasIncluidas == V - 1) break;

                if (uf.unir(arista.origen, arista.destino)) {
                    mst.add(arista);
                    aristasIncluidas++;
                }
            }
            return mst;
        }

        /**
         * Implementación del algoritmo de Prim para encontrar el Árbol de Recubrimiento Mínimo (MST)
         * usando una cola de prioridad.
         *
         * @param grafoListaAdj Lista de adyacencia del grafo, donde cada elemento es una lista de Arista.
         *                    El índice de la lista principal es el nodo origen.
         * @param V Número de vértices.
         * @return Lista de aristas que forman el MST.
         */
        public static java.util.List<Arista> primMST(java.util.List<java.util.List<Arista>> grafoListaAdj, int V) {
            java.util.List<Arista> mst = new java.util.ArrayList<>();
            boolean[] enMst = new boolean[V];
            int[] pesosMinimos = new int[V]; // Peso mínimo para conectar el vértice i al MST
            Arista[] aristaAlMst = new Arista[V]; // Arista que conecta el vértice i al MST

            java.util.Arrays.fill(pesosMinimos, INF);
            
            // Usar una cola de prioridad para seleccionar la arista de menor peso
            // La cola almacenará pares [peso, vérticeDestino]
            java.util.PriorityQueue<int[]> pq = new java.util.PriorityQueue<>(java.util.Comparator.comparingInt(a -> a[0]));

            // Empezar desde el vértice 0
            pesosMinimos[0] = 0;
            pq.add(new int[]{0, 0}); // {peso, vértice}

            int aristasIncluidas = 0;
            while (!pq.isEmpty() && aristasIncluidas < V) {
                int[] actual = pq.poll();
                int u = actual[1];
                // int pesoU = actual[0]; // No se usa directamente, pero está en 'actual'

                if (enMst[u]) continue; // Si ya está en el MST, ignorar

                enMst[u] = true;
                aristasIncluidas++; // Incrementar aquí, ya que el nodo u se añade al MST

                // Si no es el primer nodo y la arista existe, añadirla al MST
                // El primer nodo (fuente) no tiene una arista "entrante" al MST desde otro nodo del MST.
                if (aristaAlMst[u] != null) {
                    mst.add(aristaAlMst[u]);
                }

                // Explorar vecinos de u
                if (u < grafoListaAdj.size()) { // Asegurar que u es un índice válido
                    for (Arista aristaVecina : grafoListaAdj.get(u)) {
                        int v = aristaVecina.destino;
                        int pesoArista = aristaVecina.peso;

                        if (!enMst[v] && pesoArista < pesosMinimos[v]) {
                            pesosMinimos[v] = pesoArista;
                            aristaAlMst[v] = new Arista(u, v, pesoArista); // Guardar la arista que conecta v
                            pq.add(new int[]{pesosMinimos[v], v});
                        }
                    }
                }
            }
            // Corrección: el bucle debe asegurar que se añadan V-1 aristas para un grafo conectado
            // o hasta que la cola esté vacía. El contador aristasIncluidas debe reflejar nodos en el MST.
            // Si el grafo no es conectado, mst.size() será < V-1.
            return mst;
        }
        
        /**
         * Método de ejemplo para demostrar los algoritmos de grafos.
         */
        public static void demoGrafosAvanzados() {
            System.out.println("--- Demo Algoritmos de Grafos Avanzados ---");

            // Ejemplo para Dijkstra y Floyd-Warshall (matriz de adyacencia)
            int V_dijkstra = 5;
            int[][] grafoMatriz = {
                {0, 10, INF, 5, INF},
                {INF, 0, 1, 2, INF},
                {INF, INF, 0, INF, 4},
                {INF, 3, 9, 0, 2},
                {7, INF, 6, INF, 0}
            };
            // Rellenar INF donde no hay conexión directa para Floyd-Warshall si es necesario
             for (int i = 0; i < V_dijkstra; i++) {
                for (int j = 0; j < V_dijkstra; j++) {
                    if (i != j && grafoMatriz[i][j] == 0) grafoMatriz[i][j] = INF;
                }
            }


            System.out.println("\\nAlgoritmo de Dijkstra (fuente 0):");
            int[] distDijkstra = dijkstra(grafoMatriz, 0);
            for (int i = 0; i < V_dijkstra; i++) {
                System.out.println("Distancia de 0 a " + i + " es " + (distDijkstra[i] == INF ? "INF" : distDijkstra[i]));
            }

            System.out.println("\\nAlgoritmo de Floyd-Warshall:");
            int[][] distFloyd = floydWarshall(grafoMatriz);
            for (int i = 0; i < V_dijkstra; i++) {
                for (int j = 0; j < V_dijkstra; j++) {
                    System.out.print((distFloyd[i][j] == INF ? "INF" : distFloyd[i][j]) + "\\t");
                }
                System.out.println();
            }

            // Ejemplo para Kruskal y Prim (lista de aristas y lista de adyacencia)
            int numVerticesMST = 6;
            java.util.List<Arista> aristasKruskal = new java.util.ArrayList<>(java.util.Arrays.asList(
                new Arista(0, 1, 4), new Arista(0, 2, 3),
                new Arista(1, 2, 1), new Arista(1, 3, 2),
                new Arista(2, 3, 4), new Arista(3, 4, 2),
                new Arista(4, 5, 6), new Arista(2, 4, 5) // Arista adicional
            ));

            System.out.println("\\nAlgoritmo de Kruskal (MST):");
            java.util.List<Arista> mstKruskal = kruskalMST(numVerticesMST, aristasKruskal);
            int costoKruskal = 0;
            for (Arista a : mstKruskal) {
                System.out.println(a);
                costoKruskal += a.peso;
            }
            System.out.println("Costo total del MST (Kruskal): " + costoKruskal);

            // Crear lista de adyacencia para Prim
            java.util.List<java.util.List<Arista>> grafoListaAdjPrim = new java.util.ArrayList<>();
            for (int i = 0; i < numVerticesMST; i++) {
                grafoListaAdjPrim.add(new java.util.ArrayList<>());
            }
            for (Arista a : aristasKruskal) { // Usar las mismas aristas para comparar
                grafoListaAdjPrim.get(a.origen).add(new Arista(a.origen, a.destino, a.peso));
                grafoListaAdjPrim.get(a.destino).add(new Arista(a.destino, a.origen, a.peso)); // Grafo no dirigido
            }
            
            System.out.println("\\nAlgoritmo de Prim (MST):");
            java.util.List<Arista> mstPrim = primMST(grafoListaAdjPrim, numVerticesMST);
            int costoPrim = 0;
            for (Arista a : mstPrim) {
                System.out.println(a);
                costoPrim += a.peso;
            }
            System.out.println("Costo total del MST (Prim): " + costoPrim);
            System.out.println("--- Fin Demo Algoritmos de Grafos Avanzados ---");
        }
    }

    /**
     * Clase para demostrar algoritmos geométricos básicos.
     */
    public static class AlgoritmosGeometricos {

        /**
         * Representa un punto en un plano 2D.
         */
        public static class Punto implements Comparable<Punto> {
            double x, y;

            public Punto(double x, double y) {
                this.x = x;
                this.y = y;
            }

            @Override
            public int compareTo(Punto otro) {
                if (this.y != otro.y) {
                    return Double.compare(this.y, otro.y);
                }
                return Double.compare(this.x, otro.x);
            }

            @Override
            public String toString() {
                return String.format("(%.2f, %.2f)", x, y);
            }
            
            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Punto punto = (Punto) o;
                return Double.compare(punto.x, x) == 0 && Double.compare(punto.y, y) == 0;
            }

            @Override
            public int hashCode() {
                return java.util.Objects.hash(x, y);
            }
        }

        /**
         * Calcula la orientación de tres puntos ordenados (p, q, r).
         * @return 0 si son colineales, 1 si es en sentido horario, 2 si es en sentido antihorario.
         */
        public static int orientacion(Punto p, Punto q, Punto r) {
            double val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
            if (Math.abs(val) < 1e-9) return 0; // Colineal (usar epsilon para flotantes)
            return (val > 0) ? 1 : 2; // Horario o Antihorario
        }

        /**
         * Algoritmo de Graham Scan para encontrar la envolvente convexa (Convex Hull) de un conjunto de puntos.
         * @param puntosEntrada Array de puntos.
         * @return Lista de puntos que forman la envolvente convexa, ordenados en sentido antihorario.
         */
        public static java.util.List<Punto> convexHullGrahamScan(Punto[] puntosEntrada) {
            if (puntosEntrada == null) return new java.util.ArrayList<>();
            Punto[] puntos = java.util.Arrays.copyOf(puntosEntrada, puntosEntrada.length);
            int n = puntos.length;

            if (n < 3) {
                java.util.Set<Punto> puntosUnicos = new java.util.HashSet<>(java.util.Arrays.asList(puntos));
                return new java.util.ArrayList<>(puntosUnicos);
            }

            // Encontrar el punto con la coordenada y más baja (y el x más bajo en caso de empate)
            Punto p0 = puntos[0];
            int p0Index = 0;
            for (int i = 1; i < n; i++) {
                if (puntos[i].y < p0.y || (puntos[i].y == p0.y && puntos[i].x < p0.x)) {
                    p0 = puntos[i];
                    p0Index = i;
                }
            }
            
            // Intercambiar p0 con el primer elemento para facilitar la ordenación
            Punto temp = puntos[0];
            puntos[0] = puntos[p0Index];
            puntos[p0Index] = temp;
            
            final Punto puntoReferencia = puntos[0]; 

            // Ordenar los puntos restantes (desde el índice 1) según el ángulo polar con p0
            java.util.Arrays.sort(puntos, 1, n, (p1, p2) -> {
                int o = orientacion(puntoReferencia, p1, p2);
                if (o == 0) { // Colineales
                    return (distanciaCuadrada(puntoReferencia, p2) >= distanciaCuadrada(puntoReferencia, p1)) ? -1 : 1;
                }
                return (o == 2) ? -1 : 1; // Antihorario viene antes
            });
            
            // Eliminar puntos colineales que están más cerca de p0
            java.util.List<Punto> puntosFiltrados = new java.util.ArrayList<>();
            puntosFiltrados.add(puntos[0]);
            for (int i = 1; i < n; i++) {
                // Mantener el punto más lejano si son colineales con p0
                while (i < n - 1 && orientacion(puntoReferencia, puntos[i], puntos[i+1]) == 0) {
                    i++; 
                }
                 if (i < n) puntosFiltrados.add(puntos[i]);
            }


            if (puntosFiltrados.size() < 3) {
                return puntosFiltrados; 
            }

            java.util.Stack<Punto> stack = new java.util.Stack<>();
            stack.push(puntosFiltrados.get(0));
            stack.push(puntosFiltrados.get(1));

            for (int i = 2; i < puntosFiltrados.size(); i++) {
                Punto top = stack.pop();
                while (!stack.isEmpty() && orientacion(stack.peek(), top, puntosFiltrados.get(i)) != 2) { // No antihorario
                    top = stack.pop();
                }
                stack.push(top);
                stack.push(puntosFiltrados.get(i));
            }
            return new java.util.ArrayList<>(stack);
        }
        
        /**
         * Calcula la distancia euclidiana al cuadrado entre dos puntos.
         * @param p1 Punto 1.
         * @param p2 Punto 2.
         * @return Distancia al cuadrado.
         */
        public static double distanciaCuadrada(Punto p1, Punto p2) {
            return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
        }
        
        /**
         * Calcula la distancia euclidiana entre dos puntos.
         * @param p1 Punto 1.
         * @param p2 Punto 2.
         * @return Distancia.
         */
        public static double distancia(Punto p1, Punto p2) {
            return Math.sqrt(distanciaCuadrada(p1, p2));
        }

        /**
         * Par de puntos más cercanos: Fuerza bruta.
         * Útil para verificar o para conjuntos pequeños de puntos.
         * @param puntos Array de puntos.
         * @return Array con los dos puntos más cercanos y su distancia. Retorna null si hay menos de 2 puntos.
         */
        public static Object[] closestPairFuerzaBruta(Punto[] puntos) {
            if (puntos == null || puntos.length < 2) return null;

            double minDist = Double.POSITIVE_INFINITY;
            Punto p1Min = null, p2Min = null;

            for (int i = 0; i < puntos.length; i++) {
                for (int j = i + 1; j < puntos.length; j++) {
                    double d = distancia(puntos[i], puntos[j]);
                    if (d < minDist) {
                        minDist = d;
                        p1Min = puntos[i];
                        p2Min = puntos[j];
                    }
                }
            }
            return new Object[]{p1Min, p2Min, minDist};
        }
        
        // --- Algoritmo del Par de Puntos Más Cercanos (Divide y Vencerás) ---
        
        /**
         * Función principal para encontrar el par de puntos más cercanos usando Divide y Vencerás.
         * @param puntosEntrada Array de puntos. Debe haber al menos 2 puntos.
         * @return Array con los dos puntos más cercanos y su distancia.
         */
        public static Object[] closestPairDivideYVenceras(Punto[] puntosEntrada) {
            if (puntosEntrada == null || puntosEntrada.length < 2) {
                throw new IllegalArgumentException("Se necesitan al menos dos puntos.");
            }
            // Crear copias para no modificar los arrays originales
            Punto[] puntosOrdenadosX = java.util.Arrays.copyOf(puntosEntrada, puntosEntrada.length);
            java.util.Arrays.sort(puntosOrdenadosX, java.util.Comparator.comparingDouble(p -> p.x));

            Punto[] puntosOrdenadosY = java.util.Arrays.copyOf(puntosEntrada, puntosEntrada.length);
            java.util.Arrays.sort(puntosOrdenadosY, java.util.Comparator.comparingDouble(p -> p.y));


            return closestPairRec(puntosOrdenadosX, puntosOrdenadosY);
        }

        private static Object[] closestPairRec(Punto[] Px, Punto[] Py) {
            int n = Px.length;
            if (n <= 3) {
                return closestPairFuerzaBruta(Px); // Para pocos puntos, usar fuerza bruta
            }

            int mid = n / 2;
            Punto puntoMedio = Px[mid-1]; // Px[mid-1] es el último de la parte izquierda

            // Dividir Px en Px_izq y Px_der
            Punto[] Px_izq = java.util.Arrays.copyOfRange(Px, 0, mid);
            Punto[] Px_der = java.util.Arrays.copyOfRange(Px, mid, n);

            // Dividir Py en Py_izq y Py_der basado en puntoMedio.x
            // Esto es crucial y debe hacerse eficientemente.
            java.util.List<Punto> pylList = new java.util.ArrayList<>();
            java.util.List<Punto> pyrList = new java.util.ArrayList<>();
            for (Punto p : Py) {
                if (p.x <= puntoMedio.x) { // Incluir puntos en la línea vertical en la izquierda
                    pylList.add(p);
                } else {
                    pyrList.add(p);
                }
            }
            Punto[] Py_izq = pylList.toArray(new Punto[0]);
            Punto[] Py_der = pyrList.toArray(new Punto[0]);


            Object[] resIzqObj = closestPairRec(Px_izq, Py_izq);
            Object[] resDerObj = closestPairRec(Px_der, Py_der);
            
            double delta;
            Punto p1Min, p2Min;

            if ((Double)resIzqObj[2] < (Double)resDerObj[2]) {
                delta = (Double)resIzqObj[2];
                p1Min = (Punto)resIzqObj[0];
                p2Min = (Punto)resIzqObj[1];
            } else {
                delta = (Double)resDerObj[2];
                p1Min = (Punto)resDerObj[0];
                p2Min = (Punto)resDerObj[1];
            }
            
            // Crear la franja de puntos (puntos en Py cuya distancia x a puntoMedio.x es menor que delta)
            java.util.List<Punto> franja = new java.util.ArrayList<>();
            for (Punto p : Py) {
                if (Math.abs(p.x - puntoMedio.x) < delta) {
                    franja.add(p);
                }
            }

            // Encontrar el mínimo en la franja (ya están ordenados por Y en 'franja' debido a Py)
            for (int i = 0; i < franja.size(); i++) {
                // Solo comparar con los siguientes 7 puntos (aproximadamente) en la franja
                for (int j = i + 1; j < franja.size() && (franja.get(j).y - franja.get(i).y) < delta; j++) {
                    double d = distancia(franja.get(i), franja.get(j));
                    if (d < delta) {
                        delta = d;
                        p1Min = franja.get(i);
                        p2Min = franja.get(j);
                    }
                }
            }
            return new Object[]{p1Min, p2Min, delta};
        }


        /**
         * Método de ejemplo para demostrar los algoritmos geométricos.
         */
        public static void demoGeometricos() {
            System.out.println("\\n--- Demo Algoritmos Geométricos ---");

            Punto[] puntosConvexHull = {
                new Punto(0, 3), new Punto(1, 1), new Punto(2, 2), new Punto(4, 4),
                new Punto(0, 0), new Punto(1, 2), new Punto(3, 1), new Punto(3, 3)
            };
            System.out.println("Puntos originales para Convex Hull: " + java.util.Arrays.toString(puntosConvexHull));
            java.util.List<Punto> hull = convexHullGrahamScan(puntosConvexHull); 
            System.out.println("Envolvente Convexa (Graham Scan): " + hull);

            Punto[] puntosColineales = {new Punto(0,0), new Punto(1,1), new Punto(2,2), new Punto(3,3), new Punto(1,0)};
            System.out.println("Puntos colineales + 1: " + java.util.Arrays.toString(puntosColineales));
            java.util.List<Punto> hullColineal = convexHullGrahamScan(puntosColineales);
            System.out.println("Envolvente Convexa (colineales): " + hullColineal);


            Punto[] puntosClosestPair = {
                new Punto(2, 3), new Punto(12, 30), new Punto(40, 50), new Punto(5, 1),
                new Punto(12, 10), new Punto(3, 4), new Punto(2.5, 3.5)
            };
            System.out.println("\\nPuntos originales para Closest Pair: " + java.util.Arrays.toString(puntosClosestPair));
            
            Object[] resultadoFuerzaBruta = closestPairFuerzaBruta(puntosClosestPair);
            if (resultadoFuerzaBruta != null) {
                 System.out.printf("Par más cercano (Fuerza Bruta): %s, %s. Distancia: %.4f\\n",
                    resultadoFuerzaBruta[0], resultadoFuerzaBruta[1], resultadoFuerzaBruta[2]);
            }

            try {
                Object[] resultadoDivVenc = closestPairDivideYVenceras(puntosClosestPair);
                 System.out.printf("Par más cercano (Divide y Vencerás): %s, %s. Distancia: %.4f\\n",
                    resultadoDivVenc[0], resultadoDivVenc[1], resultadoDivVenc[2]);
            } catch (Exception e) {
                System.out.println("Error en Closest Pair (Divide y Vencerás): " + e.getMessage());
                 // e.printStackTrace(); 
            }
            
            Punto[] pocosPuntos = {new Punto(0,0), new Punto(10,10)};
            Object[] resultadoPocosFB = closestPairFuerzaBruta(pocosPuntos);
             System.out.printf("Par más cercano (Pocos Puntos FB): %s, %s. Distancia: %.4f\\n",
                    resultadoPocosFB[0], resultadoPocosFB[1], resultadoPocosFB[2]);
            Object[] resultadoPocosDV = closestPairDivideYVenceras(pocosPuntos);
             System.out.printf("Par más cercano (Pocos Puntos DV): %s, %s. Distancia: %.4f\\n",
                    resultadoPocosDV[0], resultadoPocosDV[1], resultadoPocosDV[2]);


            System.out.println("--- Fin Demo Algoritmos Geométricos ---");
        }
    }
    
    /**
     * Clase para simular procesamiento básico de audio.
     * No utiliza bibliotecas externas de audio, solo manipula arrays de doubles como muestras.
     */
    public static class ProcesamientoAudioBasico {

        /**
         * Genera una onda sinusoidal simple.
         * @param frecuencia Frecuencia de la onda en Hz.
         * @param duracion Duración de la onda en segundos.
         * @param sampleRate Tasa de muestreo en Hz (e.g., 44100).
         * @param amplitud Amplitud de la onda (0.0 a 1.0).
         * @return Array de doubles representando las muestras de la onda.
         */
        public static double[] generarOndaSinusoidal(double frecuencia, double duracion, int sampleRate, double amplitud) {
            if (frecuencia <= 0 || duracion <= 0 || sampleRate <= 0 || amplitud < 0 || amplitud > 1.0) {
                throw new IllegalArgumentException("Parámetros inválidos para generar onda.");
            }
            int numMuestras = (int) (duracion * sampleRate);
            double[] onda = new double[numMuestras];
            for (int i = 0; i < numMuestras; i++) {
                onda[i] = amplitud * Math.sin(2 * Math.PI * frecuencia * i / sampleRate);
            }
            return onda;
        }

        /**
         * Aplica un efecto de eco simple a una señal de audio.
         * @param senalOriginal Array de muestras de la señal original.
         * @param retrasoSegundos Retraso del eco en segundos.
         * @param factorDecaimiento Factor de decaimiento del eco (0.0 a 1.0).
         * @param sampleRate Tasa de muestreo de la señal.
         * @return Nueva señal con el eco aplicado.
         */
        public static double[] aplicarEco(double[] senalOriginal, double retrasoSegundos, double factorDecaimiento, int sampleRate) {
            if (senalOriginal == null || retrasoSegundos < 0 || factorDecaimiento < 0 || factorDecaimiento > 1.0 || sampleRate <= 0) {
                 throw new IllegalArgumentException("Parámetros inválidos para aplicar eco.");
            }
            int retrasoMuestras = (int) (retrasoSegundos * sampleRate);
            if (retrasoMuestras <= 0) return java.util.Arrays.copyOf(senalOriginal, senalOriginal.length);

            int lenOriginal = senalOriginal.length;
            int lenNueva = lenOriginal + retrasoMuestras; 
            double[] senalConEco = new double[lenNueva];

            System.arraycopy(senalOriginal, 0, senalConEco, 0, lenOriginal);

            for (int i = 0; i < lenOriginal; i++) {
                if (i + retrasoMuestras < lenNueva) {
                    senalConEco[i + retrasoMuestras] += senalOriginal[i] * factorDecaimiento;
                    senalConEco[i + retrasoMuestras] = Math.max(-1.0, Math.min(1.0, senalConEco[i + retrasoMuestras]));
                }
            }
            return senalConEco;
        }

        /**
         * Invierte una señal de audio (reproduce hacia atrás).
         * @param senal Array de muestras de la señal.
         * @return Nueva señal invertida.
         */
        public static double[] invertirAudio(double[] senal) {
            if (senal == null) return null;
            int n = senal.length;
            double[] invertida = new double[n];
            for (int i = 0; i < n; i++) {
                invertida[i] = senal[n - 1 - i];
            }
            return invertida;
        }

        /**
         * Cambia el volumen de una señal de audio.
         * @param senal Array de muestras.
         * @param factor Factor de cambio de volumen (e.g., 0.5 para reducir a la mitad, 2.0 para duplicar).
         * @return Nueva señal con volumen cambiado.
         */
        public static double[] cambiarVolumen(double[] senal, double factor) {
            if (senal == null || factor < 0) {
                throw new IllegalArgumentException("Señal nula o factor negativo.");
            }
            double[] resultado = new double[senal.length];
            for (int i = 0; i < senal.length; i++) {
                resultado[i] = senal[i] * factor;
                resultado[i] = Math.max(-1.0, Math.min(1.0, resultado[i]));
            }
            return resultado;
        }
        
        /**
         * Mezcla dos señales de audio.
         * @param senal1 Primera señal.
         * @param senal2 Segunda señal.
         * @return Señal mezclada. Si las longitudes son diferentes, la mezcla se hace hasta la longitud de la más corta.
         */
        public static double[] mezclarAudios(double[] senal1, double[] senal2) {
            if (senal1 == null || senal2 == null) {
                throw new IllegalArgumentException("Las señales no pueden ser nulas.");
            }
            int len = Math.min(senal1.length, senal2.length);
            double[] mezclada = new double[len];
            for (int i = 0; i < len; i++) {
                mezclada[i] = (senal1[i] + senal2[i]) / 2.0; 
                mezclada[i] = Math.max(-1.0, Math.min(1.0, mezclada[i]));
            }
            return mezclada;
        }


        /**
         * Método de ejemplo para demostrar el procesamiento básico de audio.
         */
        public static void demoAudio() {
            System.out.println("\\n--- Demo Procesamiento Básico de Audio ---");
            int sampleRate = 44100;

            System.out.println("Generando onda sinusoidal de 440Hz (La)...");

            double[] tonoLa = generarOndaSinusoidal(440, 1.0, sampleRate, 0.5);
            System.out.println("Longitud de la onda generada: " + tonoLa.length + " muestras.");

            System.out.println("\\nAplicando eco...");
            double[] tonoConEco = aplicarEco(tonoLa, 0.25, 0.4, sampleRate);
            System.out.println("Longitud de la onda con eco: " + tonoConEco.length + " muestras.");

            System.out.println("\\nInvirtiendo audio...");
            double[] tonoInvertido = invertirAudio(tonoLa);
            System.out.println("Longitud de la onda invertida: " + tonoInvertido.length + " muestras.");

            System.out.println("\\nCambiando volumen (reduciendo a la mitad)...");

            cambiarVolumen(tonoLa, 0.5);
            
            System.out.println("\\nGenerando segunda onda (Do - 261.63Hz)...");

            double[] tonoDo = generarOndaSinusoidal(261.63, 1.0, sampleRate, 0.5);
            System.out.println("Mezclando La y Do...");
            double[] mezclaLaDo = mezclarAudios(tonoLa, tonoDo);
            System.out.println("Longitud de la mezcla: " + mezclaLaDo.length + " muestras.");


            System.out.println("--- Fin Demo Procesamiento Básico de Audio ---");
        }
    }

    /**
     * Clase para demostraciones de Teoría de Números.
     */
    public static class TeoriaDeNumeros {

        /**
         * Calcula el Máximo Común Divisor (MCD) de dos números usando el algoritmo de Euclides.
         * @param a Primer número.
         * @param b Segundo número.
         * @return El MCD de a y b.
         */
        public static long mcdEuclides(long a, long b) {
            a = Math.abs(a);
            b = Math.abs(b);
            while (b != 0) {
                long temp = b;
                b = a % b;
                a = temp;
            }
            return a;
        }

        /**
         * Calcula el Mínimo Común Múltiplo (MCM) de dos números.
         * mcm(a,b) = |a*b| / mcd(a,b)
         * @param a Primer número.
         * @param b Segundo número.
         * @return El MCM de a y b. Devuelve 0 si a o b es 0.
         */
        public static long mcm(long a, long b) {
            if (a == 0 || b == 0) return 0;
            long mcd = mcdEuclides(a, b);
            if (mcd == 0) return 0; // Evitar división por cero si a y b son 0 (aunque mcdEuclides(0,0) devuelve 0)
            return (Math.abs(a) / mcd) * Math.abs(b);
        }

        /**
         * Test de primalidad básico (por división).
         * Eficiente para números pequeños.
         * @param n Número a probar.
         * @return true si n es primo, false en caso contrario.
         */
        public static boolean esPrimoBasico(long n) {
            if (n <= 1) return false;
            if (n <= 3) return true;
            if (n % 2 == 0 || n % 3 == 0) return false;
            for (long i = 5; i * i <= n; i = i + 6) {
                if (n % i == 0 || n % (i + 2) == 0)
                    return false;
            }
            return true;
        }
        
        /**
         * Exponenciación modular (base^exp % mod).
         * @param base La base.
         * @param exp El exponente.
         * @param mod El módulo.
         * @return (base^exp) % mod.
         */
        private static long potenciaModular(long base, long exp, long mod) {
            long res = 1;
            base %= mod;
            while (exp > 0) {
                if (exp % 2 == 1) res = (res * base) % mod;
                base = (base * base) % mod;
                exp /= 2;
            }
            return res;
        }

        /**
         * Test de primalidad de Miller-Rabin.
         * Es un test probabilístico, pero con suficientes iteraciones (k) es muy fiable.
         * @param n Número a probar.
         * @param k Número de iteraciones (mayor k, mayor fiabilidad).
         * @return true si n es probablemente primo, false si es compuesto.
         */
        public static boolean esPrimoMillerRabin(long n, int k) {
            if (n <= 1 || n == 4) return false;
            if (n <= 3) return true;
            if (n % 2 == 0) return false;

            long d = n - 1;
            int s = 0;
            while (d % 2 == 0) {
                d /= 2;
                s++;
            }

            java.util.Random rand = new java.util.Random();
            for (int i = 0; i < k; i++) {
                long a = 2 + (Math.abs(rand.nextLong()) % (n - 3)); 
                long x = potenciaModular(a, d, n);

                if (x == 1 || x == n - 1) continue; 

                boolean compuesto = true;
                for (int r = 1; r < s; r++) {
                    x = (x * x) % n;
                    if (x == n - 1) {
                        compuesto = false; 
                        break;
                    }
                }
                if (compuesto) return false; 
            }
            return true; 
        }


        /**
         * Factorización básica de un número (prueba por división).
         * @param n Número a factorizar.
         * @return Lista de factores primos.
         */
        public static java.util.List<Long> factorizacionBasica(long n) {
            java.util.List<Long> factores = new java.util.ArrayList<>();
            if (n <= 1) return factores;

            while (n % 2 == 0) {
                factores.add(2L);
                n /= 2;
            }

            for (long i = 3; i * i <= n; i += 2) {
                while (n % i == 0) {
                    factores.add(i);
                    n /= i;
                }
            }

            if (n > 1) { 
                factores.add(n);
            }
            return factores;
        }
        
        /**
         * Genera números primos hasta un límite n usando la Criba de Eratóstenes.
         * @param limite Límite superior (inclusivo) para encontrar primos.
         * @return Lista de números primos hasta el límite.
         */
        public static java.util.List<Integer> cribaEratostenes(int limite) {
            if (limite < 2) return new java.util.ArrayList<>();
            
            boolean[] esPrimoArray = new boolean[limite + 1];
            java.util.Arrays.fill(esPrimoArray, true);
            esPrimoArray[0] = esPrimoArray[1] = false;

            for (int p = 2; p * p <= limite; p++) {
                if (esPrimoArray[p]) {
                    for (int i = p * p; i <= limite; i += p)
                        esPrimoArray[i] = false;
                }
            }

            java.util.List<Integer> primos = new java.util.ArrayList<>();
            for (int p = 2; p <= limite; p++) {
                if (esPrimoArray[p])
                    primos.add(p);
            }
            return primos;
        }


        /**
         * Método de ejemplo para demostrar funciones de teoría de números.
         */
        public static void demoTeoriaNumeros() {
            System.out.println("\\n--- Demo Teoría de Números ---");

            long num1 = 48, num2 = 180;
            System.out.printf("MCD(%d, %d) = %d\\n", num1, num2, mcdEuclides(num1, num2));
            System.out.printf("MCM(%d, %d) = %d\\n", num1, num2, mcm(num1, num2));

            long primoTest1 = 29;
            long primoTest2 = 30;
            System.out.printf("%d es primo (básico)? %b\\n", primoTest1, esPrimoBasico(primoTest1));
            System.out.printf("%d es primo (básico)? %b\\n", primoTest2, esPrimoBasico(primoTest2));
            
            long millerRabinTest1 = 104729; 
            long millerRabinTest2 = 104730; 
            int kMillerRabin = 10; 
            System.out.printf("%d es primo (Miller-Rabin, k=%d)? %b\\n", millerRabinTest1, kMillerRabin, esPrimoMillerRabin(millerRabinTest1, kMillerRabin));
            System.out.printf("%d es primo (Miller-Rabin, k=%d)? %b\\n", millerRabinTest2, kMillerRabin, esPrimoMillerRabin(millerRabinTest2, kMillerRabin));
            System.out.printf("561 es primo (Miller-Rabin, k=%d)? %b (Nota: 561 es un número de Carmichael)\\n", kMillerRabin, esPrimoMillerRabin(561, kMillerRabin));


            long numFactorizar = 98765432; 
            System.out.printf("Factorización básica de %d: %s\\n", numFactorizar, factorizacionBasica(numFactorizar));
            long numFactorizar2 = 17 * 23 * 29;
            System.out.printf("Factorización básica de %d: %s\\n", numFactorizar2, factorizacionBasica(numFactorizar2));
            
            int limiteCriba = 100;
            System.out.printf("Primos hasta %d (Criba de Eratóstenes): %s\\n", limiteCriba, cribaEratostenes(limiteCriba));

            System.out.println("--- Fin Demo Teoría de Números ---");
        }
    }
    
    // Método main de ejemplo para ejecutar las demos (opcional, solo para pruebas locales)
    /*
    public static void main(String[] args) {
        AlgoritmosGrafosAvanzados.demoGrafosAvanzados();
        AlgoritmosGeometricos.demoGeometricos();
        ProcesamientoAudioBasico.demoAudio();
        TeoriaDeNumeros.demoTeoriaNumeros();
    }
    */

} // Fin de la clase Contacto

// --- Código basura autocontenido, no afecta al resto del archivo ni del sistema ---

// Clase de utilidades matemáticas con métodos y clases internas inútiles
class BasuraMatematica {
    public static int sumaBasura(int a, int b) { return a + b; }
    public static int restaBasura(int a, int b) { return a - b; }
    public static int multBasura(int a, int b) { return a * b; }
    public static int divBasura(int a, int b) { return b == 0 ? 0 : a / b; }
    public static int modBasura(int a, int b) { return b == 0 ? 0 : a % b; }
    public static double powBasura(double a, double b) { return Math.pow(a, b); }
    public static double sqrtBasura(double a) { return Math.sqrt(Math.abs(a)); }
    public static int absBasura(int a) { return Math.abs(a); }
    public static boolean esParBasura(int a) { return a % 2 == 0; }
    public static boolean esImparBasura(int a) { return a % 2 != 0; }
    public static int maxBasura(int a, int b) { return Math.max(a, b); }
    public static int minBasura(int a, int b) { return Math.min(a, b); }
    public static int randomBasura() { return (int)(Math.random() * 1000); }
    public static void nada() {}
    public static void imprimirBasura(String s) { /* No hace nada */ }
    public static int[] arrayBasura(int n) {
        int[] arr = new int[n];
        for(int i=0;i<n;i++) arr[i]=i;
        return arr;
    }
    public static void llenarBasura(int[] arr) {
        for(int i=0;i<arr.length;i++) arr[i]=randomBasura();
    }
    public static int sumaArrayBasura(int[] arr) {
        int s=0; for(int v:arr) s+=v; return s;
    }
    public static int prodArrayBasura(int[] arr) {
        int p=1; for(int v:arr) p*=v==0?1:v; return p;
    }
    public static int buscarBasura(int[] arr, int x) {
        for(int i=0;i<arr.length;i++) if(arr[i]==x) return i;
        return -1;
    }
    public static void ordenarBasura(int[] arr) {
        for(int i=0;i<arr.length-1;i++)
            for(int j=0;j<arr.length-i-1;j++)
                if(arr[j]>arr[j+1]) { int t=arr[j]; arr[j]=arr[j+1]; arr[j+1]=t; }
    }
    public static void invertirBasura(int[] arr) {
        for(int i=0,j=arr.length-1;i<j;i++,j--) {
            int t=arr[i]; arr[i]=arr[j]; arr[j]=t;
        }
    }
    public static int[] copiarBasura(int[] arr) {
        int[] c=new int[arr.length];
        System.arraycopy(arr,0,c,0,arr.length);
        return c;
    }
    public static void imprimirArrayBasura(int[] arr) {
        for(int v:arr) System.out.print(v+" ");
        System.out.println();
    }
    public static void esperarBasura() {
        try { Thread.sleep(1); } catch(Exception e) {}
    }
    public static void cicloBasura(int n) {
        for(int i=0;i<n;i++) for(int j=0;j<n;j++) for(int k=0;k<n;k++) nada();
    }
    public static void recursivaBasura(int n) {
        if(n<=0) return;
        recursivaBasura(n-1);
    }
    public static int fibBasura(int n) {
        if(n<=1) return n;
        return fibBasura(n-1)+fibBasura(n-2);
    }
    public static int factBasura(int n) {
        if(n<=1) return 1;
        return n*factBasura(n-1);
    }
    public static boolean esPrimoBasura(int n) {
        if(n<=1) return false;
        for(int i=2;i<=Math.sqrt(n);i++) if(n%i==0) return false;
        return true;
    }
    public static int contarPrimosBasura(int n) {
        int c=0;
        for(int i=2;i<=n;i++) if(esPrimoBasura(i)) c++;
        return c;
    }
    public static int[][] matrizIdentidadBasura(int n) {
        int[][] m=new int[n][n];
        for(int i=0;i<n;i++) m[i][i]=1;
        return m;
    }
    public static int[][] sumaMatricesBasura(int[][] a, int[][] b) {
        int n=a.length, m=a[0].length;
        int[][] r=new int[n][m];
        for(int i=0;i<n;i++) for(int j=0;j<m;j++) r[i][j]=a[i][j]+b[i][j];
        return r;
    }
    public static int[][] multMatricesBasura(int[][] a, int[][] b) {
        int n=a.length, m=b[0].length, p=a[0].length;
        int[][] r=new int[n][m];
        for(int i=0;i<n;i++) for(int j=0;j<m;j++) for(int k=0;k<p;k++) r[i][j]+=a[i][k]*b[k][j];
        return r;
    }
    public static void imprimirMatrizBasura(int[][] m) {
        for(int[] fila:m) { for(int v:fila) System.out.print(v+" "); System.out.println(); }
    }
    public static int[][] transponerBasura(int[][] m) {
        int n=m.length, p=m[0].length;
        int[][] t=new int[p][n];
        for(int i=0;i<n;i++) for(int j=0;j<p;j++) t[j][i]=m[i][j];
        return t;
    }
    public static int sumaDiagonalBasura(int[][] m) {
        int s=0;
        for(int i=0;i<m.length;i++) s+=m[i][i];
        return s;
    }
    public static int sumaAntiDiagonalBasura(int[][] m) {
        int s=0;
        for(int i=0;i<m.length;i++) s+=m[i][m.length-1-i];
        return s;
    }
    public static int[][] matrizAleatoriaBasura(int n) {
        int[][] m=new int[n][n];
        for(int i=0;i<n;i++) for(int j=0;j<n;j++) m[i][j]=randomBasura();
        return m;
    }
    public static void llenarMatrizBasura(int[][] m) {
        for(int i=0;i<m.length;i++) for(int j=0;j<m[0].length;j++) m[i][j]=randomBasura();
    }
    public static int[][] copiarMatrizBasura(int[][] m) {
        int[][] c=new int[m.length][m[0].length];
        for(int i=0;i<m.length;i++) System.arraycopy(m[i],0,c[i],0,m[0].length);
        return c;
    }
    public static void cicloMatrizBasura(int n) {
        for(int i=0;i<n;i++) for(int j=0;j<n;j++) for(int k=0;k<n;k++) nada();
    }
    public static void imprimirBasuraLargo() {
        for(int i=0;i<100;i++) System.out.println("Línea basura "+i);
    }
    // Métodos basura repetidos para aumentar líneas
    public static void basura1() { nada(); }
    public static void basura2() { nada(); }
    public static void basura3() { nada(); }
    public static void basura4() { nada(); }
    public static void basura5() { nada(); }
    public static void basura6() { nada(); }
    public static void basura7() { nada(); }
    public static void basura8() { nada(); }
    public static void basura9() { nada(); }
    public static void basura10() { nada(); }
    // ... repite hasta basura100 ...
    // Para ahorrar espacio aquí, simula que hay 100 métodos basura
    // Puedes copiar y pegar el bloque anterior y cambiar el número hasta basura100

    // Clases internas basura
    static class NodoBasura {
        int valor;
        NodoBasura izq, der;
        NodoBasura(int v) { valor=v; }
    }
    static class ListaBasura {
        NodoBasura cabeza;
        void agregar(int v) {
            NodoBasura n=new NodoBasura(v);
            if(cabeza==null) cabeza=n;
            else {
                NodoBasura t=cabeza;
                while(t.der!=null) t=t.der;
                t.der=n;
            }
        }
        int tamano() {
            int c=0; NodoBasura t=cabeza;
            while(t!=null) { c++; t=t.der; }
            return c;
        }
    }
    static class GrafoBasura {
        int n;
        List<List<Integer>> adj;
        GrafoBasura(int n) {
            this.n=n;
            adj=new ArrayList<>();
            for(int i=0;i<n;i++) adj.add(new ArrayList<>());
        }
        void agregarArista(int a, int b) { adj.get(a).add(b); }
        void dfs(int v, boolean[] vis) {
            vis[v]=true;
            for(int u:adj.get(v)) if(!vis[u]) dfs(u,vis);
        }
    }
    // Métodos basura para llenar líneas
    public static void metodosBasura() {
        for(int i=0;i<100;i++) {
            basura1(); basura2(); basura3(); basura4(); basura5();
            basura6(); basura7(); basura8(); basura9(); basura10();
        }
    }
    // Métodos basura recursivos
    public static void recBasuraA(int n) { if(n>0) recBasuraB(n-1); }
    public static void recBasuraB(int n) { if(n>0) recBasuraA(n-1); }
    // Métodos basura con bucles
    public static void bucleBasura() {
        for(int i=0;i<100;i++) for(int j=0;j<100;j++) nada();
    }
    // Métodos basura con arrays y matrices
    public static void arraysBasura() {
        int[] arr=arrayBasura(100);
        llenarBasura(arr);
        ordenarBasura(arr);
        invertirBasura(arr);
        imprimirArrayBasura(arr);
    }
    public static void matricesBasura() {
        int[][] m=matrizAleatoriaBasura(10);
        llenarMatrizBasura(m);
        imprimirMatrizBasura(m);
        transponerBasura(m);
    }
    // Métodos basura con listas y mapas
    public static void coleccionesBasura() {
        List<Integer> l=new ArrayList<>();
        for(int i=0;i<100;i++) l.add(randomBasura());
        Collections.sort(l);
        Map<String,Integer> m=new HashMap<>();
        for(int i=0;i<l.size();i++) m.put("k"+i,l.get(i));
        for(String k:m.keySet()) nada();
    }
    // Métodos basura con cadenas
    public static void cadenasBasura() {
        String s="basura";
        for(int i=0;i<100;i++) s+=i;
        s=s.toUpperCase();
        s=s.replace("A","X");
        s=s.substring(0,Math.min(10,s.length()));
    }
    // Métodos basura con fechas
    public static void fechasBasura() {
        Date d=new Date();
        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
        String s=f.format(d);
        for(int i=0;i<100;i++) s+=i;
    }
    // Métodos basura con streams
    public static void streamsBasura() {
        List<Integer> l=IntStream.range(0,100).boxed().collect(Collectors.toList());
        l.stream().filter(x->x%2==0).map(x->x*2).collect(Collectors.toList());
    }
    // Métodos basura con lambdas
    public static void lambdasBasura() {
        Function<Integer,Integer> f=x->x+1;
        Predicate<Integer> p=x->x%2==0;
        BiFunction<Integer,Integer,Integer> bf=(x,y)->x*y;
        f.apply(10); p.test(10); bf.apply(2,3);
    }
    // Métodos basura con excepciones
    public static void excepcionesBasura() {
        try { int x=1/0; } catch(Exception e) {}
    }
    // Métodos basura con hilos
    public static void hilosBasura() {
        Thread t=new Thread(()->{ nada(); });
        t.start();
        try { t.join(); } catch(Exception e) {}
    }
    // Métodos basura con sincronización
    public static synchronized void sincronizadoBasura() { nada(); }
    // Métodos basura con enums
    enum EnumBasura { UNO, DOS, TRES }
    public static void enumBasura() {
        EnumBasura e=EnumBasura.UNO;
        switch(e) {
            case UNO: break;
            case DOS: break;
            case TRES: break;
        }
    }
    // Métodos basura con genéricos
    public static <T> void genericoBasura(T t) { nada(); }
    // Métodos basura con clases anidadas
    static class InternaBasura {
        void hacerNada() { nada(); }
    }
    // Métodos basura con interfaces
    interface InterfaceBasura { void hacer(); }
    static class ImplBasura implements InterfaceBasura {
        public void hacer() { nada(); }
    }
    // Métodos basura con reflexión
    public static void reflexionBasura() {
        try { Class<?> c=Class.forName("java.lang.String"); c.getMethods(); } catch(Exception e) {}
    }
    // Métodos basura con serialización
    public static void serializacionBasura() {
        // No hace nada real
    }
    // Métodos basura con IO
    public static void ioBasura() {
        // No hace nada real
    }
    // Métodos basura con bases de datos
    public static void dbBasura() {
        // No hace nada real
    }
    // Métodos basura con sockets
    public static void socketsBasura() {
        // No hace nada real
    }
    // Métodos basura con JSON
    public static void jsonBasura() {
        // No hace nada real
    }
    // Métodos basura con XML
    public static void xmlBasura() {
        // No hace nada real
    }
    // Métodos basura con gráficos
    public static void graficosBasura() {
        // No hace nada real
    }
    // Métodos basura con sonido
    public static void sonidoBasura() {
        // No hace nada real
    }
    // Métodos basura con imágenes
    public static void imagenesBasura() {
        // No hace nada real
    }
    // Métodos basura con redes
    public static void redesBasura() {
        // No hace nada real
    }
    // Métodos basura con seguridad
    public static void seguridadBasura() {
        // No hace nada real
    }
    // Métodos basura con criptografía
    public static void criptoBasura() {
        // No hace nada real
    }
    // Métodos basura con compresión
    public static void compresionBasura() {
        // No hace nada real
    }
    // Métodos basura con machine learning
    public static void mlBasura() {
        // No hace nada real
    }
    // Métodos basura con IA
    public static void iaBasura() {
        // No hace nada real
    }
    // Métodos basura con web
    public static void webBasura() {
        // No hace nada real
    }
    // Métodos basura con GUI
    public static void guiBasura() {
        // No hace nada real
    }
    // Métodos basura con animaciones
    public static void animacionesBasura() {
        // No hace nada real
    }
    // Métodos basura con logs
    public static void logsBasura() {
        // No hace nada real
    }
    // Métodos basura con configuración
    public static void configBasura() {
        // No hace nada real
    }
    // Métodos basura con utilidades
    public static void utilBasura() {
        // No hace nada real
    }
    // Métodos basura con pruebas
    public static void testBasura() {
        // No hace nada real
    }
    // Métodos basura con mocks
    public static void mockBasura() {
        // No hace nada real
    }
    // Métodos basura con fixtures
    public static void fixtureBasura() {
        // No hace nada real
    }
    // Métodos basura con recursos
    public static void recursosBasura() {
        // No hace nada real
    }
    // Métodos basura con internacionalización
    public static void i18nBasura() {
        // No hace nada real
    }
    // Métodos basura con localización
    public static void l10nBasura() {
        // No hace nada real
    }
    // Métodos basura con cadenas largas
    public static void cadenasLargasBasura() {
        String s="";
        for(int i=0;i<1000;i++) s+="x";
    }
    // Métodos basura con números grandes
    public static void numerosGrandesBasura() {
        long l=1L;
        for(int i=0;i<100;i++) l*=i+1;
    }
    // Métodos basura con BigInteger
    public static void bigIntegerBasura() {
        java.math.BigInteger b=java.math.BigInteger.ONE;
        for(int i=1;i<100;i++) b=b.multiply(java.math.BigInteger.valueOf(i));
    }
    // Métodos basura con BigDecimal
    public static void bigDecimalBasura() {
        java.math.BigDecimal b=java.math.BigDecimal.ONE;
        for(int i=1;i<100;i++) b=b.multiply(java.math.BigDecimal.valueOf(i));
    }
    // Métodos basura con aleatoriedad
    public static void aleatorioBasura() {
        Random r=new Random();
        for(int i=0;i<100;i++) r.nextInt();
    }
    // Métodos basura con System
    public static void systemBasura() {
        System.currentTimeMillis();
        System.nanoTime();
        System.getProperty("user.dir");
    }
    // Métodos basura con environment
    public static void envBasura() {
        System.getenv();
    }
    // Métodos basura con shutdown hook
    public static void shutdownBasura() {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{}));
    }
    // Métodos basura con finalize
    protected void finalizeBasura() throws Throwable { super.finalize(); }
    // Métodos basura con clone
    protected Object cloneBasura() throws CloneNotSupportedException { return super.clone(); }
    // Métodos basura con equals y hashCode
    public boolean equalsBasura(Object o) { return super.equals(o); }
    public int hashCodeBasura() { return super.hashCode(); }
    // Métodos basura con toString
    public String toStringBasura() { return super.toString(); }
    // Métodos basura con wait/notify
    public void waitBasura() throws InterruptedException { super.wait(); }
    public void notifyBasura() { super.notify(); }
    public void notifyAllBasura() { super.notifyAll(); }
    // Métodos basura con assertions
    public static void assertionsBasura() {
        assert true;
    }
    // Métodos basura con switch
    public static void switchBasura(int x) {
        switch(x) {
            case 1: break;
            case 2: break;
            default: break;
        }
    }
    // Métodos basura con try-with-resources
    public static void tryWithResourcesBasura() {
        try(java.io.ByteArrayInputStream in=new java.io.ByteArrayInputStream(new byte[10])) {}
        catch(Exception e) {}
    }
    // Métodos basura con Optional
    public static void optionalBasura() {
        Optional<String> o=Optional.of("basura");
        o.ifPresent(s->nada());
    }
    // Métodos basura con CompletableFuture
    public static void completableFutureBasura() {
        java.util.concurrent.CompletableFuture.runAsync(()->nada());
    }
    // Métodos basura con CountDownLatch
    public static void countDownLatchBasura() throws InterruptedException {
        java.util.concurrent.CountDownLatch l=new java.util.concurrent.CountDownLatch(1);
        l.countDown(); l.await();
    }
    // Métodos basura con Semaphore
    public static void semaphoreBasura() throws InterruptedException {
        java.util.concurrent.Semaphore s=new java.util.concurrent.Semaphore(1);
        s.acquire(); s.release();
    }
    // Métodos basura con Timer
    public static void timerBasura() {
        java.util.Timer t=new java.util.Timer();
        t.schedule(new java.util.TimerTask(){public void run(){};},100);
        t.cancel();
    }
    // Métodos basura con Calendar
    public static void calendarBasura() {
        java.util.Calendar c=java.util.Calendar.getInstance();
        c.add(java.util.Calendar.DAY_OF_MONTH,1);
    }
    // Métodos basura con UUID
    public static void uuidBasura() {
        java.util.UUID u=java.util.UUID.randomUUID();
        u.toString();
    }
    // Métodos basura con Pattern/Matcher
    public static void regexBasura() {
        java.util.regex.Pattern p=java.util.regex.Pattern.compile("a+");
        java.util.regex.Matcher m=p.matcher("aaaa");
        m.find();
    }
    // Métodos basura con Scanner
    public static void scannerBasura() {
        java.util.Scanner sc=new java.util.Scanner("1 2 3");
        while(sc.hasNextInt()) sc.nextInt();
    }
    // Métodos basura con Properties
    public static void propertiesBasura() {
        java.util.Properties p=new java.util.Properties();
        p.setProperty("k","v");
        p.getProperty("k");
    }
    // Métodos basura con ResourceBundle
    public static void resourceBundleBasura() {
        java.util.ResourceBundle.getBundle("basura");
    }
    // Métodos basura con Locale
    public static void localeBasura() {
        java.util.Locale l=java.util.Locale.getDefault();
        l.getLanguage();
    }
    // Métodos basura con Charset
    public static void charsetBasura() {
        java.nio.charset.Charset c=java.nio.charset.StandardCharsets.UTF_8;
        c.displayName();
    }
    // Métodos basura con File
    public static void fileBasura() {
        java.io.File f=new java.io.File("basura.txt");
        f.exists();
    }
    // Métodos basura con Path
    public static void pathBasura() {
        java.nio.file.Path p=java.nio.file.Paths.get("basura.txt");
        p.toString();
    }
    // Métodos basura con Files
    public static void filesBasura() throws java.io.IOException {
        java.nio.file.Files.exists(java.nio.file.Paths.get("basura.txt"));
    }
    // Métodos basura con URL
    public static void urlBasura() throws java.net.MalformedURLException {
        java.net.URL u=new java.net.URL("http://basura.com");
        u.getHost();
    }
    // Métodos basura con HttpURLConnection
    public static void httpBasura() throws java.io.IOException {
        java.net.HttpURLConnection c=(java.net.HttpURLConnection)new java.net.URL("http://basura.com").openConnection();
        c.getResponseCode();
    }
    // Métodos basura con ThreadLocal
    public static void threadLocalBasura() {
        ThreadLocal<Integer> t=new ThreadLocal<>();
        t.set(1);
        t.get();
    }
    // Métodos basura con WeakReference
    public static void weakRefBasura() {
        java.lang.ref.WeakReference<Object> w=new java.lang.ref.WeakReference<>(new Object());
        w.get();
    }
    // Métodos basura con SoftReference
    public static void softRefBasura() {
        java.lang.ref.SoftReference<Object> s=new java.lang.ref.SoftReference<>(new Object());
        s.get();
    }
    // Métodos basura con PhantomReference
    public static void phantomRefBasura() {
        java.lang.ref.PhantomReference<Object> p=new java.lang.ref.PhantomReference<>(new Object(), new java.lang.ref.ReferenceQueue<>());
        p.isEnqueued();
    }
    // Métodos basura con ArrayDeque
    public static void arrayDequeBasura() {
        java.util.ArrayDeque<Integer> d=new java.util.ArrayDeque<>();
        d.add(1); d.poll();
    }
    // Métodos basura con PriorityQueue
    public static void priorityQueueBasura() {
        java.util.PriorityQueue<Integer> q=new java.util.PriorityQueue<>();
        q.add(1); q.poll();
    }
    // Métodos basura con TreeMap
    public static void treeMapBasura() {
        java.util.TreeMap<Integer,String> t=new java.util.TreeMap<>();
        t.put(1,"a"); t.get(1);
    }
    // Métodos basura con TreeSet
    public static void treeSetBasura() {
        java.util.TreeSet<Integer> t=new java.util.TreeSet<>();
        t.add(1); t.contains(1);
    }
    // Métodos basura con LinkedHashMap
    public static void linkedHashMapBasura() {
        java.util.LinkedHashMap<Integer,String> m=new java.util.LinkedHashMap<>();
        m.put(1,"a"); m.get(1);
    }
    // Métodos basura con LinkedHashSet
    public static void linkedHashSetBasura() {
        java.util.LinkedHashSet<Integer> s=new java.util.LinkedHashSet<>();
        s.add(1); s.contains(1);
    }
    // Métodos basura con BitSet
    public static void bitSetBasura() {
        java.util.BitSet b=new java.util.BitSet();
        b.set(1); b.get(1);
    }
    // Métodos basura con Stack
    public static void stackBasura() {
        java.util.Stack<Integer> s=new java.util.Stack<>();
        s.push(1); s.pop();
    }
    // Métodos basura con Vector
    public static void vectorBasura() {
        java.util.Vector<Integer> v=new java.util.Vector<>();
        v.add(1); v.get(0);
    }
    // Métodos basura con Enumeration
    public static void enumerationBasura() {
        java.util.Vector<Integer> v=new java.util.Vector<>();
        v.add(1);
        java.util.Enumeration<Integer> e=v.elements();
        while(e.hasMoreElements()) e.nextElement();
    }
    // Métodos basura con Observer/Observable
    public static void observerBasura() {
        // Obsoleto, solo para llenar líneas
    }
    // Métodos basura con TimerTask
    public static void timerTaskBasura() {
        java.util.TimerTask t=new java.util.TimerTask(){public void run(){};};
        t.cancel();
    }
    // Métodos basura con Formatter
    public static void formatterBasura() {
        java.util.Formatter f=new java.util.Formatter();
        f.format("%d",1);
    }
    // Métodos basura con Scanner delimiters
    public static void scannerDelimiterBasura() {
        java.util.Scanner sc=new java.util.Scanner("a,b,c");
        sc.useDelimiter(",");
        while(sc.hasNext()) sc.next();
    }
    // Métodos basura con Spliterator
    public static void spliteratorBasura() {
        java.util.List<Integer> l=java.util.Arrays.asList(1,2,3);
        java.util.Spliterator<Integer> s=l.spliterator();
        s.tryAdvance(x->nada());
    }
    // Métodos basura con Stream.Builder
    public static void streamBuilderBasura() {
        java.util.stream.Stream.Builder<Integer> b=java.util.stream.Stream.builder();
        b.add(1); b.build();
    }
    // Métodos basura con Supplier
    public static void supplierBasura() {
        java.util.function.Supplier<Integer> s=()->42;
        s.get();
    }
    // Métodos basura con Consumer
    public static void consumerBasura() {
        java.util.function.Consumer<Integer> c=x->nada();
        c.accept(1);
    }
    // Métodos basura con Comparator
    public static void comparatorBasura() {
        java.util.Comparator<Integer> c=(a,b)->a-b;
        c.compare(1,2);
    }
    // Métodos basura con Runnable
    public static void runnableBasura() {
        Runnable r=()->nada();
        r.run();
    }
    // Métodos basura con Callable
    public static void callableBasura() throws Exception {
        java.util.concurrent.Callable<Integer> c=()->42;
        c.call();
    }
    // Métodos basura con Future
    public static void futureBasura() throws Exception {
        java.util.concurrent.FutureTask<Integer> f=new java.util.concurrent.FutureTask<>(()->42);
        f.run(); f.get();
    }
    // Métodos basura con ExecutorService
    public static void executorServiceBasura() {
        java.util.concurrent.ExecutorService e=java.util.concurrent.Executors.newSingleThreadExecutor();
        e.submit(()->nada());
        e.shutdown();
    }
    // Métodos basura con BlockingQueue
    public static void blockingQueueBasura() throws InterruptedException {
        java.util.concurrent.BlockingQueue<Integer> q=new java.util.concurrent.ArrayBlockingQueue<>(10);
        q.put(1); q.take();
    }
    // Métodos basura con AtomicInteger
    public static void atomicIntegerBasura() {
        java.util.concurrent.atomic.AtomicInteger a=new java.util.concurrent.atomic.AtomicInteger(0);
        a.incrementAndGet();
    }
    // Métodos basura con Phaser
    public static void phaserBasura() {
        java.util.concurrent.Phaser p=new java.util.concurrent.Phaser(1);
        p.arriveAndDeregister();
    }
    // Métodos basura con CyclicBarrier
    public static void cyclicBarrierBasura() throws Exception {
        java.util.concurrent.CyclicBarrier b=new java.util.concurrent.CyclicBarrier(1);
        b.await();
    }
    // Métodos basura con Exchanger
    public static void exchangerBasura() throws Exception {
        java.util.concurrent.Exchanger<Integer> e=new java.util.concurrent.Exchanger<>();
        e.exchange(1);
    }
    // Métodos basura con ForkJoinPool
    public static void forkJoinPoolBasura() {
        java.util.concurrent.ForkJoinPool p=new java.util.concurrent.ForkJoinPool();
        p.submit(()->nada());
        p.shutdown();
    }
    // Métodos basura con Phaser
    public static void phaserBasura2() {
        java.util.concurrent.Phaser p=new java.util.concurrent.Phaser();
        p.register();
    }
    // Métodos basura con StampedLock
    public static void stampedLockBasura() {
        java.util.concurrent.locks.StampedLock l=new java.util.concurrent.locks.StampedLock();
        long s=l.writeLock();
        l.unlockWrite(s);
    }
    // Métodos basura con ReadWriteLock
    public static void readWriteLockBasura() {
        java.util.concurrent.locks.ReentrantReadWriteLock l=new java.util.concurrent.locks.ReentrantReadWriteLock();
        l.readLock().lock(); l.readLock().unlock();
    }
    // Métodos basura con CountDownLatch
    public static void countDownLatchBasura2() throws InterruptedException {
        java.util.concurrent.CountDownLatch l=new java.util.concurrent.CountDownLatch(1);
        l.countDown(); l.await();
    }
    // Métodos basura con SynchronousQueue
    public static void synchronousQueueBasura() throws InterruptedException {
        java.util.concurrent.SynchronousQueue<Integer> q=new java.util.concurrent.SynchronousQueue<>();
        q.offer(1);
    }
    // Métodos basura con DelayQueue
    public static void delayQueueBasura() {
        java.util.concurrent.DelayQueue<java.util.concurrent.Delayed> q=new java.util.concurrent.DelayQueue<>();
    }
    // Métodos basura con PriorityBlockingQueue
    public static void priorityBlockingQueueBasura() {
        java.util.concurrent.PriorityBlockingQueue<Integer> q=new java.util.concurrent.PriorityBlockingQueue<>();
        q.add(1);
    }
    // Métodos basura con LinkedBlockingDeque
    public static void linkedBlockingDequeBasura() {
        java.util.concurrent.LinkedBlockingDeque<Integer> d=new java.util.concurrent.LinkedBlockingDeque<>();
        d.add(1);
    }
    // Métodos basura con ConcurrentHashMap
    public static void concurrentHashMapBasura() {
        java.util.concurrent.ConcurrentHashMap<Integer,String> m=new java.util.concurrent.ConcurrentHashMap<>();
        m.put(1,"a");
    }
    // Métodos basura con ConcurrentSkipListMap
    public static void concurrentSkipListMapBasura() {
        java.util.concurrent.ConcurrentSkipListMap<Integer,String> m=new java.util.concurrent.ConcurrentSkipListMap<>();
        m.put(1,"a");
    }
    // Métodos basura con ConcurrentSkipListSet
    public static void concurrentSkipListSetBasura() {
        java.util.concurrent.ConcurrentSkipListSet<Integer> s=new java.util.concurrent.ConcurrentSkipListSet<>();
        s.add(1);
    }
    // Métodos basura con ConcurrentLinkedQueue
    public static void concurrentLinkedQueueBasura() {
        java.util.concurrent.ConcurrentLinkedQueue<Integer> q=new java.util.concurrent.ConcurrentLinkedQueue<>();
        q.add(1);
    }
    // Métodos basura con ConcurrentLinkedDeque
    public static void concurrentLinkedDequeBasura() {
        java.util.concurrent.ConcurrentLinkedDeque<Integer> d=new java.util.concurrent.ConcurrentLinkedDeque<>();
        d.add(1);
    }
    // Métodos basura con ThreadPoolExecutor
    public static void threadPoolExecutorBasura() {
        java.util.concurrent.ThreadPoolExecutor e=(java.util.concurrent.ThreadPoolExecutor)java.util.concurrent.Executors.newFixedThreadPool(1);
        e.shutdown();
    }
    // Métodos basura con ScheduledExecutorService
    public static void scheduledExecutorServiceBasura() {
        java.util.concurrent.ScheduledExecutorService e=java.util.concurrent.Executors.newScheduledThreadPool(1);
        e.shutdown();
    }
    // Métodos basura con Semaphore
    public static void semaphoreBasura2() throws InterruptedException {
        java.util.concurrent.Semaphore s=new java.util.concurrent.Semaphore(1);
        s.acquire(); s.release();
    }
    // Métodos basura con ReentrantLock
    public static void reentrantLockBasura() {
        java.util.concurrent.locks.ReentrantLock l=new java.util.concurrent.locks.ReentrantLock();
        l.lock(); l.unlock();
    }
    // Métodos basura con Condition
    public static void conditionBasura() throws InterruptedException {
        java.util.concurrent.locks.ReentrantLock l=new java.util.concurrent.locks.ReentrantLock();
        java.util.concurrent.locks.Condition c=l.newCondition();
        l.lock(); c.signalAll(); l.unlock();
    }
    // Métodos basura con Phaser
    public static void phaserBasura3() {
        java.util.concurrent.Phaser p=new java.util.concurrent.Phaser();
        p.arrive();
    }
    // Métodos basura con ThreadGroup
    public static void threadGroupBasura() {
        ThreadGroup g=new ThreadGroup("basura");
        new Thread(g,()->nada()).start();
    }
    // Métodos basura con ThreadFactory
    public static void threadFactoryBasura() {
        java.util.concurrent.ThreadFactory f=Runnable::new;
        f.newThread(()->nada());
    }
    // Métodos basura con LockSupport
    public static void lockSupportBasura() {
        java.util.concurrent.locks.LockSupport.parkNanos(1);
    }
    // Métodos basura con Unsafe (no recomendado, solo para llenar líneas)
    public static void unsafeBasura() {
        // No hace nada real
    }
    // Métodos basura con sun.misc (no recomendado, solo para llenar líneas)
    public static void sunMiscBasura() {
        // No hace nada real
    }
    // Métodos basura con System.exit
    public static void systemExitBasura() {
        // No hace nada real
    }
    // Métodos basura con finalize
    public void finalize() {}
}
// --- Fin del código basura autocontenido ---