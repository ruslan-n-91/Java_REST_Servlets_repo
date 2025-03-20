package dao.impl;

import dao.BookDao;
import db.ConnectionManager;
import db.ConnectionManagerImpl;
import entity.Author;
import entity.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Testcontainers
class BookDaoImplTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    ).withInitScript("sql/sql_script.sql");

    static BookDao bookDao;

    @BeforeAll
    static void setUp() {
        ConnectionManager connectionProvider = new ConnectionManagerImpl(
                postgres.getDriverClassName(),
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        bookDao = new BookDaoImpl(connectionProvider);
    }

    @Test
    void findAllShouldReturnAllEntriesFromDb() {
        List<Book> books = bookDao.findAll();

        Assertions.assertEquals(8, books.size());
        Assertions.assertEquals("Java The Complete Reference 9th Edition", books.get(0).getTitle());
        Assertions.assertEquals("Grokking Algorithms", books.get(2).getTitle());
    }

    @Test
    void findByIdShouldReturnEntryWithSpecifiedIdFromDb() {
        Book book = bookDao.findById(3);
        Author author = new Author(6, "Aditya Y. Bhargava", null);

        Assertions.assertEquals(3, book.getId());
        Assertions.assertEquals("Grokking Algorithms", book.getTitle());
        Assertions.assertTrue(book.getAuthors().contains(author));
    }

    @Test
    void saveShouldCreateNewEntryInDb() {
        Book newBook1 = new Book(null, "Book 5555", 10, null);

        Set<Author> authors = new HashSet<>();
        authors.add(new Author(6, null, null));
        authors.add(new Author(1, null, null));
        Book newBook2 = new Book(null, "Book 9999", 15, authors);

        bookDao.save(newBook1);
        bookDao.save(newBook2);

        Book book1 = bookDao.findById(7);
        Book book2 = bookDao.findById(8);

        Assertions.assertEquals(7, book1.getId());
        Assertions.assertEquals("Book 5555", book1.getTitle());
        Assertions.assertEquals(10, book1.getQuantity());
        Assertions.assertEquals(0, book1.getAuthors().size());

        Assertions.assertEquals(8, book2.getId());
        Assertions.assertEquals("Book 9999", book2.getTitle());
        Assertions.assertEquals(15, book2.getQuantity());
        Assertions.assertEquals(2, book2.getAuthors().size());
        Assertions.assertTrue(book2.getAuthors().contains(new Author(6, "Aditya Y. Bhargava", null)));
        Assertions.assertTrue(book2.getAuthors().contains(new Author(1, "Herbert Schildt", null)));
    }

    @Test
    void updateShouldChangeEntryInDb() {
        Set<Author> authors = new HashSet<>();
        authors.add(new Author(9, null, null));
        Book newBook = new Book(5, "Book 3333", 20, authors);

        bookDao.update(newBook);

        Book book = bookDao.findById(5);

        Assertions.assertEquals(5, book.getId());
        Assertions.assertEquals("Book 3333", book.getTitle());
        Assertions.assertEquals(20, book.getQuantity());
        Assertions.assertEquals(1, book.getAuthors().size());
        Assertions.assertTrue(book.getAuthors().contains(new Author(9, "Frank Herbert", null)));
    }

    @Test
    void deleteShouldRemoveEntryFromDb() {
        bookDao.delete(5);
        bookDao.delete(7);
        bookDao.delete(8);

        List<Book> books = bookDao.findAll();

        Assertions.assertEquals(5, books.size());
    }
}
