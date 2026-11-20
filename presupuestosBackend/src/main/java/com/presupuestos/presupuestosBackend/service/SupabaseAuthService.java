
package com.presupuestos.presupuestosBackend.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.presupuestos.presupuestosBackend.exception.AccesoDenegadoException;

@Service
public class SupabaseAuthService {

    private final RestClient restClient;

    public SupabaseAuthService(
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.publishable-key}") String publishableKey) {

        System.out.println("URL BASE SUPABASE: [" + supabaseUrl + "]");


        this.restClient = RestClient.builder()
                .baseUrl(supabaseUrl)
                .defaultHeader("apikey", publishableKey)
                .build();
    }

    public void verificarUsuario(
            String accessToken,
            UUID authUserId,
            String emailAutenticado) {

        Map<?, ?> usuarioSupabase;

        try {
            usuarioSupabase = restClient.get()
                    .uri("/auth/v1/user")
                    .headers(headers -> headers.setBearerAuth(accessToken))
                    .retrieve()
                    .body(Map.class);

        } catch (RestClientException e) {
            System.err.println(
                "Error al consultar Supabase: " + e.getClass().getSimpleName()
            );

            if (e instanceof org.springframework.web.client.RestClientResponseException ex) {
                System.err.println("Estado HTTP de Supabase: " + ex.getStatusCode());
            }

            throw new AccesoDenegadoException(
                "No se ha podido verificar la identidad con Supabase"
            );
        }

        if (usuarioSupabase == null) {
            throw new AccesoDenegadoException(
                    "Supabase no ha devuelto ningún usuario"
            );
        }

        Object id = usuarioSupabase.get("id");
        Object email = usuarioSupabase.get("email");
        Object emailConfirmado = usuarioSupabase.get("email_confirmed_at");

        if (id == null ||
            !authUserId.toString().equals(id.toString())) {
            throw new AccesoDenegadoException(
                    "La identidad de Supabase no coincide"
            );
        }

        if (emailAutenticado == null ||
            !(email instanceof String) ||
            !((String) email).equalsIgnoreCase(emailAutenticado.trim())) {
            throw new AccesoDenegadoException(
                    "El correo autenticado no coincide"
            );
        }

        if (!(emailConfirmado instanceof String) ||
            ((String) emailConfirmado).isBlank()) {
            throw new AccesoDenegadoException(
                    "Debes confirmar tu correo electrónico"
            );
        }
    }
}
