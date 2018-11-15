package webservice.restws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.DocumentoDaoImp;
import dao.FuncionarioDaoImp;
import dao.PermisoDaoImp;
import dao.UsuarioDaoImp;
import dto.DocumentoDto;
import dto.FuncionarioDto;
import dto.PermisoDto;
import dto.UsuarioDto;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
@Path("/documento")
public class DocumentoWs {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentoWs.class);

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
    @Path("/create")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces("application/json")
    public Response createPeticion(@HeaderParam("accessToken") String accessToken, InputStream fileInputStream,
            @QueryParam("fileName") String rawFileName, @QueryParam("solId") String solId) {
        LOG.info("Request for Create Documento");
        LOG.info("Input Stream: " + fileInputStream.toString());
        LOG.info("Raw File Name: "+ rawFileName);
        String fileName = rawFileName.replace("-", ".");
        Gson jsonConstructor = new GsonBuilder().setPrettyPrinting().create();
        JsonObject response = new JsonObject();
        //if(accessToken.equalsIgnoreCase(Config.get("ACCESS_TOKEN"))){
        if (accessToken.equalsIgnoreCase("password")) {
            LOG.info("correct access token");

            try {
                File file = new File(fileName);
                try (OutputStream outStream = new FileOutputStream(file)) {
                    int cursor;
                    while ((cursor = fileInputStream.read()) != -1) {
                        outStream.write(cursor);
                    }
                } catch (Exception ex) {
                    LOG.info("File couldn't be saved: " + ex.toString());
                    response.addProperty("response", "failed");
                    response.addProperty("message", "Internal Server Error: " + ex.toString());
                    return Response.ok(jsonConstructor.toJson(response), MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
                }
                DocumentoDaoImp ddi = new DocumentoDaoImp();
                DocumentoDto ddt = new DocumentoDto();
                ddt.setDir(file.getAbsolutePath());
                ddt.setFormato_documento("." + fileName.split("\\.")[1]);
                ddt.setId_permiso(Integer.parseInt(solId));
                ddt.setNombre_documento(fileName);
                int result = ddi.insertar(ddt);
                if (result == 0) {
                    LOG.info("Document Correctly Inserted");
                    response.addProperty("response", "success");
                    response.addProperty("result", "success");
                    return Response.ok(jsonConstructor.toJson(response), MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
                } else {
                    LOG.info("Invalid Parameters");
                    response.addProperty("response", "failed");
                    response.addProperty("message", "Invalid Parameters");
                    return Response.ok(jsonConstructor.toJson(response), MediaType.APPLICATION_JSON + ";charset=UTF-8").build();
                }
            } catch (Exception ex) {
                LOG.info("There was an exception: " + ex.toString());
                ex.printStackTrace();
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
