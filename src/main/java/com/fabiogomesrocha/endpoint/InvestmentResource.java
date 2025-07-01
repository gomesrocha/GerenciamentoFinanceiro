package com.fabiogomesrocha.endpoint;

import com.fabiogomesrocha.model.Investiment;
import com.fabiogomesrocha.model.InvestmentValue;
import com.fabiogomesrocha.service.InvestmentService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Path("/investments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InvestmentResource {

    @Inject
    InvestmentService investmentService;

    @GET
    public List<Investiment> listAll() {
        return investmentService.listAll();
    }

    @POST
    public Response create(Investiment investiment) {
        Investiment created = investmentService.create(investiment);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @POST
    @Path("/{id}/values")
    public Response addValue(@PathParam("id") Long id, Map<String, String> body) {
        LocalDate date = LocalDate.parse(body.get("date"));
        BigDecimal value = new BigDecimal(body.get("value"));

        InvestmentValue val = investmentService.addValue(id, date, value);
        return Response.status(Response.Status.CREATED).entity(val).build();
    }

    @GET
    @Path("/{id}/evolution")
    public List<InvestmentValue> evolution(@PathParam("id") Long id) {
        return investmentService.getEvolution(id);
    }
}
