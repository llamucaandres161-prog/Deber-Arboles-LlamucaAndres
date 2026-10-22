package negocio;

import enums.TipoUnidad;
import modelo.UnidadBiblioteca;

/** Árbol general n-ario: BIBLIOTECA -> AREA -> ESTANTE. Todo es recursivo. */
public class ArbolBiblioteca {

    private final NodoUnidad raiz;

    public ArbolBiblioteca(UnidadBiblioteca raiz) {
        if (raiz == null || raiz.getTipo() != TipoUnidad.BIBLIOTECA) {
            throw new IllegalArgumentException("La raíz debe ser de tipo BIBLIOTECA.");
        }
        this.raiz = new NodoUnidad(raiz);
    }

    public NodoUnidad getRaiz() { return raiz; }

    /** Agrega una unidad bajo el padre indicado, respetando la jerarquía y sin códigos repetidos. */
    public boolean agregar(String codigoPadre, UnidadBiblioteca nueva) {
        if (codigoPadre == null || nueva == null) {
            return false;
        }
        NodoUnidad padre = buscar(codigoPadre);
        if (padre == null) {
            return false;
        }
        if (buscar(nueva.getCodigo()) != null) {
            return false;
        }
        if (!padre.getDato().getTipo().puedeTenerHijo(nueva.getTipo())) {
            return false;
        }
        padre.agregarHijo(new NodoUnidad(nueva));
        return true;
    }

    /** Busca sin distinguir mayúsculas. null si no existe. */
    public NodoUnidad buscar(String codigo) {
        if (codigo == null) {
            return null;
        }
        return buscar(raiz, codigo.trim());
    }

    private NodoUnidad buscar(NodoUnidad nodo, String codigo) {
        if (nodo.getDato().getCodigo().equalsIgnoreCase(codigo)) {
            return nodo;
        }
        for (NodoUnidad hijo : nodo.getHijos()) {
            NodoUnidad r = buscar(hijo, codigo);
            if (r != null) {
                return r;
            }
        }
        return null;
    }

    /** Altura en aristas (solo la raíz = 0). */
    public int altura() {
        return altura(raiz);
    }

    private int altura(NodoUnidad nodo) {
        int max = 0;
        for (NodoUnidad hijo : nodo.getHijos()) {
            max = Math.max(max, 1 + altura(hijo));
        }
        return max;
    }

    public int contarHojas() {
        return contarHojas(raiz);
    }

    private int contarHojas(NodoUnidad nodo) {
        if (nodo.esHoja()) {
            return 1;
        }
        int total = 0;
        for (NodoUnidad hijo : nodo.getHijos()) {
            total += contarHojas(hijo);
        }
        return total;
    }

    /** Nivel de la unidad (raíz = 0). -1 si no existe. */
    public int nivel(String codigo) {
        if (codigo == null) {
            return -1;
        }
        return nivel(raiz, codigo.trim(), 0);
    }

    private int nivel(NodoUnidad nodo, String codigo, int nivelActual) {
        if (nodo.getDato().getCodigo().equalsIgnoreCase(codigo)) {
            return nivelActual;
        }
        for (NodoUnidad hijo : nodo.getHijos()) {
            int r = nivel(hijo, codigo, nivelActual + 1);
            if (r != -1) {
                return r;
            }
        }
        return -1;
    }

    /** Jerarquía en preorden, con 2 espacios por nivel. */
    public String mostrarJerarquia() {
        StringBuilder sb = new StringBuilder();
        mostrarJerarquia(raiz, "", sb);
        return sb.toString();
    }

    private void mostrarJerarquia(NodoUnidad nodo, String sangria, StringBuilder sb) {
        sb.append(sangria).append(nodo.getDato().getCodigo())
                .append(' ').append(nodo.getDato().getNombre());
        if (nodo.getCatalogo() != null) {
            sb.append(" (").append(nodo.getCatalogo().contar()).append(" libros)");
        }
        sb.append('\n');
        for (NodoUnidad hijo : nodo.getHijos()) {
            mostrarJerarquia(hijo, sangria + "  ", sb);
        }
    }

    public int contarLibrosTotal() {
        return contarLibrosTotal(raiz);
    }

    private int contarLibrosTotal(NodoUnidad nodo) {
        int total = nodo.getCatalogo() != null ? nodo.getCatalogo().contar() : 0;
        for (NodoUnidad hijo : nodo.getHijos()) {
            total += contarLibrosTotal(hijo);
        }
        return total;
    }
}
