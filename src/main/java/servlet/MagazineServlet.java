package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.MagazineService;
import service.impl.MagazineServiceImpl;
import servlet.dto.MagazineIncomingDto;
import servlet.dto.MagazineOutgoingDto;
import servlet.dto.PublisherIncomingDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "MagazineServlet", urlPatterns = "/magazines/*")
public class MagazineServlet extends HttpServlet {
    private transient Gson gson;
    private transient MagazineService magazineService;

    private static final String EXCEPTION_SET_CONTENT_TYPE = "text/html;charset=UTF-8";
    private static final String INCOMING_FIELD_ID = "id";
    private static final String INCOMING_FIELD_TITLE = "title";
    private static final String INCOMING_FIELD_QUANTITY = "quantity";
    private static final String INCOMING_FIELD_PUBLISHER = "publisher";

    @Override
    public void init() throws ServletException {
        super.init();
        gson = new Gson();
        magazineService = new MagazineServiceImpl();
    }

    /**
     * Sets json content type and utf-8 character encoding for the http response.
     */
    private void setResponseHeader(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    /**
     * Reads and returns the data from the request body.
     */
    private String getBodyOfRequest(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader requestReader = request.getReader();
        String string;

        while ((string = requestReader.readLine()) != null) {
            stringBuilder.append(string);
        }

        return stringBuilder.toString();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setResponseHeader(response);
        String responseString = "";

        try {
            List<MagazineOutgoingDto> listOfMagazines = new ArrayList<>();

            if (request.getParameter("id") == null) {
                listOfMagazines = magazineService.findAll();
            } else {
                Integer magazineId = Integer.parseInt(request.getParameter("id"));
                listOfMagazines.add(magazineService.findById(magazineId));
            }

            List<String> listOfMagazinesJson = listOfMagazines.stream().map(gson::toJson).toList();
            responseString = listOfMagazinesJson.toString();
        } catch (Exception e) {
            response.setContentType(EXCEPTION_SET_CONTENT_TYPE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseString = e.getMessage();
        }

        PrintWriter printWriter = response.getWriter();
        printWriter.println(responseString);
        printWriter.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setResponseHeader(response);
        String responseString = "";

        try {
            String requestBody = getBodyOfRequest(request);

            JsonObject jsonObject = new Gson().fromJson(requestBody, JsonObject.class);

            if (jsonObject.has(INCOMING_FIELD_TITLE)
                    && jsonObject.has(INCOMING_FIELD_QUANTITY)) {
                String title = jsonObject.get(INCOMING_FIELD_TITLE).getAsString();
                Integer quantity = jsonObject.get(INCOMING_FIELD_QUANTITY).getAsInt();

                PublisherIncomingDto publisher = new PublisherIncomingDto();

                if (jsonObject.has(INCOMING_FIELD_PUBLISHER)) {
                    JsonObject temp = jsonObject.getAsJsonObject(INCOMING_FIELD_PUBLISHER);

                    if (temp.has(INCOMING_FIELD_ID)) {
                        publisher = new PublisherIncomingDto(temp.get(INCOMING_FIELD_ID).getAsInt(),
                                null, null);
                    }
                }

                MagazineIncomingDto magazineIncomingDto = new MagazineIncomingDto(null, title, quantity, publisher);
                magazineService.save(magazineIncomingDto);

                responseString = gson.toJson(magazineIncomingDto);
            }
        } catch (Exception e) {
            response.setContentType(EXCEPTION_SET_CONTENT_TYPE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseString = e.getMessage();
        }

        PrintWriter printWriter = response.getWriter();
        printWriter.println(responseString);
        printWriter.flush();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setResponseHeader(response);
        String responseString = "";

        try {
            String requestBody = getBodyOfRequest(request);

            JsonObject jsonObject = new Gson().fromJson(requestBody, JsonObject.class);

            if (jsonObject.has(INCOMING_FIELD_ID)
                    && jsonObject.has(INCOMING_FIELD_TITLE)
                    && jsonObject.has(INCOMING_FIELD_QUANTITY)) {
                Integer id = jsonObject.get(INCOMING_FIELD_ID).getAsInt();
                String title = jsonObject.get(INCOMING_FIELD_TITLE).getAsString();
                Integer quantity = jsonObject.get(INCOMING_FIELD_QUANTITY).getAsInt();

                PublisherIncomingDto publisher = new PublisherIncomingDto();

                if (jsonObject.has(INCOMING_FIELD_PUBLISHER)) {
                    JsonObject temp = jsonObject.getAsJsonObject(INCOMING_FIELD_PUBLISHER);

                    if (temp.has(INCOMING_FIELD_ID)) {
                        publisher = new PublisherIncomingDto(temp.get(INCOMING_FIELD_ID).getAsInt(),
                                null, null);
                    }
                }

                MagazineIncomingDto magazineIncomingDto = new MagazineIncomingDto(id, title, quantity, publisher);
                magazineService.update(magazineIncomingDto);

                responseString = gson.toJson(magazineIncomingDto);
            }
        } catch (Exception e) {
            response.setContentType(EXCEPTION_SET_CONTENT_TYPE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseString = e.getMessage();
        }

        PrintWriter printWriter = response.getWriter();
        printWriter.println(responseString);
        printWriter.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setResponseHeader(response);
        String responseString = "";

        try {
            String requestBody = getBodyOfRequest(request);

            JsonObject jsonObject = new Gson().fromJson(requestBody, JsonObject.class);

            if (jsonObject.has(INCOMING_FIELD_ID)) {
                Integer id = jsonObject.get(INCOMING_FIELD_ID).getAsInt();

                magazineService.delete(id);

                responseString = jsonObject.toString();
            }
        } catch (Exception e) {
            response.setContentType(EXCEPTION_SET_CONTENT_TYPE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseString = e.getMessage();
        }

        PrintWriter printWriter = response.getWriter();
        printWriter.println(responseString);
        printWriter.flush();
    }
}
