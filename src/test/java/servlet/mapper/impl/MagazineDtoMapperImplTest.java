package servlet.mapper.impl;

import entity.Magazine;
import entity.Publisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servlet.dto.*;
import servlet.mapper.MagazineDtoMapper;

import java.util.List;

class MagazineDtoMapperImplTest {
    private MagazineDtoMapper magazineDtoMapper;

    @BeforeEach
    void setUp() {
        magazineDtoMapper = new MagazineDtoMapperImpl();
    }

    @Test
    void mapToMagazine_ShouldReturnValidObject() {
        MagazineIncomingDto dto = new MagazineIncomingDto(15, "magazine", 11,
                new PublisherIncomingDto(14, "publisher", null));

        Magazine magazine = magazineDtoMapper.mapToMagazine(dto);

        Assertions.assertEquals(dto.getId(), magazine.getId());
        Assertions.assertEquals(dto.getTitle(), magazine.getTitle());
        Assertions.assertEquals(dto.getQuantity(), magazine.getQuantity());
        Assertions.assertEquals(dto.getPublisher().getId(), magazine.getPublisher().getId());
        Assertions.assertEquals(dto.getPublisher().getName(), magazine.getPublisher().getName());
    }

    @Test
    void mapToMagazineOutgoingDto_ShouldReturnValidDto() {
        Magazine magazine = new Magazine(15, "magazine", 11,
                new Publisher(14, "publisher", null));

        MagazineOutgoingDto dto = magazineDtoMapper.mapToMagazineOutgoingDto(magazine);

        Assertions.assertEquals(magazine.getId(), dto.getId());
        Assertions.assertEquals(magazine.getTitle(), dto.getTitle());
        Assertions.assertEquals(magazine.getQuantity(), dto.getQuantity());
        Assertions.assertEquals(magazine.getPublisher().getId(), dto.getPublisher().getId());
        Assertions.assertEquals(magazine.getPublisher().getName(), dto.getPublisher().getName());
    }

    @Test
    void mapToListOfBookOutgoingDtos_ShouldReturnValidList() {
        List<Magazine> list = List.of(new Magazine(11, "magazine", 4, new Publisher()),
                new Magazine(12, "magazine", 5, new Publisher()));
        List<MagazineOutgoingDto> listDto = magazineDtoMapper.mapToListOfMagazineOutgoingDtos(list);

        Assertions.assertEquals(list.size(), listDto.size());
    }
}
