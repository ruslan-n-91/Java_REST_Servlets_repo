package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.PublisherService;
import service.impl.PublisherServiceImpl;
import servlet.dto.PublisherIncomingDto;
import servlet.dto.PublisherOutgoingDto;
import servlet.dto.MagazineIncomingDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet(name = "PublisherServlet", urlPatterns = "/publishers/*")
public class PublisherServlet extends HttpServlet {
    private transient Gson gson;
    private transient PublisherService publisherService;

    private static final String EXCEPTION_SET_CONTENT_TYPE = "text/html;charset=UTF-8";
    private static final String INCOMING_FIELD_ID = "id";
    private static final String INCOMING_FIELD_NAME = "name";
    private static final String INCOMING_FIELD_MAGAZINES = "magazines";

    @Override
    public void init() throws ServletException {
        super.init();
        gson = new Gson();
        publisherService = new PublisherServiceImpl();
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
            List<PublisherOutgoingDto> listOfPublishers = new ArrayList<>();

            if (request.getParameter("id") == null) {
                listOfPublishers = publisherService.findAll();
            } else {
                Integer publisherId = Integer.parseInt(request.getParameter("id"));
                listOfPublishers.add(publisherService.findById(publisherId));
            }

            List<String> listOfPublishersJson = listOfPublishers.stream().map(gson::toJson).toList();
            responseString = listOfPublishersJson.toString();
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

            if (jsonObject.has(INCOMING_FIELD_NAME)) {
                String name = jsonObject.get(INCOMING_FIELD_NAME).getAsString();

                Set<MagazineIncomingDto> magazines = new HashSet<>();

                if (jsonObject.has(INCOMING_FIELD_MAGAZINES)) {
                    for (JsonElement jsonElement : jsonObject.getAsJsonArray(INCOMING_FIELD_MAGAZINES)) {
                        JsonObject temp = jsonElement.getAsJsonObject();
                        if (temp.has(INCOMING_FIELD_ID)) {
                            magazines.add(new MagazineIncomingDto(temp.get(INCOMING_FIELD_ID).getAsInt(), null,
                                    null, null));
                        }
                    }
                }

                PublisherIncomingDto publisherIncomingDto = new PublisherIncomingDto(null, name, magazines);
                publisherService.save(publisherIncomingDto);

                responseString = gson.toJson(publisherIncomingDto);
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
                    && jsonObject.has(INCOMING_FIELD_NAME)) {
                Integer id = jsonObject.get(INCOMING_FIELD_ID).getAsInt();
                String name = jsonObject.get(INCOMING_FIELD_NAME).getAsString();

                Set<MagazineIncomingDto> magazines = new HashSet<>();

                if (jsonObject.has(INCOMING_FIELD_MAGAZINES)) {
                    for (JsonElement jsonElement : jsonObject.getAsJsonArray(INCOMING_FIELD_MAGAZINES)) {
                        JsonObject temp = jsonElement.getAsJsonObject();
                        if (temp.has(INCOMING_FIELD_ID)) {
                            magazines.add(new MagazineIncomingDto(temp.get(INCOMING_FIELD_ID).getAsInt(), null,
                                    null, null));
                        }
                    }
                }

                PublisherIncomingDto publisherIncomingDto = new PublisherIncomingDto(id, name, magazines);
                publisherService.update(publisherIncomingDto);

                responseString = gson.toJson(publisherIncomingDto);
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

                publisherService.delete(id);

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
