package dao.impl;

import dao.AuthorDao;
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
class AuthorDaoImplTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    ).withInitScript("sql/sql_script.sql");

    static AuthorDao authorDao;

    @BeforeAll
    static void setUp() {
        ConnectionManager connectionProvider = new ConnectionManagerImpl(
                postgres.getDriverClassName(),
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        authorDao = new AuthorDaoImpl(connectionProvider);
    }

    @Test
    void findAllShouldReturnAllEntriesFromDb() {
        List<Author> authors = authorDao.findAll();

        Assertions.assertEquals(11, authors.size());
        Assertions.assertEquals("Herbert Schildt", authors.get(0).getName());
        Assertions.assertEquals("Erich Gamma", authors.get(1).getName());
    }

    @Test
    void findByIdShouldReturnEntryWithSpecifiedIdFromDb() {
        Author author = authorDao.findById(3);
        Book book = new Book(2, "Design Patterns: Elements of Reusable Object-Oriented Software",
                30, null);

        Assertions.assertEquals(3, author.getId());
        Assertions.assertEquals("Richard Helm", author.getName());
        Assertions.assertTrue(author.getBooks().contains(book));
    }

    @Test
    void saveShouldCreateNewEntryInDb() {
        Author newAuthor1 = new Author(null, "Author 5555", null);

        Set<Book> books = new HashSet<>();
        books.add(new Book(3, null, null, null));
        books.add(new Book(4, null, null, null));
        Author newAuthor2 = new Author(null, "Author 9999", books);

        authorDao.save(newAuthor1);
        authorDao.save(newAuthor2);

        Author author1 = authorDao.findById(10);
        Author author2 = authorDao.findById(11);

        Assertions.assertEquals(10, author1.getId());
        Assertions.assertEquals("Author 5555", author1.getName());
        Assertions.assertEquals(0, author1.getBooks().size());

        Assertions.assertEquals(11, author2.getId());
        Assertions.assertEquals("Author 9999", author2.getName());
        Assertions.assertEquals(2, author2.getBooks().size());
        Assertions.assertTrue(author2.getBooks().contains(new Book(3, "Grokking Algorithms",
                25, null)));
        Assertions.assertTrue(author2.getBooks().contains(new Book(4, "War and Peace",
                30, null)));
    }

    @Test
    void updateShouldChangeEntryInDb() {
        Set<Book> books = new HashSet<>();
        books.add(new Book(5, null, null, null));
        Author newAuthor = new Author(9, "Author 3333", books);

        authorDao.update(newAuthor);

        Author author = authorDao.findById(9);

        Assertions.assertEquals(9, author.getId());
        Assertions.assertEquals("Author 3333", author.getName());
        Assertions.assertEquals(1, author.getBooks().size());
        Assertions.assertTrue(author.getBooks().contains(new Book(5, "Adventures of Huckleberry Finn",
                50, null)));
    }

    @Test
    void deleteShouldRemoveEntryFromDb() {
        authorDao.delete(8);
        authorDao.delete(9);

        List<Author> authors = authorDao.findAll();

        Assertions.assertEquals(9, authors.size());
    }
}
