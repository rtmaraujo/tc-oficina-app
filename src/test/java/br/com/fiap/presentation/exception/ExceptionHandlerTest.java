package br.com.fiap.presentation.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Exceções de Apresentação")
class ExceptionHandlerTest {

    @Test
    @DisplayName("ValidationException com todos os campos")
    void shouldCreateValidationExceptionWithAllFields() {
        String field = "cpfCnpj";
        String message = "Invalid CPF";
        String errorCode = "INVALID_CPF";

        ValidationException ex = new ValidationException(field, message, errorCode);

        assertEquals(message, ex.getMessage());
        assertEquals(field, ex.getField());
        assertEquals(errorCode, ex.getErrorCode());
    }

    @Test
    @DisplayName("ValidationException com campo e mensagem")
    void shouldCreateValidationExceptionWithFieldAndMessage() {
        String field = "placa";
        String message = "Invalid license plate format";

        ValidationException ex = new ValidationException(field, message);

        assertEquals(message, ex.getMessage());
        assertEquals(field, ex.getField());
        assertNull(ex.getErrorCode());
    }

    @Test
    @DisplayName("ValidationException apenas com mensagem")
    void shouldCreateValidationExceptionWithMessageOnly() {
        String message = "Validation failed";

        ValidationException ex = new ValidationException(message);

        assertEquals(message, ex.getMessage());
        assertNull(ex.getField());
        assertNull(ex.getErrorCode());
    }

    @Test
    @DisplayName("ResourceNotFoundException com todos os campos")
    void shouldCreateResourceNotFoundExceptionWithAllFields() {
        String resourceName = "Client";
        String fieldName = "id";
        Object fieldValue = 123L;

        ResourceNotFoundException ex = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

        assertTrue(ex.getMessage().contains(resourceName));
        assertTrue(ex.getMessage().contains(fieldName));
        assertTrue(ex.getMessage().contains(fieldValue.toString()));
        assertEquals(resourceName, ex.getResourceName());
        assertEquals(fieldName, ex.getFieldName());
        assertEquals(fieldValue, ex.getFieldValue());
    }

    @Test
    @DisplayName("ResourceNotFoundException apenas com mensagem")
    void shouldCreateResourceNotFoundExceptionWithMessageOnly() {
        String message = "Resource not found";

        ResourceNotFoundException ex = new ResourceNotFoundException(message);

        assertEquals(message, ex.getMessage());
        assertNull(ex.getResourceName());
        assertNull(ex.getFieldName());
        assertNull(ex.getFieldValue());
    }
}

