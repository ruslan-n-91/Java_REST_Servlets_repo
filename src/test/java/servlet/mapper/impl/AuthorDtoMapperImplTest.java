package servlet.mapper.impl;

import entity.Author;
import entity.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servlet.dto.AuthorIncomingDto;
import servlet.dto.AuthorOutgoingDto;
import servlet.dto.BookIncomingDto;
import servlet.mapper.AuthorDtoMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class AuthorDtoMapperImplTest {
    private AuthorDtoMapper authorDtoMapper;

    @BeforeEach
    void setUp() {
        authorDtoMapper = new AuthorDtoMapperImpl();
    }

    @Test
    void mapToAuthor_ShouldReturnValidObject() {
        AuthorIncomingDto dto = new AuthorIncomingDto(18, "author",
                Set.of(new BookIncomingDto(14, "book", 77, null)));

        Author author = authorDtoMapper.mapToAuthor(dto);

        Assertions.assertEquals(dto.getId(), author.getId());
        Assertions.assertEquals(dto.getName(), author.getName());
        Assertions.assertEquals(dto.getBooks().size(), author.getBooks().size());
    }

    @Test
    void mapToAuthorOutgoingDto_ShouldReturnValidDto() {
        Author author = new Author(18, "author",
                Set.of(new Book()));

        AuthorOutgoingDto dto = authorDtoMapper.mapToAuthorOutgoingDto(author);

        Assertions.assertEquals(author.getId(), dto.getId());
        Assertions.assertEquals(author.getName(), dto.getName());
        Assertions.assertEquals(author.getBooks().size(), dto.getBooks().size());
    }

    @Test
    void mapToListOfAuthorOutgoingDtos_ShouldReturnValidList() {
        List<Author> list = List.of(new Author(19, "author", new HashSet<>()),
                new Author(20, "author", new HashSet<>()));
        List<AuthorOutgoingDto> listDto = authorDtoMapper.mapToListOfAuthorOutgoingDtos(list);

        Assertions.assertEquals(list.size(), listDto.size());
    }
}
