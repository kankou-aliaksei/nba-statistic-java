package pro.sunspace.nba.exception.handler;

import io.opentracing.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.ServerWebExchange;
import pro.sunspace.nba.dto.ErrorResponse;
import pro.sunspace.nba.exception.ServerException;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Tracer tracer;

    public GlobalExceptionHandler(Tracer tracer) {
        this.tracer = tracer;
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationExceptions(
            WebExchangeBindException ex,
            ServerWebExchange exchange) {
        var requestId = exchange.getRequest().getId();

        var span = tracer.buildSpan("validation-exception")
                .withTag("error", true)
                .start();

        var traceId = span.context().toTraceId();

        var errors = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, fieldError ->
                        Optional.ofNullable(fieldError.getDefaultMessage()).orElse("")));

        var errorMessage = errors.toString();
        span.log(Map.of("event", "validation_error", "error", errorMessage));

        var errorResponse = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(errorMessage)
                .path(exchange.getRequest().getPath().value())
                .requestId(requestId)
                .traceId(traceId)
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse))
                .doFinally(signalType -> span.finish());
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleServerWebInputException(ServerWebInputException ex, ServerWebExchange exchange) {
        var requestId = exchange.getRequest().getId();

        var span = tracer.buildSpan("server-web-input-exception")
                .withTag("error", true)
                .start();

        var traceId = span.context().toTraceId();

        var errorMessage = String.format("Message: %s; Reason: %s; Cause: %s",
                ex.getMessage(),
                ex.getReason(),
                Optional.ofNullable(ex.getCause()).map(Throwable::getMessage).orElse("N/A"));

        span.log(Map.of("event", "input_error", "error", errorMessage));

        var errorResponse = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(errorMessage)
                .path(exchange.getRequest().getPath().value())
                .requestId(requestId)
                .traceId(traceId)
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse))
                .doFinally(signalType -> span.finish());
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneralException(Exception ex, ServerWebExchange exchange) {
        var requestId = exchange.getRequest().getId();

        var span = tracer.buildSpan("general-exception")
                .withTag("error", true)
                .start();

        var traceId = span.context().toTraceId();

        var errorMessage = ex.getMessage();
        span.log(Map.of("event", "general_error", "error", errorMessage));

        var errorResponse = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(errorMessage)
                .path(exchange.getRequest().getPath().value())
                .requestId(requestId)
                .traceId(traceId)
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse))
                .doFinally(signalType -> span.finish());
    }

    @ExceptionHandler(ServerException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleServerException(ServerException ex, ServerWebExchange exchange) {

        var requestId = exchange.getRequest().getId();

        var errorResponse = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(ex.getMessage())
                .requestId(requestId)
                .traceId(ex.getTraceId())
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }
}
