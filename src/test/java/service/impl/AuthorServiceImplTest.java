package service.impl;

import dao.Dao;
import entity.Author;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import servlet.dto.AuthorIncomingDto;
import servlet.dto.AuthorOutgoingDto;

import java.util.HashSet;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {
    @InjectMocks
    AuthorServiceImpl authorService;

    @Mock
    Dao<Author, Integer> mockAuthorDao;

    @AfterEach
    void tearDown() {
        Mockito.reset(mockAuthorDao);
    }

    @Test
    void findAllShouldCallFindAllFromAuthorDao() {
        authorService.findAll();

        Mockito.verify(mockAuthorDao).findAll();
    }

    @Test
    void findByIdShouldCallFindByIdFromAuthorDao_AndShouldReturnValidDto() {
        Author author = new Author(17, "Some Author 17", new HashSet<>());

        Mockito.doReturn(author).when(mockAuthorDao).findById(17);

        AuthorOutgoingDto dto = authorService.findById(17);

        Mockito.verify(mockAuthorDao).findById(17);
        Assertions.assertEquals(author.getId(), dto.getId());
        Assertions.assertEquals(author.getName(), dto.getName());
        Assertions.assertEquals(author.getBooks().size(), dto.getBooks().size());
    }

    @Test
    void saveShouldCallSaveFromAuthorDao() {
        AuthorIncomingDto dto = new AuthorIncomingDto(null, "Some Author 18", new HashSet<>());
        Author author = new Author(null, "Some Author 18", new HashSet<>());

        authorService.save(dto);

        Mockito.verify(mockAuthorDao).save(author);
    }

    @Test
    void updateShouldCallUpdateFromAuthorDao() {
        AuthorIncomingDto dto = new AuthorIncomingDto(76, "Some Author 76", new HashSet<>());
        Author author = new Author(76, "Some Author 76", new HashSet<>());

        authorService.update(dto);

        Mockito.verify(mockAuthorDao).update(author);
    }

    @Test
    void deleteShouldCallDeleteFromAuthorDao() {
        authorService.delete(8);

        Mockito.verify(mockAuthorDao).delete(8);
    }
}
