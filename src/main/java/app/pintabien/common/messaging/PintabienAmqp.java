package app.pintabien.common.messaging;

/**
 * Nombres de exchange, routing keys y convenciones de colas.
 * Cada servicio declara sus propias colas (durables) con DLQ: {@code pintabien.<servicio>.<evento>} y {@code ...dlq}.
 */
public final class PintabienAmqp {
    private PintabienAmqp() {}

    public static final String EXCHANGE = "pintabien.events";
    public static final String DLX = "pintabien.events.dlx";

    public static final String RK_EVALUATION_REQUESTED = "evaluation.requested";
    public static final String RK_SOURCES_COLLECTED = "sources.collected";
    public static final String RK_EVALUATION_COMPLETED = "evaluation.completed";
    public static final String RK_DECLARATION_SUBMITTED = "declaration.submitted";

    /** Cabecera AMQP con el numero de reintentos ya realizados. */
    public static final String HEADER_RETRY_COUNT = "x-pintabien-retries";

    public static String queue(String service, String routingKey) {
        return "pintabien." + service + "." + routingKey;
    }

    public static String dlq(String service, String routingKey) {
        return queue(service, routingKey) + ".dlq";
    }
}
