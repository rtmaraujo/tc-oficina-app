package br.com.fiap.util;

import java.util.regex.Pattern;

public class ValidadorUtil {

    private static final Pattern CNPJ_PATTERN = Pattern.compile("^[A-Z0-9]{12}[0-9]{2}$");
    private static final Pattern CPF_PATTERN = Pattern.compile("^[0-9]{11}$");

    public static void validarPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("A placa não pode ser nula ou estar vazia.");
        }

        String clean = placa.replaceAll("[^A-Za-z0-9]", "").toUpperCase();

        if (clean.length() != 7) {
            throw new IllegalArgumentException("Formato de placa inválido: deve conter 7 caracteres (3 letras + 4 números)");
        }

        if (clean.matches("^[A-Z]{3}[0-9]{4}$")) {
            return;
        }

        if (clean.matches("^[A-Z]{3}[0-9][A-Z][0-9]{2}$")) {
            return;
        }

        throw new IllegalArgumentException("Formato de placa inválido: deve seguir o padrão ABC1234 ou ABC1D23");
    }

    public static void validar(String cpfOuCnpj) {
        if (cpfOuCnpj == null || cpfOuCnpj.trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF/CNPJ não pode ser nulo ou vazio.");
        }

        String limpo = cpfOuCnpj.replaceAll("[\\D]", "").toUpperCase();

        if (limpo.length() == 11) {
            validarCPF(limpo);
        } else if (limpo.length() == 14) {
            validarCNPJ(limpo);
        } else {
            throw new IllegalArgumentException("Formato CPF/CNPJ inválido: deve ter 11 ou 14 dígitos.");
        }
    }

    private static void validarCPF(String cpf) {
        if (!CPF_PATTERN.matcher(cpf).matches()) {
            throw new IllegalArgumentException("Formato de CPF inválido: deve conter apenas dígitos.");
        }

        if (!calcularDigitosCPF(cpf)) {
            throw new IllegalArgumentException("CPF inválido: os dígitos de verificação não correspondem.");
        }
    }

    private static void validarCNPJ(String cnpj) {
        String cnpjLimpo = cnpj.replaceAll("[\\.\\/\\-]", "").toUpperCase();

        if (!CNPJ_PATTERN.matcher(cnpjLimpo).matches()) {
            throw new IllegalArgumentException("Formato CNPJ inválido: deve conter 12 dígitos alfanuméricos + 2 dígitos numéricos.");
        }

        if (!calcularDigitosValidoCNPJ(cnpjLimpo)) {
            throw new IllegalArgumentException("CNPJ inválido: os dígitos de verificação não coincidem.");
        }
    }

    private static boolean calcularDigitosCPF(String cpf) {
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int dv1 = 11 - (soma % 11);
        dv1 = dv1 > 9 ? 0 : dv1;

        if (dv1 != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int dv2 = 11 - (soma % 11);
        dv2 = dv2 > 9 ? 0 : dv2;

        return dv2 == Character.getNumericValue(cpf.charAt(10));
    }

    private static boolean calcularDigitosValidoCNPJ(String cnpj) {
        String base = cnpj.substring(0, 12);
        char dv1 = cnpj.charAt(12);
        char dv2 = cnpj.charAt(13);

        char calcDv1 = calcularDV(base, new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
        if (calcDv1 != dv1) return false;

        char calcDv2 = calcularDV(base + calcDv1, new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
        return calcDv2 == dv2;
    }

    private static char calcularDV(String parte, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < parte.length(); i++) {
            char c = parte.charAt(i);
            int valor = converterCharParaValor(c);
            soma += valor * pesos[i];
        }

        int resto = soma % 11;
        int resultado = 11 - resto;

        if (resultado > 9) {
            return '0';
        }
        return Character.forDigit(resultado, 10);
    }

    private static int converterCharParaValor(char c) {
        return switch (c) {
            case '0' -> 0;
            case '1' -> 1;
            case '2' -> 2;
            case '3' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            case '9' -> 9;
            default -> (c - 'A') + 10; // A=10, B=11, ..., Z=35
        };
    }
}
