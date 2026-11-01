package app.pintabien.common.model;

/** Semaforo de riesgo crediticio. El color manda; el score solo ordena y explica. */
public enum TrafficLight {
    VERDE,
    AMARILLO,
    ROJO,
    GRIS;

    /** Etiqueta que muestra el panel. */
    public String label() {
        return switch (this) {
            case VERDE -> "Apto";
            case AMARILLO -> "Apto sujeto a validación";
            case ROJO -> "No Apto";
            case GRIS -> "Pendiente / Sin historial";
        };
    }
}
