package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import service.BookService;
import servlet.dto.AuthorIncomingDto;
import servlet.dto.BookIncomingDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

@ExtendWith(MockitoExtension.class)
class BookServletTest {
    @InjectMocks
    private BookServlet bookServlet;
    @Mock
    private BookService mockBookService;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private BufferedReader mockBufferedReader;

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockBookService);
    }

    @Test
    void doGetShouldCallFindAllFromBookService() throws ServletException, IOException {
        bookServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockBookService).findAll();
    }

    @Test
    void doGetShouldCallFindByIdFromBookService() throws ServletException, IOException {
        Mockito.doReturn("53").when(mockRequest).getParameter("id");

        bookServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockBookService).findById(53);
    }

    @Test
    void doPostShouldCallSaveFromBookService_AndShouldSendValidDto() throws ServletException, IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"title\":\"Some Book 35\",\"quantity\":\"20\"," +
                                "\"authors\":[{\"id\":1},{\"id\":2}]}",
                        null)
                .when(mockBufferedReader).readLine();

        bookServlet.doPost(mockRequest, mockResponse);

        ArgumentCaptor<BookIncomingDto> argumentCaptor = ArgumentCaptor.forClass(BookIncomingDto.class);
        Mockito.verify(mockBookService).save(argumentCaptor.capture());

        BookIncomingDto dto = argumentCaptor.getValue();

        Assertions.assertEquals("Some Book 35", dto.getTitle());
        Assertions.assertEquals(20, dto.getQuantity());
        Assertions.assertEquals(2, dto.getAuthors().size());
        Assertions.assertTrue(dto.getAuthors().contains(
                new AuthorIncomingDto(1, null, null)));
        Assertions.assertTrue(dto.getAuthors().contains(
                new AuthorIncomingDto(2, null, null)));
    }

    @Test
    void doPutShouldCallUpdateFromBookService_AndShouldSendValidDto() throws ServletException, IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"id\":\"40\",\"title\":\"Some Book 35\"," +
                                "\"quantity\":\"20\",\"authors\":[{\"id\":2}]}",
                        null)
                .when(mockBufferedReader).readLine();

        bookServlet.doPut(mockRequest, mockResponse);

        ArgumentCaptor<BookIncomingDto> argumentCaptor = ArgumentCaptor.forClass(BookIncomingDto.class);
        Mockito.verify(mockBookService).update(argumentCaptor.capture());

        BookIncomingDto dto = argumentCaptor.getValue();

        Assertions.assertEquals(40, dto.getId());
        Assertions.assertEquals("Some Book 35", dto.getTitle());
        Assertions.assertEquals(20, dto.getQuantity());
        Assertions.assertEquals(1, dto.getAuthors().size());
        Assertions.assertTrue(dto.getAuthors().contains(
                new AuthorIncomingDto(2, null, null)));
    }

    @Test
    void doDeleteShouldCallDeleteFromBookService_AndShouldSendId() throws ServletException, IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"id\":\"45\"}",
                        null)
                .when(mockBufferedReader).readLine();

        bookServlet.doDelete(mockRequest, mockResponse);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(mockBookService).delete(argumentCaptor.capture());

        Integer id = argumentCaptor.getValue();

        Assertions.assertEquals(45, id);
    }
}
