package servlet.mapper.impl;

import entity.Author;
import entity.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servlet.dto.AuthorIncomingDto;
import servlet.dto.BookIncomingDto;
import servlet.dto.BookOutgoingDto;
import servlet.mapper.BookDtoMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class BookDtoMapperImplTest {
    private BookDtoMapper bookDtoMapper;

    @BeforeEach
    void setUp() {
        bookDtoMapper = new BookDtoMapperImpl();
    }

    @Test
    void mapToBook_ShouldReturnValidObject() {
        BookIncomingDto dto = new BookIncomingDto(15, "book", 45,
                Set.of(new AuthorIncomingDto(14, "author", null)));

        Book book = bookDtoMapper.mapToBook(dto);

        Assertions.assertEquals(dto.getId(), book.getId());
        Assertions.assertEquals(dto.getTitle(), book.getTitle());
        Assertions.assertEquals(dto.getQuantity(), book.getQuantity());
        Assertions.assertEquals(dto.getAuthors().size(), book.getAuthors().size());
    }

    @Test
    void mapToBookOutgoingDto_ShouldReturnValidDto() {
        Book book = new Book(15, "book", 45,
                Set.of(new Author()));

        BookOutgoingDto dto = bookDtoMapper.mapToBookOutgoingDto(book);

        Assertions.assertEquals(book.getId(), dto.getId());
        Assertions.assertEquals(book.getTitle(), dto.getTitle());
        Assertions.assertEquals(book.getQuantity(), dto.getQuantity());
        Assertions.assertEquals(book.getAuthors().size(), dto.getAuthors().size());
    }

    @Test
    void mapToListOfBookOutgoingDtos_ShouldReturnValidList() {
        List<Book> list = List.of(new Book(11, "book", 4, new HashSet<>()),
                new Book(12, "book", 5, new HashSet<>()));
        List<BookOutgoingDto> listDto = bookDtoMapper.mapToListOfBookOutgoingDtos(list);

        Assertions.assertEquals(list.size(), listDto.size());
    }
}
