package servlet.mapper.impl;

import entity.Author;
import entity.Book;
import servlet.dto.AuthorOutgoingDto;
import servlet.dto.BookOutgoingDto;
import servlet.dto.BookIncomingDto;
import servlet.mapper.BookDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

public class BookDtoMapperImpl implements BookDtoMapper {
    @Override
    public Book mapToBook(BookIncomingDto bookIncomingDto) {
        return new Book(bookIncomingDto.getId(), bookIncomingDto.getTitle(), bookIncomingDto.getQuantity(),
                bookIncomingDto.getAuthors().stream()
                        .map(authorIncomingDto ->
                                new Author(authorIncomingDto.getId(), authorIncomingDto.getName(), null)
                        ).collect(Collectors.toSet()));
    }

    @Override
    public BookOutgoingDto mapToBookOutgoingDto(Book book) {
        return new BookOutgoingDto(book.getId(), book.getTitle(), book.getQuantity(),
                book.getAuthors().stream()
                        .map(author ->
                                new AuthorOutgoingDto(author.getId(), author.getName(), null)
                        ).collect(Collectors.toSet()));
    }

    @Override
    public List<BookOutgoingDto> mapToListOfBookOutgoingDtos(List<Book> listOfBooks) {
        return listOfBooks.stream().map(this::mapToBookOutgoingDto).toList();
    }
}
