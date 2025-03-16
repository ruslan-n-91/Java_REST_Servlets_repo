package dao.impl;

import dao.AuthorDao;
import db.ConnectionManager;
import db.ConnectionManagerImpl;
import entity.Author;
import entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthorDaoImpl implements AuthorDao {
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();

    @Override
    public List<Author> findAll() {
        List<Author> listOfAuthors = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM authors")) {

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

    @Override
    public Author findById(Integer id) {
        Author author = null;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM authors WHERE id = ?")) {

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

    @Override
    public void save(Author author) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO authors (name) VALUES(?)", PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, author.getName());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            Integer generatedKey = null;

            if (resultSet.next()) {
                generatedKey = resultSet.getInt("id");
            }

            removeBooksFromAuthor(generatedKey);
            addBooksToAuthor(generatedKey, author.getBooks());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

    private void addBooksToAuthor(Integer id, Set<Book> books) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO books_authors (book_id, author_id) VALUES(?, ?)")) {

            for (Book book : books) {
                preparedStatement.setInt(1, book.getId());
                preparedStatement.setInt(2, id);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
