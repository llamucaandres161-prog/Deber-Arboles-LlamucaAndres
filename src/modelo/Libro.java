package modelo;

public class Libro {

    public static final int ANIO_MIN = 1900;
    public static final int ANIO_MAX = 2026;

    private final int codigo; // sin setter: la clave del ABB no puede cambiar
    private String titulo;
    private String autor;
    private int anio;

    public Libro(int codigo, String titulo, String autor, int anio) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código debe ser un entero mayor que 0.");
        }
        this.codigo = codigo;
        actualizar(titulo, autor, anio);
    }

    /** Cambia título, autor y año. Valida todo antes de asignar nada. */
    public void actualizar(String titulo, String autor, int anio) {
        String t = validarTexto(titulo, "título");
        String a = validarTexto(autor, "autor");
        validarAnio(anio);
        this.titulo = t;
        this.autor = a;
        this.anio = anio;
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El " + campo + " no puede estar vacío.");
        }
        return valor.trim();
    }

    private static void validarAnio(int anio) {
        if (anio < ANIO_MIN || anio > ANIO_MAX) {
            throw new IllegalArgumentException(
                    "El año debe estar entre " + ANIO_MIN + " y " + ANIO_MAX + ".");
        }
    }

    public int getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAnio() { return anio; }

    public void setTitulo(String titulo) { this.titulo = validarTexto(titulo, "título"); }
    public void setAutor(String autor) { this.autor = validarTexto(autor, "autor"); }
    public void setAnio(int anio) { validarAnio(anio); this.anio = anio; }

    @Override
    public String toString() {
        return "[" + codigo + "] " + titulo + " - " + autor + " (" + anio + ")";
    }
}
