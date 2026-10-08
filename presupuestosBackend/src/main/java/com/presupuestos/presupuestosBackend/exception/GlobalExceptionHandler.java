package com.presupuestos.presupuestosBackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<String> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());    
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<String> manejarAccesoDenegado(AccesoDenegadoException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(SolicitudInvalidaException.class)
    public ResponseEntity<String> manejarSolicitudInvalida(SolicitudInvalidaException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<String> manejarConflicto(ConflictoException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    
}