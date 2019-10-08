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

import model.Agent;


@Path("/agent")
public class AgentRestService {

	// http://localhost:9090/JSP-DAY4/rs/agent/getallagents
	@GET
	@Path("/getallagents")
    @Produces(MediaType.APPLICATION_JSON)
	public String getAllAgents() 
	{
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("JSP-DAY4");
		EntityManager em = factory.createEntityManager();
		Query query = (Query) em.createQuery("select a from Agent a");
		List<Agent> list = (List<Agent>) query.getResultList();
		Gson gson = new Gson();
		Type type = new TypeToken<List<Agent>>() {}.getType();
		return gson.toJson(list, type);
	}
	
	// http://localhost:9090/JSP-DAY4/rs/agent/getagencyagents/{agencyid}
	@GET
	@Path("/getagencyagents/{agencyid}")
    @Produces(MediaType.APPLICATION_JSON)
	public String getAgencyAgents(@PathParam("agencyid") int agencyid) 
	{
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("JSP-DAY4");
		EntityManager em = factory.createEntityManager();
		Query query = (Query) em.createQuery("select a from Agent a where a.agencyId=" + agencyid);
		List<Agent> list = (List<Agent>) query.getResultList();
		Gson gson = new Gson();
		Type type = new TypeToken<List<Agent>>() {}.getType();
		
		return gson.toJson(list, type);
	}
	
	// http://localhost:9090/JSP-DAY4/rs/agent/getagent/{agentid}
	@GET
	@Path("/getagent/{agentid}")
    @Produces(MediaType.APPLICATION_JSON)
	public String getAgent(@PathParam("agentid") int agentId) 
	{
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("JSP-DAY4");
		EntityManager em = factory.createEntityManager();
		Query query = em.createQuery("select a from Agent a where a.agentId=" + agentId);
		Agent agent = (Agent) query.getSingleResult();
		Gson gson = new Gson();
		Type type = new TypeToken<Agent>() {}.getType();
		return gson.toJson(agent, type);
	}
	
	// http://localhost:9090/JSP-DAY4/rs/agent/postagent
	@POST
	@Path("/postagent")
    @Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String postAgent(String jsonString) 
	{
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("JSP-DAY4");
		EntityManager em = factory.createEntityManager();
		Gson gson = new Gson();
		Type type = new TypeToken<Agent>() {}.getType();
		Agent agent = gson.fromJson(jsonString, type);
		em.getTransaction().begin();
		Agent result = em.merge(agent);
		em.getTransaction().commit();
		em.close();
		return "Updates complete";
	}

	// http://localhost:9090/JSP-DAY4/rs/agent/putagent
	@PUT
	@Path("/putagent")
    @Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String putAgent(String jsonString) 
	{
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("JSP-DAY4");
		EntityManager em = factory.createEntityManager();
		Gson gson = new Gson();
		Agent a = gson.fromJson(jsonString, Agent.class);
		em.getTransaction().begin();
		em.persist(a);
		em.getTransaction().commit();
		return "Agent added to the system";
	}

	// http://localhost:9090/JSP-DAY4/rs/agent/deleteagent/{agentid}
	@DELETE
	@Path("/deleteagent/{agentid}")
	public String deleteAgent(@PathParam("agentid") int agentId) 
	{
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("JSP-DAY4");
		EntityManager em = factory.createEntityManager();
		Agent foundAgent = em.find(Agent.class, agentId);
		em.getTransaction().begin();
		em.remove(foundAgent);
		// check if deleted
		if (em.contains(foundAgent))
		{
			em.getTransaction().rollback();
			em.close();
			return "Could not delete agent";
		}
		else
		{
			em.getTransaction().commit();
			em.close();
			return "Agent successfully removed";
		}
	}
	
}
