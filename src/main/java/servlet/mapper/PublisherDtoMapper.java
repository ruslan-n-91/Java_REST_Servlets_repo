package servlet.mapper;

import entity.Publisher;
import servlet.dto.PublisherIncomingDto;
import servlet.dto.PublisherOutgoingDto;

import java.util.List;

public interface PublisherDtoMapper {
    Publisher mapToPublisher(PublisherIncomingDto publisherIncomingDto);

    PublisherOutgoingDto mapToPublisherOutgoingDto(Publisher publisher);

    List<PublisherOutgoingDto> mapToListOfPublisherOutgoingDtos(List<Publisher> listOfPublishers);
}
