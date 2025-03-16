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
    private final transient Gson gson = new Gson();
    private final transient PublisherService publisherService = new PublisherServiceImpl();

    private static final String RESPONSE_SET_CONTEXT_ON_CATCH = "text/html;charset=UTF-8";
    private static final String PUBLISHER_MAGAZINES = "magazines";

    private void setResponseHeader(HttpServletResponse response) {
        // method sets json content type and utf-8 character encoding for the http response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    private String getBodyOfRequest(HttpServletRequest request) throws IOException {
        // method reads and returns data from the request body
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
        List<PublisherOutgoingDto> listOfPublishers = new ArrayList<>();

        try {
            if (request.getParameter("id") == null) {
                listOfPublishers = publisherService.findAll();
            } else {
                Integer publisherId = Integer.parseInt(request.getParameter("id"));
                listOfPublishers.add(publisherService.findById(publisherId));
            }

            List<String> listOfPublishersJson = listOfPublishers.stream().map(gson::toJson).toList();
            responseString = listOfPublishersJson.toString();
        } catch (Exception e) {
            response.setContentType(RESPONSE_SET_CONTEXT_ON_CATCH);
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
        String requestBody = getBodyOfRequest(request);

        String responseString = "";

        try {
            JsonObject jsonObject = new Gson().fromJson(requestBody, JsonObject.class);

            if (jsonObject.has("name")) {
                String name = jsonObject.get("name").getAsString();

                Set<MagazineIncomingDto> magazines = new HashSet<>();

                if (jsonObject.has(PUBLISHER_MAGAZINES)) {
                    for (JsonElement jsonElement : jsonObject.getAsJsonArray(PUBLISHER_MAGAZINES)) {
                        JsonObject temp = jsonElement.getAsJsonObject();
                        if (temp.has("id")) {
                            magazines.add(new MagazineIncomingDto(temp.get("id").getAsInt(), null,
                                    null, null));
                        }
                    }
                }

                PublisherIncomingDto publisherIncomingDto = new PublisherIncomingDto(null, name, magazines);
                publisherService.save(publisherIncomingDto);

                responseString = gson.toJson(publisherIncomingDto);
            }
        } catch (Exception e) {
            response.setContentType(RESPONSE_SET_CONTEXT_ON_CATCH);
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
        String requestBody = getBodyOfRequest(request);

        String responseString = "";

        try {
            JsonObject jsonObject = new Gson().fromJson(requestBody, JsonObject.class);

            if (jsonObject.has("id")
                    && jsonObject.has("name")) {
                Integer id = jsonObject.get("id").getAsInt();
                String name = jsonObject.get("name").getAsString();

                Set<MagazineIncomingDto> magazines = new HashSet<>();

                if (jsonObject.has(PUBLISHER_MAGAZINES)) {
                    for (JsonElement jsonElement : jsonObject.getAsJsonArray(PUBLISHER_MAGAZINES)) {
                        JsonObject temp = jsonElement.getAsJsonObject();
                        if (temp.has("id")) {
                            magazines.add(new MagazineIncomingDto(temp.get("id").getAsInt(), null,
                                    null, null));
                        }
                    }
                }

                PublisherIncomingDto publisherIncomingDto = new PublisherIncomingDto(id, name, magazines);
                publisherService.update(publisherIncomingDto);

                responseString = gson.toJson(publisherIncomingDto);
            }
        } catch (Exception e) {
            response.setContentType(RESPONSE_SET_CONTEXT_ON_CATCH);
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
        String requestBody = getBodyOfRequest(request);

        String responseString = "";

        try {
            JsonObject jsonObject = new Gson().fromJson(requestBody, JsonObject.class);

            if (jsonObject.has("id")) {
                Integer id = jsonObject.get("id").getAsInt();

                publisherService.delete(id);

                responseString = jsonObject.toString();
            }
        } catch (Exception e) {
            response.setContentType(RESPONSE_SET_CONTEXT_ON_CATCH);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseString = e.getMessage();
        }

        PrintWriter printWriter = response.getWriter();
        printWriter.println(responseString);
        printWriter.flush();
    }
}
