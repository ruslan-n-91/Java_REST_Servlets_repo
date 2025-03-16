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
    private final Gson gson = new Gson();
    private final MagazineService magazineService = new MagazineServiceImpl();

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
        List<MagazineOutgoingDto> listOfMagazines = new ArrayList<>();

        try {
            if (request.getParameter("id") == null) {
                listOfMagazines = magazineService.findAll();
            } else {
                Integer magazineId = Integer.parseInt(request.getParameter("id"));
                listOfMagazines.add(magazineService.findById(magazineId));
            }

            List<String> listOfMagazinesJson = listOfMagazines.stream().map(gson::toJson).toList();
            responseString = listOfMagazinesJson.toString();
        } catch (Exception e) {
            response.setContentType("text/html;charset=UTF-8");
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

            if (jsonObject.has("title")
                    && jsonObject.has("quantity")) {
                String title = jsonObject.get("title").getAsString();
                Integer quantity = jsonObject.get("quantity").getAsInt();

                PublisherIncomingDto publisher = new PublisherIncomingDto();

                if (jsonObject.has("publisher")) {
                    JsonObject temp = jsonObject.getAsJsonObject("publisher");

                    if (temp.has("id")) {
                        publisher = new PublisherIncomingDto(temp.get("id").getAsInt(), null, null);
                    }
                }

                MagazineIncomingDto magazineIncomingDto = new MagazineIncomingDto(null, title, quantity, publisher);
                magazineService.save(magazineIncomingDto);

                responseString = gson.toJson(magazineIncomingDto);
            }
        } catch (Exception e) {
            response.setContentType("text/html;charset=UTF-8");
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
                    && jsonObject.has("title")
                    && jsonObject.has("quantity")) {
                Integer id = jsonObject.get("id").getAsInt();
                String title = jsonObject.get("title").getAsString();
                Integer quantity = jsonObject.get("quantity").getAsInt();

                PublisherIncomingDto publisher = new PublisherIncomingDto();

                if (jsonObject.has("publisher")) {
                    JsonObject temp = jsonObject.getAsJsonObject("publisher");

                    if (temp.has("id")) {
                        publisher = new PublisherIncomingDto(temp.get("id").getAsInt(), null, null);
                    }
                }

                MagazineIncomingDto magazineIncomingDto = new MagazineIncomingDto(id, title, quantity, publisher);
                magazineService.update(magazineIncomingDto);

                responseString = gson.toJson(magazineIncomingDto);
            }
        } catch (Exception e) {
            response.setContentType("text/html;charset=UTF-8");
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

                magazineService.delete(id);

                responseString = jsonObject.toString();
            }
        } catch (Exception e) {
            response.setContentType("text/html;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseString = e.getMessage();
        }

        PrintWriter printWriter = response.getWriter();
        printWriter.println(responseString);
        printWriter.flush();
    }
}
