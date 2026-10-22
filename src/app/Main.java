package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import java.util.List;
import java.util.Scanner;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoUnidad;

public class Main {

    private static final Scanner SC = new Scanner(System.in);
    private static ArbolBiblioteca biblioteca = DatosPrueba.crearBibliotecaVacia();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Opción: ");
            try {
                ejecutar(opcion);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
        System.out.println("Hasta luego.");
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== CATÁLOGO BIBLIOTECA FISEI =====");
        System.out.println(" 1. Cargar estructura inicial");
        System.out.println(" 2. Mostrar jerarquía");
        System.out.println(" 3. Agregar unidad");
        System.out.println(" 4. Buscar unidad");
        System.out.println(" 5. Consultar altura, hojas, nivel y total de libros");
        System.out.println(" 6. Insertar libro en un estante");
        System.out.println(" 7. Buscar libro en un estante");
        System.out.println(" 8. Modificar libro de un estante");
        System.out.println(" 9. Eliminar libro de un estante");
        System.out.println("10. Mostrar recorridos con traza");
        System.out.println("11. Ejecutar retos");
        System.out.println("12. Ejecutar PruebasBiblioteca");
        System.out.println(" 0. Salir");
    }

    private static void ejecutar(int opcion) {
        switch (opcion) {
            case 1: cargarInicial(); break;
            case 2: System.out.print(biblioteca.mostrarJerarquia()); break;
            case 3: agregarUnidad(); break;
            case 4: buscarUnidad(); break;
            case 5: consultas(); break;
            case 6: insertarLibro(); break;
            case 7: buscarLibro(); break;
            case 8: modificarLibro(); break;
            case 9: eliminarLibro(); break;
            case 10: recorridos(); break;
            case 11: retos(); break;
            case 12: PruebasBiblioteca.main(new String[0]); break;
            case 0: break;
            default: System.out.println("Opción no válida.");
        }
    }

    // ------------------------------------------------------------- unidades

    private static void cargarInicial() {
        biblioteca = DatosPrueba.crearBibliotecaInicial();
        System.out.println("Estructura inicial cargada.");
        System.out.print(biblioteca.mostrarJerarquia());
    }

    private static void agregarUnidad() {
        String padre = leerTexto("Código del padre: ");
        String codigo = leerTexto("Código de la nueva unidad: ");
        String nombre = leerTexto("Nombre: ");
        String tipoTxt = leerTexto("Tipo (BIBLIOTECA, AREA, ESTANTE): ").toUpperCase();
        TipoUnidad tipo;
        try {
            tipo = TipoUnidad.valueOf(tipoTxt);
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo no válido.");
            return;
        }
        boolean ok = biblioteca.agregar(padre, new UnidadBiblioteca(codigo, nombre, tipo));
        System.out.println(ok ? "Unidad agregada."
                : "No se pudo agregar (padre inexistente, código repetido o jerarquía inválida).");
    }

    private static void buscarUnidad() {
        NodoUnidad nodo = biblioteca.buscar(leerTexto("Código a buscar: "));
        if (nodo == null) {
            System.out.println("No existe.");
        } else {
            System.out.println("Encontrada: " + nodo.getDato() + " [" + nodo.getDato().getTipo() + "]");
        }
    }

    private static void consultas() {
        System.out.println("Altura: " + biblioteca.altura() + " aristas");
        System.out.println("Hojas: " + biblioteca.contarHojas());
        System.out.println("Total de libros: " + biblioteca.contarLibrosTotal());
        String codigo = leerTexto("Código para consultar su nivel: ");
        System.out.println("Nivel de " + codigo.toUpperCase() + ": " + biblioteca.nivel(codigo));
    }

    // --------------------------------------------------------------- libros

    /** Pide un código de estante y devuelve su catálogo, o null si no es válido. */
    private static CatalogoABB pedirCatalogo() {
        String codigo = leerTexto("Código del estante: ");
        NodoUnidad nodo = biblioteca.buscar(codigo);
        if (nodo == null) {
            System.out.println("El estante no existe.");
            return null;
        }
        if (nodo.getCatalogo() == null) {
            System.out.println("La unidad " + nodo.getDato().getCodigo() + " no es un ESTANTE.");
            return null;
        }
        return nodo.getCatalogo();
    }

    private static void insertarLibro() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        int codigo = leerEntero("Código del libro: ");
        String titulo = leerTexto("Título: ");
        String autor = leerTexto("Autor: ");
        int anio = leerEntero("Año (1900-2026): ");
        boolean ok = catalogo.insertar(new Libro(codigo, titulo, autor, anio));
        System.out.println(ok ? "Libro insertado." : "El código ya existe.");
    }

    private static void buscarLibro() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        Libro libro = catalogo.buscar(leerEntero("Código del libro: "));
        System.out.println(libro == null ? "No existe." : "Encontrado: " + libro);
    }

    private static void modificarLibro() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        int codigo = leerEntero("Código del libro: ");
        String titulo = leerTexto("Nuevo título: ");
        String autor = leerTexto("Nuevo autor: ");
        int anio = leerEntero("Nuevo año (1900-2026): ");
        System.out.println(catalogo.modificar(codigo, titulo, autor, anio)
                ? "Libro modificado." : "No existe ese código.");
    }

    private static void eliminarLibro() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        System.out.println(catalogo.eliminar(leerEntero("Código del libro: "))
                ? "Libro eliminado." : "No existe ese código.");
    }

    private static void recorridos() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        for (TipoRecorrido tipo : TipoRecorrido.values()) {
            System.out.println("-- traza " + tipo);
            System.out.println(catalogo.trazaRecorrido(tipo));
            System.out.println();
        }
    }

    private static void retos() {
        CatalogoABB catalogo = pedirCatalogo();
        if (catalogo == null) {
            return;
        }
        int a = leerEntero("Rango: desde a = ");
        int b = leerEntero("Rango: hasta b = ");
        System.out.println("contarEnRango(" + a + ", " + b + ") = " + catalogo.contarEnRango(a, b));

        int codigo = leerEntero("Código para caminoBusqueda: ");
        System.out.println("caminoBusqueda(" + codigo + ") = " + catalogo.caminoBusqueda(codigo));

        System.out.println("esABBValido() = " + catalogo.esABBValido());

        int[] preorden = {60, 35, 20, 45, 40, 80, 70, 75, 90};
        CatalogoABB reconstruido = CatalogoABB.desdePreorden(preorden);
        System.out.println("Extra desdePreorden([60 35 20 45 40 80 70 75 90]):");
        System.out.println("  Inorden: " + codigos(reconstruido.recorrer(TipoRecorrido.INORDEN)));
        System.out.println("  Preorden: " + codigos(reconstruido.recorrer(TipoRecorrido.PREORDEN)));
        System.out.println("  esABBValido() = " + reconstruido.esABBValido());
    }

    private static String codigos(List<Libro> libros) {
        StringBuilder sb = new StringBuilder();
        for (Libro l : libros) {
            sb.append(l.getCodigo()).append(' ');
        }
        return sb.toString().trim();
    }

    // ------------------------------------------------------------- entrada

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return SC.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = SC.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número entero válido.");
            }
        }
    }
}
