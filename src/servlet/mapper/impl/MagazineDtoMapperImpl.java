package servlet.mapper.impl;

import entity.Magazine;
import entity.Publisher;
import servlet.dto.MagazineIncomingDto;
import servlet.dto.MagazineOutgoingDto;
import servlet.dto.PublisherOutgoingDto;
import servlet.mapper.MagazineDtoMapper;

import java.util.List;

public class MagazineDtoMapperImpl implements MagazineDtoMapper {
    @Override
    public Magazine mapToMagazine(MagazineIncomingDto magazineIncomingDto) {
        return new Magazine(magazineIncomingDto.getId(), magazineIncomingDto.getTitle(),
                magazineIncomingDto.getQuantity(),
                new Publisher(magazineIncomingDto.getPublisher().getId(),
                        magazineIncomingDto.getPublisher().getName(), null));
    }

    @Override
    public MagazineOutgoingDto mapToMagazineOutgoingDto(Magazine magazine) {
        return new MagazineOutgoingDto(magazine.getId(), magazine.getTitle(), magazine.getQuantity(),
                new PublisherOutgoingDto(magazine.getPublisher().getId(),
                        magazine.getPublisher().getName(), null));
    }

    @Override
    public List<MagazineOutgoingDto> mapToListOfMagazineOutgoingDtos(List<Magazine> listOfMagazines) {
        return listOfMagazines.stream().map(this::mapToMagazineOutgoingDto).toList();
    }
}
