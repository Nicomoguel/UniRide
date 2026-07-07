/**
 * Define los posibles estados de compatibilidad resultantes de una evaluación de coincidencia.
 * * @author HodeCodeDepartment
 */
public enum MatchStatus {
    /** Coincidencia exitosa entre los usuarios. */
    MATCH,
    /** Rechazado debido a incompatibilidad de horarios. */
    REJECTED_SCHEDULE,
    /** Rechazado debido a que la distancia excede el límite permitido. */
    REJECTED_DISTANCE
}
