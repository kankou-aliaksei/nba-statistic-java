package pro.sunspace.nba.exception;

public class ServerException extends RuntimeException {
    private final String traceId;

    public ServerException(String message, String traceId) {
        super(message);
        this.traceId = traceId;
    }

    public String getTraceId() {
        return traceId;
    }
}