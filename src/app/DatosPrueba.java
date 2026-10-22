package app;

import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;

/** Datos de la estructura inicial del enunciado. */
public class DatosPrueba {

    public static final int[] LIBROS_SW_E1 = {60, 35, 80, 20, 45, 70, 90, 40, 75};
    public static final int[] LIBROS_SW_E2 = {15, 10, 25};
    public static final int[] LIBROS_TI_E1 = {50};

    /** Biblioteca con solo la raíz. */
    public static ArbolBiblioteca crearBibliotecaVacia() {
        return new ArbolBiblioteca(
                new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));
    }

    /** Biblioteca inicial: 7 unidades, 4 hojas, altura 2 y 13 libros. */
    public static ArbolBiblioteca crearBibliotecaInicial() {
        ArbolBiblioteca b = crearBibliotecaVacia();
        b.agregar("BIB", new UnidadBiblioteca("SW", "Software", TipoUnidad.AREA));
        b.agregar("BIB", new UnidadBiblioteca("TI", "Tecnologías de la Información", TipoUnidad.AREA));
        b.agregar("BIB", new UnidadBiblioteca("TEL", "Telecomunicaciones", TipoUnidad.AREA));
        b.agregar("SW", new UnidadBiblioteca("SW-E1", "Estante 1 de Software", TipoUnidad.ESTANTE));
        b.agregar("SW", new UnidadBiblioteca("SW-E2", "Estante 2 de Software", TipoUnidad.ESTANTE));
        b.agregar("TI", new UnidadBiblioteca("TI-E1", "Estante 1 de TI", TipoUnidad.ESTANTE));
        cargarLibros(b.buscar("SW-E1").getCatalogo(), LIBROS_SW_E1);
        cargarLibros(b.buscar("SW-E2").getCatalogo(), LIBROS_SW_E2);
        cargarLibros(b.buscar("TI-E1").getCatalogo(), LIBROS_TI_E1);
        return b;
    }

    /** Inserta los códigos en el orden dado. */
    public static void cargarLibros(CatalogoABB catalogo, int[] codigos) {
        for (int codigo : codigos) {
            catalogo.insertar(libro(codigo));
        }
    }

    /** Libro de ejemplo para un código. */
    public static Libro libro(int codigo) {
        return new Libro(codigo, "Libro " + codigo, "Autor FISEI", 2020);
    }
}
