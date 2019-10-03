package main;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import model.Agency;


@Path("/agency")
public class AgencyRestService {

	// http://localhost:9090/JSP-DAY4/rs/agency/getallagencies
	@GET
	@Path("/getallagencies")
    @Produces(MediaType.APPLICATION_JSON)
	public String getAllAgencies() 
	{
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("JSP-DAY4");
		EntityManager em = factory.createEntityManager();
		Query query = (Query) em.createQuery("select a from Agency a");
		List<Agency> list = (List<Agency>) query.getResultList();
		Gson gson = new Gson();
		Type type = new TypeToken<List<Agency>>() {}.getType();
		return gson.toJson(list, type);
	}
	
	// http://localhost:9090/JSP-DAY4/rs/agency/getagency/{agencyid}
	@GET
	@Path("/getagency/{agency}")
    @Produces(MediaType.APPLICATION_JSON)
	public String getAgency(@PathParam("agencyid") int agencyId) 
	{
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("JSP-DAY4");
		EntityManager em = factory.createEntityManager();
		Query query = em.createQuery("select a from Agency a where a.agencyId=" + agencyId);
		Agency agency = (Agency) query.getSingleResult();
		Gson gson = new Gson();
		Type type = new TypeToken<Agency>() {}.getType();
		return gson.toJson(agency, type);
	}
	
	// http://localhost:9090/JSP-DAY4/rs/agency/postagency
	@POST
	@Path("/postagency")
    @Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String postAgency(String jsonString) 
	{
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("JSP-DAY4");
		EntityManager em = factory.createEntityManager();
		Gson gson = new Gson();
		Type type = new TypeToken<Agency>() {}.getType();
		Agency agency = gson.fromJson(jsonString, type);
		em.getTransaction().begin();
		Agency result = em.merge(agency);
		em.getTransaction().commit();
		em.close();
		return "Updates complete";
	}

	// http://localhost:9090/JSP-DAY4/rs/agency/putagency
	@PUT
	@Path("/putagency")
    @Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String putAgency(String jsonString) 
	{
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("JSP-DAY4");
		EntityManager em = factory.createEntityManager();
		Gson gson = new Gson();
		Agency a = gson.fromJson(jsonString, Agency.class);
		em.getTransaction().begin();
		em.persist(a);
		em.getTransaction().commit();
		return "Agency added to the system";
	}

	// http://localhost:9090/JSP-DAY4/rs/agency/deleteagency/{agencyid}
	@DELETE
	@Path("/deleteagency/{agencyid}")
	public String deleteAgency(@PathParam("agencyid") int agencyId) 
	{
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("JSP-DAY4");
		EntityManager em = factory.createEntityManager();
		Agency foundAgency = em.find(Agency.class, agencyId);
		em.getTransaction().begin();
		em.remove(foundAgency);
		// check if deleted
		if (em.contains(foundAgency))
		{
			em.getTransaction().rollback();
			em.close();
			return "Could not delete Agency";
		}
		else
		{
			em.getTransaction().commit();
			em.close();
			return "Agency successfully removed";
		}
	}
	
}
