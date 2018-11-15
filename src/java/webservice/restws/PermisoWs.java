package webservice.restws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.FuncionarioDaoImp;
import dao.PermisoDaoImp;
import dao.UsuarioDaoImp;
import dto.FuncionarioDto;
import dto.PermisoDto;
import dto.UsuarioDto;
import java.util.LinkedList;
import java.util.Objects;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class with rest web services. It allows: document validation, to send a document to SII, to preview a document, and to download the PDF of the
 * document.
 *
 */
@Path("/permiso")
public class PermisoWs {

    private static final Logger LOG = LoggerFactory.getLogger(PermisoWs.class);

    @GET
    @Path("/testws")
    @Produces({MediaType.APPLICATION_JSON})
    public Response testWS() {
        LOG.info("TESTING PERMISO WEBSERVICE ");
        //JsonProducer jp = new JsonProducer();
        //String jsonString = (String)jp.listToJSONString();
        return Response.ok("{\"status\":\"success\"}", MediaType.APPLICATION_JSON + ";charset=UTF-8").build();

    }

    @POST
    @Path("/requestDashboardInfo")
    @Consumes("application/json")
    @Produces("application/json")
    public Response requestDashboardInfo(@HeaderParam("accessToken") String accessToken, String content) {
        LOG.info("Request for Peticiones Info");
        LOG.info(String.format("token: {} content: {}", accessToken, content));

        Gson jsonConstructor = new GsonBuilder().setPrettyPrinting().create();
        JsonObject response = new JsonObject();

        //if(accessToken.equalsIgnoreCase(Config.get("ACCESS_TOKEN"))){
        if (accessToken.equalsIgnoreCase("password")) {
            LOG.info("correct access token");
            try {
                JsonObject request = new JsonParser().parse(content).getAsJsonObject();

                LOG.info(request.toString());
                int run = Integer.parseInt(request.get("rut").getAsString().split("-")[0]);
                PermisoDaoImp pdi = new PermisoDaoImp();
                LinkedList<PermisoDto> list = pdi.buscarPermisos(run);
                if (!list.isEmpty()) {
                    PermisoDto lastPermiso = list.getLast();
                    JsonObject lastPermisoJsonO = new JsonObject();
                    lastPermisoJsonO.addProperty("permisoId", lastPermiso.getId());
                    lastPermisoJsonO.addProperty("permisoFecha", lastPermiso.getFechaSolicitud().toString());
                    lastPermisoJsonO.addProperty("permisoStatus", lastPermiso.getEstado());
                    response.add("lastPermiso", lastPermisoJsonO);

                    int aceptadas = 0;
                    int rechazadas = 0;
                    int pendientes = 0;
                    for (PermisoDto pdo : list) {
                        LOG.info("Estado Solicitud: " + pdo.getEstado());
                        switch (pdo.getEstado()) {
                            case 0:
                                rechazadas++;
                                break;
                            case 1:
                                aceptadas++;
                                break;
                            case 2:
                                pendientes++;
                                break;
                            default:
                                LOG.warn("Different Value");
                                break;
                        }
                    }
                    response.addProperty("solicitudesAceptadas", aceptadas);
                    response.addProperty("solicitudesRechazadas", rechazadas);
                    response.addProperty("solicitudesPendientes", pendientes);
                }
                response.addProperty("response", "success");
                return Response.ok(jsonConstructor.toJson(response), MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
            } catch (Exception ex) {
                LOG.info("There was an exception: " + ex.toString());
                response.addProperty("response", "failed");
                response.addProperty("message", "Internal Server Error: " + ex.toString());
                return Response.ok(jsonConstructor.toJson(response), MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
            }
        } else {
            LOG.info("invalid access token");
            response.addProperty("response", "failed");
            response.addProperty("message", "Invalid Access Token");
            return Response.ok(jsonConstructor.toJson(response), MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
        }

    }

    @POST
    @Path("/requestPeticionesList")
    @Consumes("application/json")
    @Produces("application/json")
    public Response requestPeticionesList(@HeaderParam("accessToken") String accessToken, String content) {
        LOG.info("Request for Peticiones List");
        LOG.info(String.format("token: {} content: {}", accessToken, content));

        Gson jsonConstructor = new GsonBuilder().setPrettyPrinting().create();
        JsonObject response = new JsonObject();

        //if(accessToken.equalsIgnoreCase(Config.get("ACCESS_TOKEN"))){
        if (accessToken.equalsIgnoreCase("password")) {
            LOG.info("correct access token");
            try {
                JsonObject request = new JsonParser().parse(content).getAsJsonObject();

                LOG.info(request.toString());

                PermisoDaoImp pdi = new PermisoDaoImp();
                LinkedList<PermisoDto> list = new LinkedList<PermisoDto>();
                if(request.get("rut") != null){
                    list = pdi.buscarPermisos(Integer.parseInt(request.get("rut").getAsString().split("-")[0]));
                }else{
                    list = pdi.listPermisos();
                }
                JsonArray permisoJsonArray = new JsonArray();
                if (!list.isEmpty()) {
                    for (PermisoDto pdo : list) {
                        JsonObject permisoJsonO = new JsonObject();
                        permisoJsonO.addProperty("permisoId", pdo.getId());
                        permisoJsonO.addProperty("permisoFunc", pdo.getSolicitante().getRun()+"-"+pdo.getSolicitante().getDv());
                        permisoJsonO.addProperty("permisoType", pdo.getTipo());
                        permisoJsonO.addProperty("permisoFechaSol", pdo.getFechaSolicitud().toString());
                        permisoJsonO.addProperty("permisoFechaIni", pdo.getFechaInicio().toString());
                        permisoJsonO.addProperty("permisoFechaFin", pdo.getFechaTermino().toString());
                        permisoJsonO.addProperty("permisoStatus", pdo.getEstado());
                        permisoJsonO.addProperty("permisoAut", pdo.getAutorizante() == null ? "N/A" : pdo.getAutorizante().getRun()+"-"+pdo.getAutorizante().getDv());
                        permisoJsonArray.add(permisoJsonO);
                    }
                    response.add("permisoList", permisoJsonArray);
                }
                response.addProperty("response", "success");
                return Response.ok(jsonConstructor.toJson(response), MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
            } catch (Exception ex) {
                LOG.info("There was an exception: " + ex.toString());
                response.addProperty("response", "failed");
                response.addProperty("message", "Internal Server Error: " + ex.toString());
                return Response.ok(jsonConstructor.toJson(response), MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
            }
        } else {
            LOG.info("invalid access token");
            response.addProperty("response", "failed");
            response.addProperty("message", "Invalid Access Token");
            return Response.ok(jsonConstructor.toJson(response), MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
        }

    }
 
}
