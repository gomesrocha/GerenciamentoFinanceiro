package com.fabiogomesrocha.infra;

import com.fabiogomesrocha.model.Transaction;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class EmailService {
    @Inject
    Mailer mailer;

    @Inject
    @Location("email-transacoes.html")
    Template emailTransacoes;

    public void enviar(String destinatario, String assunto, String corpo) {
        mailer.send(Mail.withText(destinatario, assunto, corpo));
    }

    public void enviarHtml(String destino, String assunto, String textoSimples, String corpoHtml) {
        mailer.send(
                Mail.withHtml(destino, assunto, corpoHtml)
                        .setText(textoSimples)
        );
    }

    public void enviarResumoTransacoes(String destino, List<Transaction> transacoes) {
        if (transacoes == null || transacoes.isEmpty()) return;

        BigDecimal total = transacoes.stream()
                .map(t -> t.type == Transaction.Type.DEBIT ? t.value.negate() : t.value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        TemplateInstance template = emailTransacoes
                .data("transacoes", transacoes)
                .data("total", total);

        String html = template.render();

        String textoSimples = String.format("Você teve %d transações hoje. Total: R$ %.2f",
                transacoes.size(), total);

        mailer.send(
                Mail.withHtml(destino, "Resumo diário de transações", html)
                        .setText(textoSimples)
        );
    }
}
