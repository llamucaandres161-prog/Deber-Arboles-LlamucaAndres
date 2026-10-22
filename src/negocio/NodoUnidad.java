package negocio;

import enums.TipoUnidad;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelo.UnidadBiblioteca;

public class NodoUnidad {

    private final UnidadBiblioteca dato;
    private final List<NodoUnidad> hijos = new ArrayList<>();
    private final CatalogoABB catalogo; // solo existe si la unidad es un ESTANTE

    public NodoUnidad(UnidadBiblioteca dato) {
        this.dato = dato;
        this.catalogo = dato.getTipo() == TipoUnidad.ESTANTE ? new CatalogoABB() : null;
    }

    public UnidadBiblioteca getDato() { return dato; }

    public List<NodoUnidad> getHijos() { return Collections.unmodifiableList(hijos); }

    public void agregarHijo(NodoUnidad hijo) { hijos.add(hijo); }

    public boolean esHoja() { return hijos.isEmpty(); }

    /** Catálogo de libros; null si la unidad no es un ESTANTE. */
    public CatalogoABB getCatalogo() { return catalogo; }
}
