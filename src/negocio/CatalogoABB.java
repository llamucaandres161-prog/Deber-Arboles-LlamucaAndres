package negocio;

import enums.TipoRecorrido;
import java.util.ArrayList;
import java.util.List;
import modelo.Libro;

/** ABB de libros ordenado por código. Todas las operaciones son recursivas. */
public class CatalogoABB {

    private NodoLibro raiz;

    public NodoLibro getRaiz() { return raiz; }
    public void setRaiz(NodoLibro raiz) { this.raiz = raiz; }

    // ---------------------------------------------------------------- insertar

    /** Inserta por código. false si el código ya existe. */
    public boolean insertar(Libro libro) {
        if (libro == null || buscar(libro.getCodigo()) != null) {
            return false;
        }
        raiz = insertar(raiz, libro);
        return true;
    }

    private NodoLibro insertar(NodoLibro nodo, Libro libro) {
        if (nodo == null) {
            return new NodoLibro(libro);
        }
        if (libro.getCodigo() < nodo.getDato().getCodigo()) {
            nodo.setIzquierdo(insertar(nodo.getIzquierdo(), libro));
        } else {
            nodo.setDerecho(insertar(nodo.getDerecho(), libro));
        }
        return nodo;
    }

    // ------------------------------------------------------------------ buscar

    public Libro buscar(int codigo) {
        NodoLibro n = buscar(raiz, codigo);
        return n == null ? null : n.getDato();
    }

    private NodoLibro buscar(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        int actual = nodo.getDato().getCodigo();
        if (codigo == actual) {
            return nodo;
        }
        return codigo < actual ? buscar(nodo.getIzquierdo(), codigo)
                               : buscar(nodo.getDerecho(), codigo);
    }

    // ------------------------------------------------------------------ contar

    public int contar() {
        return contar(raiz);
    }

    private int contar(NodoLibro nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contar(nodo.getIzquierdo()) + contar(nodo.getDerecho());
    }

    // --------------------------------------------------------------- recorridos

    public List<Libro> recorrer(TipoRecorrido tipo) {
        List<Libro> lista = new ArrayList<>();
        recorrer(raiz, tipo, lista);
        return lista;
    }

    private void recorrer(NodoLibro nodo, TipoRecorrido tipo, List<Libro> lista) {
        if (nodo == null) {
            return;
        }
        if (tipo == TipoRecorrido.PREORDEN) {
            lista.add(nodo.getDato());
        }
        recorrer(nodo.getIzquierdo(), tipo, lista);
        if (tipo == TipoRecorrido.INORDEN) {
            lista.add(nodo.getDato());
        }
        recorrer(nodo.getDerecho(), tipo, lista);
        if (tipo == TipoRecorrido.POSORDEN) {
            lista.add(nodo.getDato());
        }
    }

    /** Traza: entra X / VISITA X / null -> regresa, 2 espacios por nivel y línea final con la secuencia. */
    public String trazaRecorrido(TipoRecorrido tipo) {
        StringBuilder traza = new StringBuilder();
        StringBuilder secuencia = new StringBuilder();
        traza(raiz, "", tipo, traza, secuencia);
        traza.append("Secuencia ").append(tipo).append(": ").append(secuencia.toString().trim());
        return traza.toString();
    }

    private void traza(NodoLibro nodo, String sangria, TipoRecorrido tipo,
                       StringBuilder traza, StringBuilder secuencia) {
        if (nodo == null) {
            traza.append(sangria).append("null -> regresa\n");
            return;
        }
        int codigo = nodo.getDato().getCodigo();
        traza.append(sangria).append("entra ").append(codigo).append('\n');
        if (tipo == TipoRecorrido.PREORDEN) {
            visitar(codigo, sangria, traza, secuencia);
        }
        traza(nodo.getIzquierdo(), sangria + "  ", tipo, traza, secuencia);
        if (tipo == TipoRecorrido.INORDEN) {
            visitar(codigo, sangria, traza, secuencia);
        }
        traza(nodo.getDerecho(), sangria + "  ", tipo, traza, secuencia);
        if (tipo == TipoRecorrido.POSORDEN) {
            visitar(codigo, sangria, traza, secuencia);
        }
    }

    private void visitar(int codigo, String sangria, StringBuilder traza, StringBuilder secuencia) {
        traza.append(sangria).append("VISITA ").append(codigo).append('\n');
        secuencia.append(codigo).append(' ');
    }

    // --------------------------------------------------------------- modificar

    /** Modifica todo menos el código. false si no existe. */
    public boolean modificar(int codigo, String titulo, String autor, int anio) {
        Libro libro = buscar(codigo);
        if (libro == null) {
            return false;
        }
        libro.actualizar(titulo, autor, anio); // lanza IllegalArgumentException si hay datos inválidos
        return true;
    }

    // ---------------------------------------------------------------- eliminar

    /** Elimina hoja, nodo con un hijo o nodo con dos hijos (sucesor inorden). */
    public boolean eliminar(int codigo) {
        if (buscar(codigo) == null) {
            return false;
        }
        raiz = eliminar(raiz, codigo);
        return true;
    }

    private NodoLibro eliminar(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        int actual = nodo.getDato().getCodigo();
        if (codigo < actual) {
            nodo.setIzquierdo(eliminar(nodo.getIzquierdo(), codigo));
        } else if (codigo > actual) {
            nodo.setDerecho(eliminar(nodo.getDerecho(), codigo));
        } else {
            if (nodo.getIzquierdo() == null) {      // hoja o solo hijo derecho
                return nodo.getDerecho();
            }
            if (nodo.getDerecho() == null) {        // solo hijo izquierdo
                return nodo.getIzquierdo();
            }
            // dos hijos: sucesor inorden = mínimo del subárbol derecho
            NodoLibro sucesor = minimo(nodo.getDerecho());
            nodo.setDato(sucesor.getDato());
            nodo.setDerecho(eliminar(nodo.getDerecho(), sucesor.getDato().getCodigo()));
        }
        return nodo;
    }

    private NodoLibro minimo(NodoLibro nodo) {
        return nodo.getIzquierdo() == null ? nodo : minimo(nodo.getIzquierdo());
    }

    // ------------------------------------------------------------------- retos

    /** Cuenta los códigos entre a y b (inclusive), podando las ramas que no pueden aportar. */
    public int contarEnRango(int a, int b) {
        if (a > b) {
            int t = a;
            a = b;
            b = t;
        }
        return contarEnRango(raiz, a, b);
    }

    private int contarEnRango(NodoLibro nodo, int a, int b) {
        if (nodo == null) {
            return 0;
        }
        int codigo = nodo.getDato().getCodigo();
        if (codigo < a) {                      // todo el subárbol izquierdo es menor que a
            return contarEnRango(nodo.getDerecho(), a, b);
        }
        if (codigo > b) {                      // todo el subárbol derecho es mayor que b
            return contarEnRango(nodo.getIzquierdo(), a, b);
        }
        return 1 + contarEnRango(nodo.getIzquierdo(), a, b)
                 + contarEnRango(nodo.getDerecho(), a, b);
    }

    /** Códigos visitados desde la raíz hasta el libro (o hasta donde termina la búsqueda). */
    public List<Integer> caminoBusqueda(int codigo) {
        List<Integer> camino = new ArrayList<>();
        caminoBusqueda(raiz, codigo, camino);
        return camino;
    }

    private void caminoBusqueda(NodoLibro nodo, int codigo, List<Integer> camino) {
        if (nodo == null) {
            return;
        }
        int actual = nodo.getDato().getCodigo();
        camino.add(actual);
        if (codigo < actual) {
            caminoBusqueda(nodo.getIzquierdo(), codigo, camino);
        } else if (codigo > actual) {
            caminoBusqueda(nodo.getDerecho(), codigo, camino);
        }
    }

    /** Verifica TODO el árbol pasando límites mínimo y máximo (exclusivos) hacia abajo. */
    public boolean esABBValido() {
        return esABBValido(raiz, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean esABBValido(NodoLibro nodo, long min, long max) {
        if (nodo == null) {
            return true;
        }
        int codigo = nodo.getDato().getCodigo();
        if (codigo <= min || codigo >= max) {
            return false;
        }
        return esABBValido(nodo.getIzquierdo(), min, codigo)
            && esABBValido(nodo.getDerecho(), codigo, max);
    }

    /**
     * EXTRA: reconstruye el ABB a partir de su preorden, sin insertar uno por uno.
     * Cada valor se coloca según los límites (min, max) que le corresponden. O(n).
     */
    public static CatalogoABB desdePreorden(int[] preorden) {
        CatalogoABB catalogo = new CatalogoABB();
        if (preorden == null || preorden.length == 0) {
            return catalogo;
        }
        int[] indice = {0};
        catalogo.raiz = construir(preorden, indice, Long.MIN_VALUE, Long.MAX_VALUE);
        if (indice[0] != preorden.length) {
            throw new IllegalArgumentException("La secuencia no es un preorden válido de un ABB.");
        }
        return catalogo;
    }

    private static NodoLibro construir(int[] pre, int[] indice, long min, long max) {
        if (indice[0] >= pre.length) {
            return null;
        }
        int valor = pre[indice[0]];
        if (valor <= min || valor >= max) {
            return null;
        }
        indice[0]++;
        NodoLibro nodo = new NodoLibro(
                new Libro(valor, "Libro " + valor, "Autor desconocido", 2000));
        nodo.setIzquierdo(construir(pre, indice, min, valor));
        nodo.setDerecho(construir(pre, indice, valor, max));
        return nodo;
    }
}
