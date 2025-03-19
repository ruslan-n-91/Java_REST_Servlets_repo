package servlet.mapper.impl;

import entity.Magazine;
import entity.Publisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servlet.dto.*;
import servlet.mapper.PublisherDtoMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class PublisherDtoMapperImplTest {
    private PublisherDtoMapper publisherDtoMapper;

    @BeforeEach
    void setUp() {
        publisherDtoMapper = new PublisherDtoMapperImpl();
    }

    @Test
    void mapToPublisher_ShouldReturnValidObject() {
        PublisherIncomingDto dto = new PublisherIncomingDto(24, "publisher",
                Set.of(new MagazineIncomingDto(14, "magazine", 19, null)));

        Publisher publisher = publisherDtoMapper.mapToPublisher(dto);

        Assertions.assertEquals(dto.getId(), publisher.getId());
        Assertions.assertEquals(dto.getName(), publisher.getName());
        Assertions.assertEquals(dto.getMagazines().size(), publisher.getMagazines().size());
    }

    @Test
    void mapToPublisherOutgoingDto_ShouldReturnValidDto() {
        Publisher publisher = new Publisher(24, "publisher",
                Set.of(new Magazine()));

        PublisherOutgoingDto dto = publisherDtoMapper.mapToPublisherOutgoingDto(publisher);

        Assertions.assertEquals(publisher.getId(), dto.getId());
        Assertions.assertEquals(publisher.getName(), dto.getName());
        Assertions.assertEquals(publisher.getMagazines().size(), dto.getMagazines().size());
    }

    @Test
    void mapToListOfPublisherOutgoingDtos_ShouldReturnValidList() {
        List<Publisher> list = List.of(new Publisher(31, "publisher", new HashSet<>()),
                new Publisher(32, "publisher", new HashSet<>()));
        List<PublisherOutgoingDto> listDto = publisherDtoMapper.mapToListOfPublisherOutgoingDtos(list);

        Assertions.assertEquals(list.size(), listDto.size());
    }
}
