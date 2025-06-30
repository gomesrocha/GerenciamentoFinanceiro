package com.fabiogomesrocha.endpoint;

import com.fabiogomesrocha.model.BankAccount;
import com.fabiogomesrocha.service.BankAccountService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.ws.rs.core.SecurityContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Path("/bank-accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BankAccountEndpoint {

    @GET
    @Path("/me")
    @RolesAllowed("admin")
    public Response getUserInfo() {
        return Response.ok(Map.of(
                "user", identity.getPrincipal().getName(), // vai vir o client_id
                "roles", identity.getRoles()
        )).build();
    }

    @GET
    @Path("/debug-jwt")
    @RolesAllowed("admin")
    public Response debugToken(@Context SecurityContext ctx) {
        return Response.ok(Map.of(
                "name", ctx.getUserPrincipal().getName()
        )).build();
    }


    @Inject
    SecurityIdentity identity;


    @Inject
    BankAccountService bankAccountService;

    private String getCurrentUserId() {
        return identity.getPrincipal().getName(); // retorna o "sub" do usuário logado
    }

    @GET
    @RolesAllowed("admin")
    public Response getAll() {
        List<BankAccount> banks = bankAccountService.listAll();
        return Response.ok(banks).build();
    }


    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        BankAccount bank = bankAccountService.findById(id);
        if (bank == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(bank).build();
    }


    @POST
    public Response create(BankAccount bankAccount) {

        BankAccount created = bankAccountService.create(bankAccount);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, BankAccount bankAccount) {
        BankAccount existing = bankAccountService.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        // Atualiza apenas campos permitidos
        existing.bank = bankAccount.bank;
        existing.agency = bankAccount.agency;
        existing.account = bankAccount.account;
        existing.type = bankAccount.type;
        existing.balance = bankAccount.balance;
        existing.persist();
        return Response.ok(existing).build();
    }


    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        bankAccountService.delete(id);
        return Response.noContent().build();
    }


    // EXTRAS
    @GET
    @Path("/search/by-type")
    public Response findByType(@QueryParam("type") String type) {
        return Response.ok(bankAccountService.findByType(type)).build();
    }

    @GET
    @Path("/search/by-balance-range")
    public Response findByBalance(
            @QueryParam("min") BigDecimal min,
            @QueryParam("max") BigDecimal max) {
        return Response.ok(bankAccountService.findByBalanceRange(min, max)).build();
    }

    @GET
    @Path("/total-balance")
    public Response getTotalBalance() {
        String userId = getCurrentUserId();
        return Response.ok(bankAccountService.totalBalance()).build();
    }

}
