package service.impl;

import dao.Dao;
import entity.Magazine;
import entity.Publisher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import servlet.dto.*;

@ExtendWith(MockitoExtension.class)
class MagazineServiceImplTest {
    @InjectMocks
    MagazineServiceImpl magazineService;

    @Mock
    Dao<Magazine, Integer> mockMagazineDao;

    @AfterEach
    void tearDown() {
        Mockito.reset(mockMagazineDao);
    }

    @Test
    void findAllShouldCallFindAllFromMagazineDao() {
        magazineService.findAll();

        Mockito.verify(mockMagazineDao).findAll();
    }

    @Test
    void findByIdShouldCallFindByIdFromMagazineDao_AndShouldReturnValidDto() {
        Magazine magazine = new Magazine(56, "Some Magazine 56", 8,
                new Publisher(56, null, null));

        Mockito.doReturn(magazine).when(mockMagazineDao).findById(56);

        MagazineOutgoingDto dto = magazineService.findById(56);

        Mockito.verify(mockMagazineDao).findById(56);
        Assertions.assertEquals(magazine.getId(), dto.getId());
        Assertions.assertEquals(magazine.getTitle(), dto.getTitle());
        Assertions.assertEquals(magazine.getQuantity(), dto.getQuantity());
        Assertions.assertEquals(magazine.getPublisher().getId(), dto.getPublisher().getId());
    }

    @Test
    void saveShouldCallSaveFromMagazineDao() {
        MagazineIncomingDto dto = new MagazineIncomingDto(null, "Some Magazine 32", 18,
                new PublisherIncomingDto(32, null, null));
        Magazine magazine = new Magazine(null, "Some Magazine 32", 18,
                new Publisher(32, null, null));

        magazineService.save(dto);

        Mockito.verify(mockMagazineDao).save(magazine);
    }

    @Test
    void updateShouldCallUpdateFromMagazineDao() {
        MagazineIncomingDto dto = new MagazineIncomingDto(89, "Some Magazine 89", 39,
                new PublisherIncomingDto(89, null, null));
        Magazine magazine = new Magazine(89, "Some Magazine 89", 39,
                new Publisher(89, null, null));

        magazineService.update(dto);

        Mockito.verify(mockMagazineDao).update(magazine);
    }

    @Test
    void deleteShouldCallDeleteFromMagazineDao() {
        magazineService.delete(59);

        Mockito.verify(mockMagazineDao).delete(59);
    }
}
