package service.impl;

import dao.PublisherDao;
import dao.impl.PublisherDaoImpl;
import entity.Publisher;
import service.PublisherService;
import servlet.dto.PublisherIncomingDto;
import servlet.dto.PublisherOutgoingDto;
import servlet.mapper.PublisherDtoMapper;
import servlet.mapper.impl.PublisherDtoMapperImpl;

import java.util.List;

public class PublisherServiceImpl implements PublisherService {
    private PublisherDao publisherDao = new PublisherDaoImpl();
    private PublisherDtoMapper publisherDtoMapper = new PublisherDtoMapperImpl();

    @Override
    public List<PublisherOutgoingDto> findAll() {
        List<Publisher> listOfPublishers = publisherDao.findAll();
        return publisherDtoMapper.mapToListOfPublisherOutgoingDtos(listOfPublishers);
    }

    @Override
    public PublisherOutgoingDto findById(Integer id) {
        Publisher publisher = publisherDao.findById(id);
        return publisherDtoMapper.mapToPublisherOutgoingDto(publisher);
    }

    @Override
    public void save(PublisherIncomingDto publisherIncomingDto) {
        publisherDao.save(publisherDtoMapper.mapToPublisher(publisherIncomingDto));
    }

    @Override
    public void update(PublisherIncomingDto publisherIncomingDto) {
        publisherDao.update(publisherDtoMapper.mapToPublisher(publisherIncomingDto));
    }

    @Override
    public void delete(Integer id) {
        publisherDao.delete(id);
    }
}
