package servlet.mapper;

import entity.Magazine;
import servlet.dto.MagazineIncomingDto;
import servlet.dto.MagazineOutgoingDto;

import java.util.List;

public interface MagazineDtoMapper {
    Magazine mapToMagazine(MagazineIncomingDto magazineIncomingDto);

    MagazineOutgoingDto mapToMagazineOutgoingDto(Magazine magazine);

    List<MagazineOutgoingDto> mapToListOfMagazineOutgoingDtos(List<Magazine> listOfMagazines);
}
