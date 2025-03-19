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
    private BookDao bookDao = new BookDaoImpl();
    private BookDtoMapper bookDtoMapper = new BookDtoMapperImpl();

    @Override
    public List<BookOutgoingDto> findAll() {
        List<Book> listOfBooks = bookDao.findAll();
        return bookDtoMapper.mapToListOfBookOutgoingDtos(listOfBooks);
    }

    @Override
    public BookOutgoingDto findById(Integer id) {
        Book book = bookDao.findById(id);
        return bookDtoMapper.mapToBookOutgoingDto(book);
    }

    @Override
    public void save(BookIncomingDto bookIncomingDto) {
        bookDao.save(bookDtoMapper.mapToBook(bookIncomingDto));
    }

    @Override
    public void update(BookIncomingDto bookIncomingDto) {
        bookDao.update(bookDtoMapper.mapToBook(bookIncomingDto));
    }

    @Override
    public void delete(Integer id) {
        bookDao.delete(id);
    }
}
