package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServlet;
import servlet.dto.AuthorIncomingDto;
import servlet.dto.BookOutgoingDto;
import servlet.dto.BookIncomingDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.BookService;
import service.impl.BookServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet(name = "BookServlet", urlPatterns = "/books/*")
public class BookServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final BookService bookService = new BookServiceImpl();

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
        List<BookOutgoingDto> listOfBooks = new ArrayList<>();

        try {
            if (request.getParameter("id") == null) {
                listOfBooks = bookService.findAll();
            } else {
                Integer bookId = Integer.parseInt(request.getParameter("id"));
                listOfBooks.add(bookService.findById(bookId));
            }

            List<String> listOfBooksJson = listOfBooks.stream().map(gson::toJson).toList();
            responseString = listOfBooksJson.toString();
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

                Set<AuthorIncomingDto> authors = new HashSet<>();

                if (jsonObject.has("authors")) {
                    for (JsonElement jsonElement : jsonObject.getAsJsonArray("authors")) {
                        JsonObject temp = jsonElement.getAsJsonObject();
                        if (temp.has("id")) {
                            authors.add(new AuthorIncomingDto(temp.get("id").getAsInt(), null, null));
                        }
                    }
                }

                BookIncomingDto bookIncomingDto = new BookIncomingDto(null, title, quantity, authors);
                bookService.save(bookIncomingDto);

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
        //response.sendRedirect(request.getContextPath() + "/books");
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

                Set<AuthorIncomingDto> authors = new HashSet<>();

                if (jsonObject.has("authors")) {
                    for (JsonElement jsonElement : jsonObject.getAsJsonArray("authors")) {
                        JsonObject temp = jsonElement.getAsJsonObject();
                        if (temp.has("id")) {
                            authors.add(new AuthorIncomingDto(temp.get("id").getAsInt(), null, null));
                        }
                    }
                }

                BookIncomingDto bookIncomingDto = new BookIncomingDto(id, title, quantity, authors);
                bookService.update(bookIncomingDto);

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
        //response.sendRedirect(request.getContextPath() + "/books");
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

                bookService.delete(id);

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
        //response.sendRedirect(request.getContextPath() + "/books");
    }
}
