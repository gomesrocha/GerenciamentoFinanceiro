package com.fabiogomesrocha.infra;

import com.fabiogomesrocha.repository.AppConfigRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class WhatsappService {

    private static final Logger LOG = Logger.getLogger(WhatsappService.class);
    private final HttpClient client = HttpClient.newHttpClient();

    @Inject
    AppConfigRepository configRepository;

    public void enviarMensagem(String telefone, String mensagem) {
        try {
            String apiUrl = configRepository.findValueByKey("API_URL");
            String apiToken = configRepository.findValueByKey("API_TOKEN");

            if (apiUrl == null || apiToken == null) {
                LOG.error("Configurações de API do WhatsApp não encontradas no banco.");
                return;
            }

            String json = String.format("""
                {
                  "to": "%s",
                  "body": "%s"
                }
                """, telefone, mensagem);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Authorization", "Bearer " + apiToken)
                    .header("accept", "application/json")
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                LOG.error("Erro ao enviar WhatsApp: " + response.body());
            } else {
                LOG.info("Mensagem WhatsApp enviada com sucesso: " + response.body());
            }

        } catch (Exception e) {
            LOG.error("Erro no envio do WhatsApp", e);
        }
    }

    public void enviarParaNumeroPadrao(String mensagem) {
        String numero = configRepository.findValueByKey("DEFAULT_PHONE");
        if (numero != null) {
            enviarMensagem(numero, mensagem);
        } else {
            LOG.error("Número padrão não configurado no banco de dados.");
        }
    }
}
