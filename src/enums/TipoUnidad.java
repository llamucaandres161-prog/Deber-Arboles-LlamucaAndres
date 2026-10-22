package enums;

/**
 * Tipos de unidad de la biblioteca. La jerarquía permitida es
 * BIBLIOTECA -> AREA -> ESTANTE.
 */
public enum TipoUnidad {
    BIBLIOTECA, AREA, ESTANTE;

    /** Indica si una unidad de este tipo puede tener como hijo una unidad del tipo dado. */
    public boolean puedeTenerHijo(TipoUnidad hijo) {
        switch (this) {
            case BIBLIOTECA:
                return hijo == AREA;
            case AREA:
                return hijo == ESTANTE;
            default:
                return false;
        }
    }
}
