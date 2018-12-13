package webservice.restws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.FuncionarioDaoImp;
import dao.PermisoDaoImp;
import dao.UnidadDaoImp;
import dao.UsuarioDaoImp;
import dto.FuncionarioDto;
import dto.PermisoDto;
import dto.UnidadDto;
import dto.UsuarioDto;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@Path("/funcionario")
public class FuncionarioWs {

    private static final Logger LOG = LoggerFactory.getLogger(FuncionarioWs.class);

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
    @Path("/requestList")
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

                FuncionarioDaoImp fdi = new FuncionarioDaoImp();

                LinkedList<FuncionarioDto> list = new LinkedList<FuncionarioDto>();
                if (request.get("type").getAsString().equalsIgnoreCase("Administrador")) {
                    list = fdi.listarFuncionarios();
                } else if (request.get("type").getAsString().equalsIgnoreCase("Encargado")) {
                    UnidadDaoImp udi = new UnidadDaoImp();
                    UnidadDto udto = udi.buscarPorJefe(request.get("rut").getAsInt());
                    list = fdi.listarFuncionariosDeUnidad(udto.getId());
                }
                JsonArray funcionarioJsonArray = new JsonArray();
                if (!list.isEmpty()) {
                    for (FuncionarioDto fdo : list) {
                        JsonObject funcionarioJsonO = new JsonObject();
                        funcionarioJsonO.addProperty("rut", fdo.getRun() + "-" + fdo.getDv());
                        funcionarioJsonO.addProperty("nombre", fdo.getNombre() + " " + fdo.getApellidoPaterno() + " " + fdo.getApellidoMaterno());
                        funcionarioJsonO.addProperty("fechaNac", fdo.getFechaNacimiento().toString());
                        funcionarioJsonO.addProperty("cargo", fdo.getCargo());
                        funcionarioJsonO.addProperty("unidad", fdo.getUnidad().getNombre());
                        funcionarioJsonArray.add(funcionarioJsonO);

                    }
                    response.add("funcionarioList", funcionarioJsonArray);
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

    @GET
    @Path("/requestView")
    @Produces("application/json")
    public Response requestFuncionarioView(@HeaderParam("accessToken") String accessToken,
            @QueryParam("rut") String rut) {
        LOG.info("Request for view funcionario");
        LOG.info(String.format("token: {}", accessToken));

        Gson jsonConstructor = new GsonBuilder().setPrettyPrinting().create();
        JsonObject response = new JsonObject();

        if (accessToken.equalsIgnoreCase("password")) {
            LOG.info("correct access token");
            try {
                LOG.info("Received parameters: rut=" + rut);
                FuncionarioDaoImp fdi = new FuncionarioDaoImp();
                FuncionarioDto fdto = new FuncionarioDto();
                fdto.setRun(Integer.parseInt(rut));
                fdto = fdi.buscar(fdto);
                JsonObject funcionario = new JsonObject();
                funcionario.addProperty("rut", fdto.getRun() + "-" + fdto.getDv());
                funcionario.addProperty("nombre", fdto.getNombre() + " " + fdto.getApellidoPaterno() + " " + fdto.getApellidoMaterno());
                funcionario.addProperty("fecNac", fdto.getFechaNacimiento().toString());
                funcionario.addProperty("correo", fdto.getCorreo());
                funcionario.addProperty("direccion", fdto.getDireccion());
                funcionario.addProperty("cargo", fdto.getCargo());
                funcionario.addProperty("unidad", fdto.getUnidad().getNombre());
                response.add("funcionario", funcionario);

                PermisoDaoImp pdi = new PermisoDaoImp();
                LinkedList<PermisoDto> list = new LinkedList();
                list = pdi.buscarPermisos(Integer.parseInt(rut));
                JsonArray permisoJsonArray = new JsonArray();
                for (PermisoDto pdo : list) {
                    LOG.info("permisos found. Returning permisos.");
                    JsonObject permisoJsonO = new JsonObject();
                    permisoJsonO.addProperty("permisoId", pdo.getId());
                    permisoJsonO.addProperty("permisoFunc", pdo.getSolicitante().getRun() + "-" + pdo.getSolicitante().getDv());
                    permisoJsonO.addProperty("permisoType", pdo.getTipo());
                    permisoJsonO.addProperty("permisoFechaSol", pdo.getFechaSolicitud().toString());
                    permisoJsonO.addProperty("permisoFechaIni", pdo.getFechaInicio().toString());
                    permisoJsonO.addProperty("permisoFechaFin", pdo.getFechaTermino().toString());
                    permisoJsonO.addProperty("permisoStatus", pdo.getEstado());
                    permisoJsonO.addProperty("permisoAut", pdo.getAutorizante() == null ? "N/A" : pdo.getAutorizante().getRun() + "-" + pdo.getAutorizante().getDv());
                    permisoJsonArray.add(permisoJsonO);
                }
                response.add("permisoList", permisoJsonArray);
                response.addProperty("response", "success");
                return Response.ok(jsonConstructor.toJson(response), MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
            } catch (Exception ex) {
                LOG.info("There was an exception: " + ex.toString());
                response.addProperty("response", "failed");
                response.addProperty("message", "Internal Server Error: " + ex.getLocalizedMessage());
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
