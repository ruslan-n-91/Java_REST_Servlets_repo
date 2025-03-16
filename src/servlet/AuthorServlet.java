package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthorService;
import service.impl.AuthorServiceImpl;
import servlet.dto.AuthorIncomingDto;
import servlet.dto.AuthorOutgoingDto;
import servlet.dto.BookIncomingDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet(name = "AuthorServlet", urlPatterns = "/authors/*")
public class AuthorServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final AuthorService authorService = new AuthorServiceImpl();

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
        List<AuthorOutgoingDto> listOfAuthors = new ArrayList<>();

        try {
            if (request.getParameter("id") == null) {
                listOfAuthors = authorService.findAll();
            } else {
                Integer authorId = Integer.parseInt(request.getParameter("id"));
                listOfAuthors.add(authorService.findById(authorId));
            }

            List<String> listOfAuthorsJson = listOfAuthors.stream().map(gson::toJson).toList();
            responseString = listOfAuthorsJson.toString();
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

            if (jsonObject.has("name")) {
                String name = jsonObject.get("name").getAsString();

                Set<BookIncomingDto> books = new HashSet<>();

                if (jsonObject.has("books")) {
                    for (JsonElement jsonElement : jsonObject.getAsJsonArray("books")) {
                        JsonObject temp = jsonElement.getAsJsonObject();
                        if (temp.has("id")) {
                            books.add(new BookIncomingDto(temp.get("id").getAsInt(), null, null, null));
                        }
                    }
                }

                AuthorIncomingDto authorIncomingDto = new AuthorIncomingDto(null, name, books);
                authorService.save(authorIncomingDto);

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
        //response.sendRedirect(request.getContextPath() + "/authors");
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

                Set<BookIncomingDto> books = new HashSet<>();

                if (jsonObject.has("books")) {
                    for (JsonElement jsonElement : jsonObject.getAsJsonArray("books")) {
                        JsonObject temp = jsonElement.getAsJsonObject();
                        if (temp.has("id")) {
                            books.add(new BookIncomingDto(temp.get("id").getAsInt(), null, null, null));
                        }
                    }
                }

                AuthorIncomingDto authorIncomingDto = new AuthorIncomingDto(id, name, books);
                authorService.update(authorIncomingDto);

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
        //response.sendRedirect(request.getContextPath() + "/authors");
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

                authorService.delete(id);

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
        //response.sendRedirect(request.getContextPath() + "/authors");
    }
}
