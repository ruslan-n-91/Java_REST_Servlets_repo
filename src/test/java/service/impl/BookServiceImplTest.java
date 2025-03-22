package service.impl;

import dao.Dao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import servlet.dto.BookIncomingDto;
import servlet.dto.BookOutgoingDto;
import entity.Book;

import java.util.HashSet;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    Dao<Book, Integer> mockBookDao;

    @AfterEach
    void tearDown() {
        Mockito.reset(mockBookDao);
    }

    @Test
    void findAllShouldCallFindAllFromBookDao() {
        bookService.findAll();

        Mockito.verify(mockBookDao).findAll();
    }

    @Test
    void findByIdShouldCallFindByIdFromBookDao_AndShouldReturnValidDto() {
        Book book = new Book(15, "Some Book 15", 20, new HashSet<>());

        Mockito.doReturn(book).when(mockBookDao).findById(15);

        BookOutgoingDto dto = bookService.findById(15);

        Mockito.verify(mockBookDao).findById(15);
        Assertions.assertEquals(book.getId(), dto.getId());
        Assertions.assertEquals(book.getTitle(), dto.getTitle());
        Assertions.assertEquals(book.getQuantity(), dto.getQuantity());
        Assertions.assertEquals(book.getAuthors().size(), dto.getAuthors().size());
    }

    @Test
    void saveShouldCallSaveFromBookDao() {
        BookIncomingDto dto = new BookIncomingDto(null, "Some Book 11", 14, new HashSet<>());
        Book book = new Book(null, "Some Book 11", 14, new HashSet<>());

        bookService.save(dto);

        Mockito.verify(mockBookDao).save(book);
    }

    @Test
    void updateShouldCallUpdateFromBookDao() {
        BookIncomingDto dto = new BookIncomingDto(54, "Some Book 54", 33, new HashSet<>());
        Book book = new Book(54, "Some Book 54", 33, new HashSet<>());

        bookService.update(dto);

        Mockito.verify(mockBookDao).update(book);
    }

    @Test
    void deleteShouldCallDeleteFromBookDao() {
        bookService.delete(3);

        Mockito.verify(mockBookDao).delete(3);
    }
}
