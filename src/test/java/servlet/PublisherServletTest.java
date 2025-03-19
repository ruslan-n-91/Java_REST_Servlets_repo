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
import service.PublisherService;
import servlet.dto.MagazineIncomingDto;
import servlet.dto.PublisherIncomingDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

@ExtendWith(MockitoExtension.class)
class PublisherServletTest {
    @InjectMocks
    private PublisherServlet publisherServlet;
    @Mock
    private PublisherService mockPublisherService;
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
        Mockito.reset(mockPublisherService);
    }

    @Test
    void doGetShouldCallFindAllFromPublisherService() throws ServletException, IOException {
        publisherServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockPublisherService).findAll();
    }

    @Test
    void doGetShouldCallFindByIdFromPublisherService() throws ServletException, IOException {
        Mockito.doReturn("98").when(mockRequest).getParameter("id");

        publisherServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockPublisherService).findById(98);
    }

    @Test
    void doPostShouldCallSaveFromPublisherService_AndShouldSendValidDto() throws ServletException, IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"name\":\"Some Publisher 77\"," +
                                "\"magazines\":[{\"id\":1},{\"id\":2}]}",
                        null)
                .when(mockBufferedReader).readLine();

        publisherServlet.doPost(mockRequest, mockResponse);

        ArgumentCaptor<PublisherIncomingDto> argumentCaptor = ArgumentCaptor.forClass(PublisherIncomingDto.class);
        Mockito.verify(mockPublisherService).save(argumentCaptor.capture());

        PublisherIncomingDto dto = argumentCaptor.getValue();

        Assertions.assertEquals("Some Publisher 77", dto.getName());
        Assertions.assertEquals(2, dto.getMagazines().size());
        Assertions.assertTrue(dto.getMagazines().contains(
                new MagazineIncomingDto(1, null, null, null)));
        Assertions.assertTrue(dto.getMagazines().contains(
                new MagazineIncomingDto(2, null, null, null)));
    }

    @Test
    void doPutShouldCallUpdateFromPublisherService_AndShouldSendValidDto() throws ServletException, IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"id\":\"36\",\"name\":\"Some Publisher 36\"," +
                                "\"magazines\":[{\"id\":2}]}",
                        null)
                .when(mockBufferedReader).readLine();

        publisherServlet.doPut(mockRequest, mockResponse);

        ArgumentCaptor<PublisherIncomingDto> argumentCaptor = ArgumentCaptor.forClass(PublisherIncomingDto.class);
        Mockito.verify(mockPublisherService).update(argumentCaptor.capture());

        PublisherIncomingDto dto = argumentCaptor.getValue();

        Assertions.assertEquals(36, dto.getId());
        Assertions.assertEquals("Some Publisher 36", dto.getName());
        Assertions.assertEquals(1, dto.getMagazines().size());
        Assertions.assertTrue(dto.getMagazines().contains(
                new MagazineIncomingDto(2, null, null, null)));
    }

    @Test
    void doDeleteShouldCallDeleteFromPublisherService_AndShouldSendId() throws ServletException, IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"id\":\"29\"}",
                        null)
                .when(mockBufferedReader).readLine();

        publisherServlet.doDelete(mockRequest, mockResponse);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(mockPublisherService).delete(argumentCaptor.capture());

        Integer id = argumentCaptor.getValue();

        Assertions.assertEquals(29, id);
    }
}
