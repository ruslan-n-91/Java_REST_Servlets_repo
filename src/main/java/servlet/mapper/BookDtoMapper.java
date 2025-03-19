package servlet.mapper;

import entity.Book;
import servlet.dto.BookOutgoingDto;
import servlet.dto.BookIncomingDto;

import java.util.List;

public interface BookDtoMapper {
    Book mapToBook(BookIncomingDto bookIncomingDto);

    BookOutgoingDto mapToBookOutgoingDto(Book book);

    List<BookOutgoingDto> mapToListOfBookOutgoingDtos(List<Book> listOfBooks);
}
