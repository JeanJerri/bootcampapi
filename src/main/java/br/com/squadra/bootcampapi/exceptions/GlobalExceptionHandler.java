package br.com.squadra.bootcampapi.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> tipoIncompativel(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        switch (request.getRequestURI()) {
            case "/uf" -> {
                if ("GET".equalsIgnoreCase(request.getMethod())) {
                    body.put("mensagem", "Não foi possível consultar UF no banco de dados.");
                }
            }
            case "/municipio" -> {
                if ("GET".equalsIgnoreCase(request.getMethod())) {
                    body.put("mensagem", "Não foi possível consultar Município no banco de dados.");
                }
            }
            case "/bairro" -> {
                if ("GET".equalsIgnoreCase(request.getMethod())) {
                    body.put("mensagem", "Não foi possível consultar Bairro no banco de dados.");
                }
            }
            case "/pessoa" -> {
                if ("GET".equalsIgnoreCase(request.getMethod())) {
                    body.put("mensagem", "Não foi possível consultar Pessoa no banco de dados.");
                }
            }
        }

        body.put("status", 404);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> argumentoInvalido(HttpMessageNotReadableException ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        switch (request.getRequestURI()) {
            case "/uf" -> handleUfMessages(request, body);
            case "/municipio" -> handleMunicipioMessages(request, body);
            case "/bairro" -> handleBairroMessages(request, body);
            case "/pessoa" -> handlePessoaMessages(request, body);
        }

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)  // Novo manipulador para erro 500
    public ResponseEntity<Map<String, Object>> handleException(Exception ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("mensagem", "Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde.");
        body.put("status", 500);

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void handleUfMessages(HttpServletRequest request, Map<String, Object> body) {
        switch (request.getMethod()) {
            case "POST" -> body.put("mensagem", "Não foi possível incluir UF no banco de dados.");
            case "PUT" -> body.put("mensagem", "Não foi possível alterar UF no banco de dados.");
        }
        body.put("status", 404);
    }

    private void handleMunicipioMessages(HttpServletRequest request, Map<String, Object> body) {
        switch (request.getMethod()) {
            case "POST" -> body.put("mensagem", "Não foi possível cadastrar Município no banco de dados.");
            case "PUT" -> body.put("mensagem", "Não foi possível alterar Município no banco de dados.");
        }
        body.put("status", 404);
    }

    private void handleBairroMessages(HttpServletRequest request, Map<String, Object> body) {
        switch (request.getMethod()) {
            case "POST" -> body.put("mensagem", "Não foi possível incluir Bairro no banco de dados.");
            case "PUT" -> body.put("mensagem", "Não foi possível alterar Bairro no banco de dados.");
        }
        body.put("status", 404);
    }

    private void handlePessoaMessages(HttpServletRequest request, Map<String, Object> body) {
        switch (request.getMethod()) {
            case "POST" -> body.put("mensagem", "Não foi possível incluir Pessoa no banco de dados.");
            case "PUT" -> body.put("mensagem", "Não foi possível alterar Pessoa no banco de dados.");
        }
        body.put("status", 404);
    }
}
