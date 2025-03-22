package dao.impl;

import dao.Dao;
import db.ConnectionManager;
import db.ConnectionManagerImpl;
import entity.Author;
import entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthorDaoImpl implements Dao<Author, Integer> {
    private final ConnectionManager connectionManager;

    public AuthorDaoImpl() {
        connectionManager = new ConnectionManagerImpl();
    }

    public AuthorDaoImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Returns a list of all authors from the database.
     */
    @Override
    public List<Author> findAll() {
        List<Author> listOfAuthors = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, name FROM authors")) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getInt("id"));
                author.setName(resultSet.getString("name"));
                author.setBooks(findAllBooksForAuthor(author.getId()));

                listOfAuthors.add(author);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listOfAuthors;
    }

    /**
     * Returns an author with the specified id from the database.
     */
    @Override
    public Author findById(Integer id) {
        Author author = null;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, name FROM authors WHERE id = ?")) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                author = new Author();
                author.setId(resultSet.getInt("id"));
                author.setName(resultSet.getString("name"));
                author.setBooks(findAllBooksForAuthor(author.getId()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return author;
    }

    /**
     * Saves the author in the database.
     */
    @Override
    public void save(Author author) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO authors (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, author.getName());
            preparedStatement.executeUpdate();

            if (author.getBooks() != null && !author.getBooks().isEmpty()) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();

                Integer generatedKey = null;

                if (resultSet.next()) {
                    generatedKey = resultSet.getInt("id");
                }

                removeBooksFromAuthor(generatedKey);
                addBooksToAuthor(generatedKey, author.getBooks());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the specified author entry in the database.
     */
    @Override
    public void update(Author author) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE authors SET name=? WHERE id=?")) {

            preparedStatement.setString(1, author.getName());
            preparedStatement.setInt(2, author.getId());
            preparedStatement.executeUpdate();

            removeBooksFromAuthor(author.getId());
            addBooksToAuthor(author.getId(), author.getBooks());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes an author with the specified id from the database.
     */
    @Override
    public void delete(Integer id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM authors WHERE id=?")) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds all books that relates to the author with the specified id.
     */
    private Set<Book> findAllBooksForAuthor(Integer id) {
        Set<Book> books = new HashSet<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT books.id, books.title, books.quantity FROM books_authors INNER JOIN books " +
                             "ON books_authors.book_id = books.id WHERE books_authors.author_id = ?")) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setQuantity(resultSet.getInt("quantity"));

                books.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return books;
    }

    /**
     * Adds all books from the set to the author with the specified id.
     */
    private void addBooksToAuthor(Integer id, Set<Book> books) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO books_authors (book_id, author_id) VALUES (?, ?)")) {

            preparedStatement.setInt(2, id);

            for (Book book : books) {
                preparedStatement.setInt(1, book.getId());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes all books from the author with the specified id.
     */
    private void removeBooksFromAuthor(Integer id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM books_authors WHERE author_id=?")) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
