package service.impl;

import dao.PublisherDao;
import entity.Publisher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import servlet.dto.PublisherIncomingDto;
import servlet.dto.PublisherOutgoingDto;

import java.util.HashSet;

@ExtendWith(MockitoExtension.class)
class PublisherServiceImplTest {
    @InjectMocks
    PublisherServiceImpl publisherService;

    @Mock
    PublisherDao mockPublisherDao;

    @AfterEach
    void tearDown() {
        Mockito.reset(mockPublisherDao);
    }

    @Test
    void findAllShouldCallFindAllFromPublisherDao() {
        publisherService.findAll();

        Mockito.verify(mockPublisherDao).findAll();
    }

    @Test
    void findByIdShouldCallFindByIdFromPublisherDao_AndShouldReturnValidDto() {
        Publisher publisher = new Publisher(43, "Some Publisher 43", new HashSet<>());

        Mockito.doReturn(publisher).when(mockPublisherDao).findById(43);

        PublisherOutgoingDto dto = publisherService.findById(43);

        Mockito.verify(mockPublisherDao).findById(43);
        Assertions.assertEquals(publisher.getId(), dto.getId());
        Assertions.assertEquals(publisher.getName(), dto.getName());
        Assertions.assertEquals(publisher.getMagazines().size(), dto.getMagazines().size());
    }

    @Test
    void saveShouldCallSaveFromPublisherDao() {
        PublisherIncomingDto dto = new PublisherIncomingDto(null, "Some Publisher 49", new HashSet<>());
        Publisher publisher = new Publisher(null, "Some Publisher 49", new HashSet<>());

        publisherService.save(dto);

        Mockito.verify(mockPublisherDao).save(publisher);
    }

    @Test
    void updateShouldCallUpdateFromPublisherDao() {
        PublisherIncomingDto dto = new PublisherIncomingDto(32, "Some Publisher 32", new HashSet<>());
        Publisher publisher = new Publisher(32, "Some Publisher 32", new HashSet<>());

        publisherService.update(dto);

        Mockito.verify(mockPublisherDao).update(publisher);
    }

    @Test
    void deleteShouldCallDeleteFromPublisherDao() {
        publisherService.delete(31);

        Mockito.verify(mockPublisherDao).delete(31);
    }
}
