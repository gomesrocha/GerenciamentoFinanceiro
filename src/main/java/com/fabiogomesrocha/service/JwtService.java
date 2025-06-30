package com.fabiogomesrocha.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@ApplicationScoped
public class JwtService {

    private static final String SECRET = "suaSuperChaveUltraSecretaSegura123";

    public String generateToken(String username, String role) {
        try {
            JwtClaims claims = new JwtClaims();
            claims.setIssuer("http://localhost:8084");
            claims.setGeneratedJwtId();
            claims.setIssuedAtToNow();
            claims.setExpirationTimeMinutesInTheFuture(60);
            claims.setSubject(username);
            claims.setClaim("upn", username);
            claims.setStringListClaim("groups", Arrays.asList(role));

            JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(claims.toJson());
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256); // HS256
            jws.setKey(new HmacKey(SECRET.getBytes(StandardCharsets.UTF_8)));
            jws.setDoKeyValidation(false); // útil no dev

            return jws.getCompactSerialization();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar token JWT com HS256", e);
        }
    }
}
