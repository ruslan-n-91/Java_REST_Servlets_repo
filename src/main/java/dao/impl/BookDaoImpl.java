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

public class BookDaoImpl implements Dao<Book, Integer> {
    private final ConnectionManager connectionManager;

    public BookDaoImpl() {
        connectionManager = new ConnectionManagerImpl();
    }

    public BookDaoImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Returns a list of all books from the database.
     */
    @Override
    public List<Book> findAll() {
        List<Book> listOfBooks = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, title, quantity FROM books")) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setQuantity(resultSet.getInt("quantity"));
                book.setAuthors(findAllAuthorsForBook(book.getId()));

                listOfBooks.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listOfBooks;
    }

    /**
     * Returns a book with the specified id from the database.
     */
    @Override
    public Book findById(Integer id) {
        Book book = null;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, title, quantity FROM books WHERE id = ?")) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setQuantity(resultSet.getInt("quantity"));
                book.setAuthors(findAllAuthorsForBook(book.getId()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return book;
    }

    /**
     * Saves the book in the database.
     */
    @Override
    public void save(Book book) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO books (title, quantity) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setInt(2, book.getQuantity());
            preparedStatement.executeUpdate();

            if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();

                Integer generatedKey = null;

                if (resultSet.next()) {
                    generatedKey = resultSet.getInt("id");
                }

                removeAuthorsFromBook(generatedKey);
                addAuthorsToBook(generatedKey, book.getAuthors());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the specified book entry in the database.
     */
    @Override
    public void update(Book book) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE books SET title=?, quantity=? WHERE id=?")) {

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setInt(2, book.getQuantity());
            preparedStatement.setInt(3, book.getId());
            preparedStatement.executeUpdate();

            removeAuthorsFromBook(book.getId());
            addAuthorsToBook(book.getId(), book.getAuthors());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes a book with the specified id from the database.
     */
    @Override
    public void delete(Integer id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM books WHERE id=?")) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds all authors that relates to the book with the specified id.
     */
    private Set<Author> findAllAuthorsForBook(Integer id) {
        Set<Author> authors = new HashSet<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT authors.id, authors.name FROM books_authors INNER JOIN authors " +
                             "ON books_authors.author_id = authors.id WHERE books_authors.book_id = ?")) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getInt("id"));
                author.setName(resultSet.getString("name"));

                authors.add(author);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return authors;
    }

    /**
     * Adds all authors from the set to the book with the specified id.
     */
    private void addAuthorsToBook(Integer id, Set<Author> authors) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO books_authors (book_id, author_id) VALUES (?, ?)")) {

            preparedStatement.setInt(1, id);

            for (Author author : authors) {
                preparedStatement.setInt(2, author.getId());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes all authors from the book with the specified id.
     */
    private void removeAuthorsFromBook(Integer id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM books_authors WHERE book_id=?")) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
