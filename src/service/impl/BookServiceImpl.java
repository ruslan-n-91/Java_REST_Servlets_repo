package service.impl;

import dao.BookDao;
import dao.impl.BookDaoImpl;
import service.BookService;
import servlet.dto.BookOutgoingDto;
import servlet.dto.BookIncomingDto;
import entity.Book;
import servlet.mapper.BookDtoMapper;
import servlet.mapper.impl.BookDtoMapperImpl;

import java.util.List;

public class BookServiceImpl implements BookService {
    private final BookDao bookDAO = new BookDaoImpl();
    private final BookDtoMapper bookDtoMapper = new BookDtoMapperImpl();

    @Override
    public List<BookOutgoingDto> findAll() {
        List<Book> listOfBooks = bookDAO.findAll();
        return bookDtoMapper.mapToListOfBookOutgoingDtos(listOfBooks);
    }

    @Override
    public BookOutgoingDto findById(Integer id) {
        Book book = bookDAO.findById(id);
        return bookDtoMapper.mapToBookOutgoingDto(book);
    }

    @Override
    public void save(BookIncomingDto bookIncomingDto) {
        bookDAO.save(bookDtoMapper.mapToBook(bookIncomingDto));
    }

    @Override
    public void update(BookIncomingDto bookIncomingDto) {
        bookDAO.update(bookDtoMapper.mapToBook(bookIncomingDto));
    }

    @Override
    public void delete(Integer id) {
        bookDAO.delete(id);
    }
}
