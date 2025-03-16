package service;

import servlet.dto.BookOutgoingDto;
import servlet.dto.BookIncomingDto;

import java.util.List;

public interface BookService {
    List<BookOutgoingDto> findAll();

    BookOutgoingDto findById(Integer id);

    void save(BookIncomingDto bookIncomingDto);

    void update(BookIncomingDto bookIncomingDto);

    void delete(Integer id);
}
