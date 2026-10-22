package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import java.util.Arrays;
import java.util.List;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoLibro;

public class PruebasBiblioteca {

    private interface Accion {
        void ejecutar();
    }

    private static int total;
    private static int ok;

    public static void main(String[] args) {
        total = 0;
        ok = 0;

        pruebaSoloRaiz();
        pruebaEstructuraInicial();
        pruebaNiveles();
        pruebaJerarquiaInvalida();
        pruebaRecorridosSwE1();
        pruebaTraza();
        pruebaCodigoRepetido();
        pruebaCaminoYRango();
        pruebaEliminaciones();
        pruebaInexistentes();
        pruebaDatosInvalidos();
        pruebaModificar();
        pruebaABBInvalido();
        pruebaDesdePreorden();

        System.out.println();
        System.out.println(ok + "/" + total + " pruebas OK");
    }

    // ------------------------------------------------------------- pruebas

    private static void pruebaSoloRaiz() {
        ArbolBiblioteca b = DatosPrueba.crearBibliotecaVacia();
        comprobar("Solo raíz: altura 0", b.altura() == 0);
        comprobar("Solo raíz: 1 hoja", b.contarHojas() == 1);
    }

    private static void pruebaEstructuraInicial() {
        ArbolBiblioteca b = DatosPrueba.crearBibliotecaInicial();
        comprobar("Inicial: altura 2", b.altura() == 2);
        comprobar("Inicial: 4 hojas", b.contarHojas() == 4);
        comprobar("Inicial: 13 libros", b.contarLibrosTotal() == 13);
        comprobar("Inicial: 7 unidades", b.mostrarJerarquia().trim().split("\n").length == 7);
    }

    private static void pruebaNiveles() {
        ArbolBiblioteca b = DatosPrueba.crearBibliotecaInicial();
        comprobar("Nivel BIB = 0", b.nivel("BIB") == 0);
        comprobar("Nivel sw (minúscula) = 1", b.nivel("sw") == 1);
        comprobar("Nivel SW-E2 = 2", b.nivel("SW-E2") == 2);
        comprobar("Nivel XYZ = -1", b.nivel("XYZ") == -1);
    }

    private static void pruebaJerarquiaInvalida() {
        ArbolBiblioteca b = DatosPrueba.crearBibliotecaInicial();
        comprobar("ESTANTE bajo BIBLIOTECA = false",
                !b.agregar("BIB", new UnidadBiblioteca("E9", "Estante 9", TipoUnidad.ESTANTE)));
        comprobar("AREA bajo BIBLIOTECA = true",
                b.agregar("BIB", new UnidadBiblioteca("MAT", "Matemática", TipoUnidad.AREA)));
        comprobar("Código de unidad repetido = false",
                !b.agregar("BIB", new UnidadBiblioteca("sw", "Otra", TipoUnidad.AREA)));
        comprobar("Padre inexistente = false",
                !b.agregar("NOPE", new UnidadBiblioteca("X1", "X", TipoUnidad.AREA)));
    }

    private static void pruebaRecorridosSwE1() {
        CatalogoABB c = DatosPrueba.crearBibliotecaInicial().buscar("SW-E1").getCatalogo();
        comprobar("SW-E1 preorden",
                codigos(c.recorrer(TipoRecorrido.PREORDEN)).equals("60 35 20 45 40 80 70 75 90"));
        comprobar("SW-E1 inorden",
                codigos(c.recorrer(TipoRecorrido.INORDEN)).equals("20 35 40 45 60 70 75 80 90"));
        comprobar("SW-E1 posorden",
                codigos(c.recorrer(TipoRecorrido.POSORDEN)).equals("20 40 45 35 75 70 90 80 60"));
    }

    private static void pruebaTraza() {
        CatalogoABB c = DatosPrueba.crearBibliotecaInicial().buscar("SW-E1").getCatalogo();
        String traza = c.trazaRecorrido(TipoRecorrido.PREORDEN);
        String[] lineas = traza.split("\n");
        comprobar("Traza: empieza con 'entra 60'", lineas[0].equals("entra 60"));
        comprobar("Traza: contiene 'null -> regresa'", traza.contains("null -> regresa"));
        comprobar("Traza: última línea con la secuencia", lineas[lineas.length - 1]
                .equals("Secuencia PREORDEN: 60 35 20 45 40 80 70 75 90"));
    }

    private static void pruebaCodigoRepetido() {
        CatalogoABB c = DatosPrueba.crearBibliotecaInicial().buscar("SW-E1").getCatalogo();
        comprobar("Insertar código repetido = false", !c.insertar(DatosPrueba.libro(45)));
        comprobar("Tras el repetido sigue habiendo 9 libros", c.contar() == 9);
    }

    private static void pruebaCaminoYRango() {
        CatalogoABB c = DatosPrueba.crearBibliotecaInicial().buscar("SW-E1").getCatalogo();
        comprobar("caminoBusqueda(42) = [60, 35, 45, 40]",
                c.caminoBusqueda(42).equals(Arrays.asList(60, 35, 45, 40)));
        comprobar("contarEnRango(40, 75) = 5", c.contarEnRango(40, 75) == 5);
    }

    private static void pruebaEliminaciones() {
        CatalogoABB c = DatosPrueba.crearBibliotecaInicial().buscar("SW-E1").getCatalogo();
        c.insertar(DatosPrueba.libro(42));
        boolean r1 = c.eliminar(20);   // hoja
        boolean r2 = c.eliminar(70);   // un hijo
        boolean r3 = c.eliminar(60);   // dos hijos (raíz)
        comprobar("Eliminar 20, 70 y 60 devuelve true", r1 && r2 && r3);
        comprobar("Preorden final 75 35 45 40 42 80 90",
                codigos(c.recorrer(TipoRecorrido.PREORDEN)).equals("75 35 45 40 42 80 90"));
        comprobar("El árbol sigue siendo un ABB válido", c.esABBValido());
    }

    private static void pruebaInexistentes() {
        CatalogoABB c = DatosPrueba.crearBibliotecaInicial().buscar("SW-E1").getCatalogo();
        comprobar("Buscar inexistente = null", c.buscar(999) == null);
        comprobar("Modificar inexistente = false", !c.modificar(999, "T", "A", 2000));
        comprobar("Eliminar inexistente = false", !c.eliminar(999));
    }

    private static void pruebaDatosInvalidos() {
        esperarIAE("Libro con código 0", () -> new Libro(0, "T", "A", 2000));
        esperarIAE("Libro con código negativo", () -> new Libro(-5, "T", "A", 2000));
        esperarIAE("Libro con año 1899", () -> new Libro(1, "T", "A", 1899));
        esperarIAE("Libro con año 2027", () -> new Libro(1, "T", "A", 2027));
        esperarIAE("Libro con título vacío", () -> new Libro(1, "  ", "A", 2000));
        esperarIAE("Libro con autor nulo", () -> new Libro(1, "T", null, 2000));
    }

    private static void pruebaModificar() {
        CatalogoABB c = DatosPrueba.crearBibliotecaInicial().buscar("SW-E1").getCatalogo();
        boolean r = c.modificar(45, "Nuevo título", "Nuevo autor", 1999);
        Libro l = c.buscar(45);
        comprobar("Modificar existente = true y cambia los datos",
                r && l.getTitulo().equals("Nuevo título") && l.getAnio() == 1999);
        comprobar("Modificar no cambia el código", l.getCodigo() == 45);
    }

    private static void pruebaABBInvalido() {
        CatalogoABB c = new CatalogoABB();
        NodoLibro raiz = new NodoLibro(DatosPrueba.libro(50));
        NodoLibro hijo = new NodoLibro(DatosPrueba.libro(30));
        NodoLibro nieto = new NodoLibro(DatosPrueba.libro(60)); // 60 > 50 pero está a la izquierda
        raiz.setIzquierdo(hijo);
        hijo.setDerecho(nieto);
        c.setRaiz(raiz);
        comprobar("esABBValido con nieto mal ubicado = false", !c.esABBValido());
        comprobar("esABBValido en árbol correcto = true",
                DatosPrueba.crearBibliotecaInicial().buscar("SW-E1").getCatalogo().esABBValido());
    }

    private static void pruebaDesdePreorden() {
        int[] pre = {60, 35, 20, 45, 40, 80, 70, 75, 90};
        CatalogoABB c = CatalogoABB.desdePreorden(pre);
        comprobar("desdePreorden: mismo preorden", codigos(c.recorrer(TipoRecorrido.PREORDEN))
                .equals("60 35 20 45 40 80 70 75 90"));
        comprobar("desdePreorden: inorden ordenado", codigos(c.recorrer(TipoRecorrido.INORDEN))
                .equals("20 35 40 45 60 70 75 80 90"));
        esperarIAE("desdePreorden con secuencia inválida",
                () -> CatalogoABB.desdePreorden(new int[]{50, 60, 40, 55}));
    }

    // ------------------------------------------------------------- ayudas

    private static String codigos(List<Libro> libros) {
        StringBuilder sb = new StringBuilder();
        for (Libro l : libros) {
            sb.append(l.getCodigo()).append(' ');
        }
        return sb.toString().trim();
    }

    private static void comprobar(String nombre, boolean condicion) {
        total++;
        if (condicion) {
            ok++;
        }
        System.out.println((condicion ? "[OK]    " : "[FALLA] ") + nombre);
    }

    private static void esperarIAE(String nombre, Accion accion) {
        boolean lanzo = false;
        try {
            accion.ejecutar();
        } catch (IllegalArgumentException e) {
            lanzo = true;
        }
        comprobar(nombre + " -> IllegalArgumentException", lanzo);
    }
}
