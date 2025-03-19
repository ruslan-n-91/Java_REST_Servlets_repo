package servlet.mapper.impl;

import entity.Author;
import entity.Book;
import servlet.dto.AuthorIncomingDto;
import servlet.dto.AuthorOutgoingDto;
import servlet.dto.BookOutgoingDto;
import servlet.mapper.AuthorDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorDtoMapperImpl implements AuthorDtoMapper {
    @Override
    public Author mapToAuthor(AuthorIncomingDto authorIncomingDto) {
        return new Author(authorIncomingDto.getId(), authorIncomingDto.getName(),
                authorIncomingDto.getBooks().stream()
                        .map(bookIncomingDto ->
                                new Book(bookIncomingDto.getId(), bookIncomingDto.getTitle(),
                                        bookIncomingDto.getQuantity(), null)
                        ).collect(Collectors.toSet()));
    }

    @Override
    public AuthorOutgoingDto mapToAuthorOutgoingDto(Author author) {
        return new AuthorOutgoingDto(author.getId(), author.getName(),
                author.getBooks().stream()
                        .map(book ->
                                new BookOutgoingDto(book.getId(), book.getTitle(),
                                        book.getQuantity(), null)
                        ).collect(Collectors.toSet()));
    }

    @Override
    public List<AuthorOutgoingDto> mapToListOfAuthorOutgoingDtos(List<Author> listOfAuthors) {
        return listOfAuthors.stream().map(this::mapToAuthorOutgoingDto).toList();
    }
}
