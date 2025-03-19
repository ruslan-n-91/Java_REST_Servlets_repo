package service.impl;

import dao.AuthorDao;
import dao.impl.AuthorDaoImpl;
import entity.Author;
import service.AuthorService;
import servlet.dto.AuthorIncomingDto;
import servlet.dto.AuthorOutgoingDto;
import servlet.mapper.AuthorDtoMapper;
import servlet.mapper.impl.AuthorDtoMapperImpl;

import java.util.List;

public class AuthorServiceImpl implements AuthorService {
    private AuthorDao authorDao = new AuthorDaoImpl();
    private AuthorDtoMapper authorDtoMapper = new AuthorDtoMapperImpl();

    @Override
    public List<AuthorOutgoingDto> findAll() {
        List<Author> listOfAuthors = authorDao.findAll();
        return authorDtoMapper.mapToListOfAuthorOutgoingDtos(listOfAuthors);
    }

    @Override
    public AuthorOutgoingDto findById(Integer id) {
        Author author = authorDao.findById(id);
        return authorDtoMapper.mapToAuthorOutgoingDto(author);
    }

    @Override
    public void save(AuthorIncomingDto authorIncomingDto) {
        authorDao.save(authorDtoMapper.mapToAuthor(authorIncomingDto));
    }

    @Override
    public void update(AuthorIncomingDto authorIncomingDto) {
        authorDao.update(authorDtoMapper.mapToAuthor(authorIncomingDto));
    }

    @Override
    public void delete(Integer id) {
        authorDao.delete(id);
    }
}
