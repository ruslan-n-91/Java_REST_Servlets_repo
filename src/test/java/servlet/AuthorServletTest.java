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
import service.AuthorService;
import servlet.dto.AuthorIncomingDto;
import servlet.dto.BookIncomingDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

@ExtendWith(MockitoExtension.class)
class AuthorServletTest {
    @InjectMocks
    private AuthorServlet authorServlet;
    @Mock
    private AuthorService mockAuthorService;
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
        Mockito.reset(mockAuthorService);
    }

    @Test
    void doGetShouldCallFindAllFromAuthorService() throws ServletException, IOException {
        authorServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockAuthorService).findAll();
    }

    @Test
    void doGetShouldCallFindByIdFromAuthorService() throws ServletException, IOException {
        Mockito.doReturn("33").when(mockRequest).getParameter("id");

        authorServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockAuthorService).findById(33);
    }

    @Test
    void doPostShouldCallSaveFromAuthorService_AndShouldSendValidDto() throws ServletException, IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"name\":\"Some Author 33\"," +
                                "\"books\":[{\"id\":1},{\"id\":2}]}",
                        null)
                .when(mockBufferedReader).readLine();

        authorServlet.doPost(mockRequest, mockResponse);

        ArgumentCaptor<AuthorIncomingDto> argumentCaptor = ArgumentCaptor.forClass(AuthorIncomingDto.class);
        Mockito.verify(mockAuthorService).save(argumentCaptor.capture());

        AuthorIncomingDto dto = argumentCaptor.getValue();

        Assertions.assertEquals("Some Author 33", dto.getName());
        Assertions.assertEquals(2, dto.getBooks().size());
        Assertions.assertTrue(dto.getBooks().contains(
                new BookIncomingDto(1, null, null, null)));
        Assertions.assertTrue(dto.getBooks().contains(
                new BookIncomingDto(2, null, null, null)));
    }

    @Test
    void doPutShouldCallUpdateFromAuthorService_AndShouldSendValidDto() throws ServletException, IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"id\":\"35\",\"name\":\"Some Author 35\"," +
                                "\"books\":[{\"id\":2}]}",
                        null)
                .when(mockBufferedReader).readLine();

        authorServlet.doPut(mockRequest, mockResponse);

        ArgumentCaptor<AuthorIncomingDto> argumentCaptor = ArgumentCaptor.forClass(AuthorIncomingDto.class);
        Mockito.verify(mockAuthorService).update(argumentCaptor.capture());

        AuthorIncomingDto dto = argumentCaptor.getValue();

        Assertions.assertEquals(35, dto.getId());
        Assertions.assertEquals("Some Author 35", dto.getName());
        Assertions.assertEquals(1, dto.getBooks().size());
        Assertions.assertTrue(dto.getBooks().contains(
                new BookIncomingDto(2, null, null, null)));
    }

    @Test
    void doDeleteShouldCallDeleteFromAuthorService_AndShouldSendId() throws ServletException, IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"id\":\"21\"}",
                        null)
                .when(mockBufferedReader).readLine();

        authorServlet.doDelete(mockRequest, mockResponse);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(mockAuthorService).delete(argumentCaptor.capture());

        Integer id = argumentCaptor.getValue();

        Assertions.assertEquals(21, id);
    }
}
