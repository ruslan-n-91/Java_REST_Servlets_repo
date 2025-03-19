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
import service.MagazineService;
import servlet.dto.MagazineIncomingDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

@ExtendWith(MockitoExtension.class)
class MagazineServletTest {
    @InjectMocks
    private MagazineServlet magazineServlet;
    @Mock
    private MagazineService mockMagazineService;
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
        Mockito.reset(mockMagazineService);
    }

    @Test
    void doGetShouldCallFindAllFromMagazineService() throws ServletException, IOException {
        magazineServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockMagazineService).findAll();
    }

    @Test
    void doGetShouldCallFindByIdFromMagazineService() throws ServletException, IOException {
        Mockito.doReturn("11").when(mockRequest).getParameter("id");

        magazineServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockMagazineService).findById(11);
    }

    @Test
    void doPostShouldCallSaveFromMagazineService_AndShouldSendValidDto() throws ServletException, IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"title\":\"Some Magazine 16\",\"quantity\":\"25\"," +
                                "\"publisher\":{\"id\":11}}",
                        null)
                .when(mockBufferedReader).readLine();

        magazineServlet.doPost(mockRequest, mockResponse);

        ArgumentCaptor<MagazineIncomingDto> argumentCaptor = ArgumentCaptor.forClass(MagazineIncomingDto.class);
        Mockito.verify(mockMagazineService).save(argumentCaptor.capture());

        MagazineIncomingDto dto = argumentCaptor.getValue();

        Assertions.assertEquals("Some Magazine 16", dto.getTitle());
        Assertions.assertEquals(25, dto.getQuantity());
        Assertions.assertEquals(11, dto.getPublisher().getId());
    }

    @Test
    void doPutShouldCallUpdateFromMagazineService_AndShouldSendValidDto() throws ServletException, IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"id\":\"12\",\"title\":\"Some Magazine 18\"," +
                                "\"quantity\":\"63\",\"publisher\":{\"id\":22}}",
                        null)
                .when(mockBufferedReader).readLine();

        magazineServlet.doPut(mockRequest, mockResponse);

        ArgumentCaptor<MagazineIncomingDto> argumentCaptor = ArgumentCaptor.forClass(MagazineIncomingDto.class);
        Mockito.verify(mockMagazineService).update(argumentCaptor.capture());

        MagazineIncomingDto dto = argumentCaptor.getValue();

        Assertions.assertEquals(12, dto.getId());
        Assertions.assertEquals("Some Magazine 18", dto.getTitle());
        Assertions.assertEquals(63, dto.getQuantity());
        Assertions.assertEquals(22, dto.getPublisher().getId());
    }

    @Test
    void doDeleteShouldCallDeleteFromMagazineService_AndShouldSendId() throws ServletException, IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"id\":\"27\"}",
                        null)
                .when(mockBufferedReader).readLine();

        magazineServlet.doDelete(mockRequest, mockResponse);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(mockMagazineService).delete(argumentCaptor.capture());

        Integer id = argumentCaptor.getValue();

        Assertions.assertEquals(27, id);
    }
}
