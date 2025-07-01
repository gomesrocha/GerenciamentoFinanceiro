package com.fabiogomesrocha.endpoint;

import com.fabiogomesrocha.infra.EmailService;
import com.fabiogomesrocha.infra.WhatsappService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/test/notify")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationTestEndpoint {

    @Inject
    EmailService emailService;

    @Inject
    WhatsappService whatsappService;

    @POST
    @Path("/email")
    public Response testEmail(@QueryParam("to") String to) {
        emailService.enviar(
                to != null ? to : "seuemail@exemplo.com",
                "📧 Teste de e-mail",
                "Este é um teste de envio de e-mail via Quarkus!"
        );
        return Response.ok().entity("Email enviado com sucesso.").build();
    }

    @POST
    @Path("/whatsapp")
    public Response testWhatsapp(@QueryParam("phone") String phone) {
        whatsappService.enviarMensagem(
                phone != null ? phone : "557991620416",
                "🟢 Teste de envio de mensagem via WhatsApp pelo sistema!"
        );
        return Response.ok().entity("WhatsApp enviado com sucesso.").build();
    }
}
