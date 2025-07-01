package com.fabiogomesrocha.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Arrays;

@ApplicationScoped
public class JwtService {

    private PrivateKey loadPrivateKey() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("private-key.pem");
            if (is == null) {
                throw new RuntimeException("Arquivo private-key.pem não encontrado");
            }

            String keyPem = new String(is.readAllBytes())
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] keyBytes = Base64.getDecoder().decode(keyPem);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar chave privada RSA", e);
        }
    }

    public String generateToken(Long userId, String username, String role) {
        try {
            JwtClaims claims = new JwtClaims();
            claims.setIssuer("http://localhost:8084");
            claims.setGeneratedJwtId();
            claims.setIssuedAtToNow();
            claims.setExpirationTimeMinutesInTheFuture(60); // Token expira em 60 min
            claims.setSubject(username);
            claims.setClaim("userId", userId);
            claims.setClaim("upn", username);
            claims.setStringListClaim("groups", Arrays.asList(role));

            JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(claims.toJson());
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256); // RS256
            jws.setKey(loadPrivateKey());

            return jws.getCompactSerialization();

        } catch (JoseException e) {
            throw new RuntimeException("Erro ao gerar token JWT com RS256", e);
        }
    }
}
