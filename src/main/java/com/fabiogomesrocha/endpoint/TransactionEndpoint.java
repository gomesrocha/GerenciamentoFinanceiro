package com.fabiogomesrocha.endpoint;

import com.fabiogomesrocha.model.Transaction;
import com.fabiogomesrocha.service.TransactionService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionEndpoint {

    @Inject
    TransactionService transactionService;

    // Listar todas as transações de uma conta bancária
    @GET
    @Path("/by-bank/{bankId}")
    public Response getByBankAccount(@PathParam("bankId") Long bankId) {
        List<Transaction> transactions = transactionService.listByBank(bankId);
        return Response.ok(transactions).build();
    }

    // Criar nova transação (débito/crédito)
    @POST
    public Response create(Transaction transaction) {
        try {
            transactionService.save(transaction);
            return Response.status(Response.Status.CREATED).entity(transaction).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(
                    "{\"error\": \"" + e.getMessage() + "\"}"
            ).build();
        }
    }

    // Total de créditos do mês atual
    @GET
    @Path("/monthly-credit")
    public Response getCreditOfMonth() {
        BigDecimal total = transactionService.creditMonth();
        return Response.ok(
                new Info("CREDIT", total)
        ).build();
    }

    // Total de débitos do mês atual
    @GET
    @Path("/monthly-debit")
    public Response getDebitOfMonth() {
        BigDecimal total = transactionService.debitMonth();
        return Response.ok(
                new Info("DEBIT", total)
        ).build();
    }

    // Classe interna auxiliar para resposta
    public static class Info {
        public String type;
        public BigDecimal total;

        public Info(String type, BigDecimal total) {
            this.type = type;
            this.total = total;
        }
    }
}
