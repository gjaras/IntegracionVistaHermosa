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
@Path("/usuario")
public class UsuarioWs {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioWs.class);

    @GET
    @Path("/testws")
    @Produces({MediaType.APPLICATION_JSON})
    public Response testWS() {
        LOG.info("TESTING USUARIO WEBSERVICE ");
        //JsonProducer jp = new JsonProducer();
        //String jsonString = (String)jp.listToJSONString();
        return Response.ok("{\"status\":\"success\"}", MediaType.APPLICATION_JSON + ";charset=UTF-8").build();

    }

    @POST
    @Path("/requestAuth")
    @Consumes("application/json")
    @Produces("application/json")
    public Response requestAuth(@HeaderParam("accessToken") String accessToken, String content) {
        LOG.info("Request for Auth initiated");
        LOG.info(String.format("token: {} content: {}", accessToken, content));

        Gson jsonConstructor = new GsonBuilder().setPrettyPrinting().create();
        JsonObject response = new JsonObject();

        //if(accessToken.equalsIgnoreCase(Config.get("ACCESS_TOKEN"))){
        if (accessToken.equalsIgnoreCase("password")) {
            LOG.info("correct access token");
            try {
                JsonObject request = new JsonParser().parse(content).getAsJsonObject();

                LOG.info(request.toString());
                UsuarioDaoImp udi = new UsuarioDaoImp();
                UsuarioDto autenticarUsuario = udi.autenticarUsuario(request.get("name").getAsString(), request.get("pass").getAsString());
                String result = autenticarUsuario.getClave() + " " + autenticarUsuario.getNombre();
                if (autenticarUsuario.getClave() != null && autenticarUsuario.getNombre() != null) {
                    FuncionarioDaoImp fdi = new FuncionarioDaoImp();
                    response.addProperty("response", "success");
                    response.addProperty("message", "authenticated");
                    response.addProperty("rut", autenticarUsuario.getFuncionario().getRun() + "-" + autenticarUsuario.getFuncionario().getDv());
                    response.addProperty("nombre", autenticarUsuario.getFuncionario().getNombre() + " " + autenticarUsuario.getFuncionario().getApellidoPaterno());
                    response.addProperty("tipo", autenticarUsuario.getTipoUsuario());
                } else {
                    response.addProperty("response", "success");
                    response.addProperty("message", "Invalid Username and/or Password");
                }
                return Response.ok(jsonConstructor.toJson(response), MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
            } catch (Exception ex) {
                LOG.info("There was an exception: " + ex.getLocalizedMessage());
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

    @GET
    @Path("/requestList")
    @Produces("application/json")
    public Response requestUserList(@HeaderParam("accessToken") String accessToken) {
        LOG.info("Request for User List Initiated");
        LOG.info(String.format("token: {}", accessToken));

        Gson jsonConstructor = new GsonBuilder().setPrettyPrinting().create();
        JsonObject response = new JsonObject();

        //if(accessToken.equalsIgnoreCase(Config.get("ACCESS_TOKEN"))){
        if (accessToken.equalsIgnoreCase("password")) {
            LOG.info("correct access token");
            try {
                UsuarioDaoImp udi = new UsuarioDaoImp();
                LinkedList<UsuarioDto> list = udi.listarUsuarios();
                JsonArray userJsonArray = new JsonArray();
                for (UsuarioDto udo : list) {
                    JsonObject member = new JsonObject();
                    member.addProperty("id", udo.getId());
                    member.addProperty("nombre", udo.getNombre());
                    member.addProperty("tipo", udo.getTipoUsuario());
                    member.addProperty("rut", udo.getFuncionario().getRun()+"-"+udo.getFuncionario().getDv());
                    userJsonArray.add(member);
                }
                response.add("users", userJsonArray);
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
    
    @GET
    @Path("/requestListWP")
    @Produces("application/json")
    public Response requestUserListWithParameters(@HeaderParam("accessToken") String accessToken, 
            @QueryParam("id") String id, @QueryParam("nombreUsr") String nombre, @QueryParam("tipoUsr") String tipo) {
        LOG.info("Request for User List With Parameters Initiated");
        LOG.info(String.format("token: {}", accessToken));

        Gson jsonConstructor = new GsonBuilder().setPrettyPrinting().create();
        JsonObject response = new JsonObject();

        //if(accessToken.equalsIgnoreCase(Config.get("ACCESS_TOKEN"))){
        if (accessToken.equalsIgnoreCase("password")) {
            LOG.info("correct access token");
            try {
                LOG.info("Received parameters: id="+id+" nombre="+nombre+" tipo="+tipo);
                UsuarioDaoImp udi = new UsuarioDaoImp();
                LinkedList<UsuarioDto> list = udi.listarUsuariosWithParameters(Objects.toString(id,""),Objects.toString(tipo,""),Objects.toString(nombre,""));
                JsonArray userJsonArray = new JsonArray();
                for (UsuarioDto udo : list) {
                    LOG.info("Members found");
                    JsonObject member = new JsonObject();
                    member.addProperty("id", udo.getId());
                    member.addProperty("nombre", udo.getNombre());
                    member.addProperty("tipo", udo.getTipoUsuario());
                    member.addProperty("rut", udo.getFuncionario().getRun()+"-"+udo.getFuncionario().getDv());
                    userJsonArray.add(member);
                }
                response.add("users", userJsonArray);
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

    @POST
    @Path("/create")
    @Produces("application/json")
    public Response createUser(@HeaderParam("accessToken") String accessToken, String content) {
        LOG.info("Request for Create User initiated");
        LOG.info(String.format("token: {} content: {}", accessToken, content));

        Gson jsonConstructor = new GsonBuilder().setPrettyPrinting().create();
        JsonObject response = new JsonObject();

        //if(accessToken.equalsIgnoreCase(Config.get("ACCESS_TOKEN"))){
        if (accessToken.equalsIgnoreCase("password")) {
            LOG.info("correct access token");

            JsonObject request = new JsonParser().parse(content).getAsJsonObject();
            try {
                UsuarioDaoImp udi = new UsuarioDaoImp();
                UsuarioDto udto = new UsuarioDto();
                FuncionarioDaoImp fdi = new FuncionarioDaoImp();
                FuncionarioDto fdto = fdi.buscarFuncionarioParcial(Integer.parseInt(request.get("rut").getAsString().split("-")[0]));
                LOG.info("Funcionario: " + fdto.toString());
                if (fdto.getRun() == -1) {
                    LOG.info("No Funcionario with that Rut found");
                    response.addProperty("response", "success");
                    response.addProperty("result", "failed");
                    response.addProperty("message", "User must be associated with an existing Funcionario");
                    return Response.ok(jsonConstructor.toJson(response), MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
                } else {
                    udto.setFuncionario(fdto);
                    udto.setNombre(request.get("nombre").getAsString());
                    udto.setClave(request.get("password").getAsString());
                    udto.setTipoUsuario(request.get("type").getAsString());

                    int result = udi.insertar(udto);
                    LOG.info("Insert Result: " + result);
                    if (result == 0) {
                        LOG.info("User Correctly Inserted");
                        response.addProperty("response", "success");
                        response.addProperty("result", "success");
                        return Response.ok(jsonConstructor.toJson(response), MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
                    } else {
                        LOG.info("Invalid Parameters");
                        response.addProperty("response", "failed");
                        response.addProperty("message", "Invalid Parameters");
                        return Response.ok(jsonConstructor.toJson(response), MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
                    }
                }
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

    @GET
    @Path("/requestSingleData")
    @Produces("application/json")
    public Response requestUserData(@HeaderParam("accessToken") String accessToken, @QueryParam("id") String id) {
        LOG.info("Request for User Data");
        LOG.info(String.format("token: {}", accessToken));

        Gson jsonConstructor = new GsonBuilder().setPrettyPrinting().create();
        JsonObject response = new JsonObject();

        //if(accessToken.equalsIgnoreCase(Config.get("ACCESS_TOKEN"))){
        if (accessToken.equalsIgnoreCase("password")) {
            LOG.info("correct access token");
            try {
                LOG.info("Received parameters: id="+id);
                UsuarioDaoImp udi = new UsuarioDaoImp();
                UsuarioDto uwi = new UsuarioDto();
                uwi.setId(Integer.parseInt(id));
                UsuarioDto udt = udi.buscar(uwi);
                response.addProperty("id",udt.getId());
                response.addProperty("nombre", udt.getNombre());
                response.addProperty("tipo", udt.getTipoUsuario());
                response.addProperty("rut", udt.getFuncionario().getRun()+"-"+udt.getFuncionario().getDv());
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
    
    @GET
    @Path("/delete")
    @Produces("application/json")
    public Response deleteUser(@HeaderParam("accessToken") String accessToken, @QueryParam("id") String id) {
        LOG.info("Request for Delete User");
        LOG.info(String.format("token: {}", accessToken));

        Gson jsonConstructor = new GsonBuilder().setPrettyPrinting().create();
        JsonObject response = new JsonObject();

        //if(accessToken.equalsIgnoreCase(Config.get("ACCESS_TOKEN"))){
        if (accessToken.equalsIgnoreCase("password")) {
            LOG.info("correct access token");
            try {
                LOG.info("Received parameters: id="+id);
                UsuarioDaoImp udi = new UsuarioDaoImp();
                UsuarioDto uwi = new UsuarioDto();
                uwi.setId(Integer.parseInt(id));
                int result = udi.eliminar(uwi);
                if(result == -1){
                    response.addProperty("result", "success");
                }else{
                    response.addProperty("result", "failed");
                }
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
