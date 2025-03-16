package servlet.mapper.impl;

import entity.Magazine;
import entity.Publisher;
import servlet.dto.PublisherIncomingDto;
import servlet.dto.PublisherOutgoingDto;
import servlet.dto.MagazineOutgoingDto;
import servlet.mapper.PublisherDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

public class PublisherDtoMapperImpl implements PublisherDtoMapper {

    @Override
    public Publisher mapToPublisher(PublisherIncomingDto publisherIncomingDto) {
        return new Publisher(publisherIncomingDto.getId(), publisherIncomingDto.getName(),
                publisherIncomingDto.getMagazines().stream()
                        .map(magazineIncomingDto ->
                            new Magazine(magazineIncomingDto.getId(), magazineIncomingDto.getTitle(),
                                    magazineIncomingDto.getQuantity(), null)
                        ).collect(Collectors.toSet()));
    }

    @Override
    public PublisherOutgoingDto mapToPublisherOutgoingDto(Publisher publisher) {
        return new PublisherOutgoingDto(publisher.getId(), publisher.getName(),
                publisher.getMagazines().stream()
                        .map(magazine ->
                            new MagazineOutgoingDto(magazine.getId(), magazine.getTitle(),
                                    magazine.getQuantity(), null)
                        ).collect(Collectors.toSet()));
    }

    @Override
    public List<PublisherOutgoingDto> mapToListOfPublisherOutgoingDtos(List<Publisher> listOfPublishers) {
        return listOfPublishers.stream().map(this::mapToPublisherOutgoingDto).toList();
    }
}
