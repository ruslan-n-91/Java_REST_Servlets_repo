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
    private transient Gson gson;
    private transient BookService bookService;

    private static final String EXCEPTION_SET_CONTENT_TYPE = "text/html;charset=UTF-8";
    private static final String INCOMING_FIELD_ID = "id";
    private static final String INCOMING_FIELD_TITLE = "title";
    private static final String INCOMING_FIELD_QUANTITY = "quantity";
    private static final String INCOMING_FIELD_AUTHORS = "authors";

    @Override
    public void init() throws ServletException {
        super.init();
        gson = new Gson();
        bookService = new BookServiceImpl();
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
            List<BookOutgoingDto> listOfBooks = new ArrayList<>();

            if (request.getParameter("id") == null) {
                listOfBooks = bookService.findAll();
            } else {
                Integer bookId = Integer.parseInt(request.getParameter("id"));
                listOfBooks.add(bookService.findById(bookId));
            }

            List<String> listOfBooksJson = listOfBooks.stream().map(gson::toJson).toList();
            responseString = listOfBooksJson.toString();
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

                Set<AuthorIncomingDto> authors = new HashSet<>();

                if (jsonObject.has(INCOMING_FIELD_AUTHORS)) {
                    for (JsonElement jsonElement : jsonObject.getAsJsonArray(INCOMING_FIELD_AUTHORS)) {
                        JsonObject temp = jsonElement.getAsJsonObject();
                        if (temp.has(INCOMING_FIELD_ID)) {
                            authors.add(new AuthorIncomingDto(temp.get(INCOMING_FIELD_ID).getAsInt(),
                                    null, null));
                        }
                    }
                }

                BookIncomingDto bookIncomingDto = new BookIncomingDto(null, title, quantity, authors);
                bookService.save(bookIncomingDto);

                responseString = gson.toJson(bookIncomingDto);
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

                Set<AuthorIncomingDto> authors = new HashSet<>();

                if (jsonObject.has(INCOMING_FIELD_AUTHORS)) {
                    for (JsonElement jsonElement : jsonObject.getAsJsonArray(INCOMING_FIELD_AUTHORS)) {
                        JsonObject temp = jsonElement.getAsJsonObject();
                        if (temp.has(INCOMING_FIELD_ID)) {
                            authors.add(new AuthorIncomingDto(temp.get(INCOMING_FIELD_ID).getAsInt(),
                                    null, null));
                        }
                    }
                }

                BookIncomingDto bookIncomingDto = new BookIncomingDto(id, title, quantity, authors);
                bookService.update(bookIncomingDto);

                responseString = gson.toJson(bookIncomingDto);
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

                bookService.delete(id);

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
