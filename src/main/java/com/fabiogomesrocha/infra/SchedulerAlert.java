package com.fabiogomesrocha.infra;

import com.fabiogomesrocha.model.Transaction;
import com.fabiogomesrocha.infra.EmailService;
import com.fabiogomesrocha.infra.WhatsappService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import io.quarkus.scheduler.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@ApplicationScoped
public class SchedulerAlert {

    @Inject
    WhatsappService whatsappService;

    @Inject
    EmailService emailService;

    @ConfigProperty(name = "monitor.email.destino", defaultValue = "gomesrocha@gmail.com")
    String emailDestino;

    @ConfigProperty(name = "monitor.whatsapp.destino", defaultValue = "557991620416")
    String numeroWhatsapp;

    private static final Logger LOG = Logger.getLogger(SchedulerAlert.class);

    // 🕗 Diariamente às 8h – Enviar email com transações do dia
    @Transactional
    @Scheduled(cron = "0 0 8 * * ?")
    public void enviarEmailDiario() {
        LocalDate hoje = LocalDate.now();
        List<Transaction> transacoesHoje = Transaction.find("date = ?1", hoje).list();

        if (!transacoesHoje.isEmpty()) {
            LOG.info("Enviando resumo diário de transações...");
            emailService.enviarResumoTransacoes(emailDestino, transacoesHoje);
        } else {
            LOG.info("Sem transações hoje.");
        }
    }

    // 📆 Semanalmente às segundas – Enviar WhatsApp com status da semana anterior
    @Transactional
    @Scheduled(cron = "0 0 9 ? * MON") // Toda segunda às 9h
    public void enviarWhatsappSemanal() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemana = hoje.minusDays(7);

        List<Transaction> transacoesSemana = Transaction.find("date >= ?1 AND date <= ?2", inicioSemana, hoje).list();

        if (!transacoesSemana.isEmpty()) {
            BigDecimal total = transacoesSemana.stream()
                    .map(t -> t.type == Transaction.Type.DEBIT ? t.value.negate() : t.value)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            String resumo = String.format(
                    "📊 Resumo semanal:\n- %d transações\n- Total: R$ %.2f",
                    transacoesSemana.size(), total
            );
            whatsappService.enviarMensagem(numeroWhatsapp, resumo);
        } else {
            whatsappService.enviarMensagem(numeroWhatsapp, "📭 Sem transações na semana passada.");
        }
    }

    // 📅 Último dia do mês às 7h – Enviar relatório mensal por email
    @Transactional
    @Scheduled(cron = "0 0 7 L * ?")
    public void enviarEmailMensal() {
        YearMonth mesAtual = YearMonth.now();
        LocalDate primeiroDia = mesAtual.atDay(1);
        LocalDate ultimoDia = mesAtual.atEndOfMonth();

        List<Transaction> transacoesMes = Transaction.find("date >= ?1 AND date <= ?2", primeiroDia, ultimoDia).list();

        if (!transacoesMes.isEmpty()) {
            LOG.info("Enviando relatório mensal...");
            emailService.enviarResumoTransacoes(emailDestino, transacoesMes);
        } else {
            LOG.info("Nenhuma transação no mês atual.");
        }
    }
}
