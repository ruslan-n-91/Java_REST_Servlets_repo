package service;

import servlet.dto.PublisherIncomingDto;
import servlet.dto.PublisherOutgoingDto;

import java.util.List;

public interface PublisherService {
    List<PublisherOutgoingDto> findAll();

    PublisherOutgoingDto findById(Integer id);

    void save(PublisherIncomingDto publisherIncomingDto);

    void update(PublisherIncomingDto publisherIncomingDto);

    void delete(Integer id);
}
