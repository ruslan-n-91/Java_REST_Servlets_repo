package dao.impl;

import dao.MagazineDao;
import db.ConnectionManager;
import db.ConnectionManagerImpl;
import entity.Magazine;
import entity.Publisher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MagazineDaoImpl implements MagazineDao {
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();

    /**
     * Returns a list of all magazines from the database.
     */
    @Override
    public List<Magazine> findAll() {
        List<Magazine> listOfMagazines = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, title, quantity FROM magazines")) {

            ResultSet resultSet = preparedStatement.executeQuery();

            // execute the sql query and add all magazines (and their publishers)
            // from the database to the list
            while (resultSet.next()) {
                Magazine magazine = new Magazine();
                magazine.setId(resultSet.getInt("id"));
                magazine.setTitle(resultSet.getString("title"));
                magazine.setQuantity(resultSet.getInt("quantity"));
                magazine.setPublisher(findPublisherForMagazine(magazine.getId()));

                listOfMagazines.add(magazine);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listOfMagazines;
    }

    /**
     * Returns a magazine with the specified id from the database.
     */
    @Override
    public Magazine findById(Integer id) {
        Magazine magazine = null;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, title, quantity FROM magazines WHERE id = ?")) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            // execute the sql query and return the magazine (and its publisher)
            // with the specified id from the database
            if (resultSet.next()) {
                magazine = new Magazine();
                magazine.setId(resultSet.getInt("id"));
                magazine.setTitle(resultSet.getString("title"));
                magazine.setQuantity(resultSet.getInt("quantity"));
                magazine.setPublisher(findPublisherForMagazine(magazine.getId()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return magazine;
    }

    /**
     * Saves the magazine in the database.
     */
    @Override
    public void save(Magazine magazine) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO magazines (title, quantity) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, magazine.getTitle());
            preparedStatement.setInt(2, magazine.getQuantity());
            preparedStatement.executeUpdate();

            // if there is publisher for this magazine in the magazine entity
            // then change the relevant entry in the magazines table
            if (magazine.getPublisher() != null) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();

                Integer generatedKey = null;

                if (resultSet.next()) {
                    generatedKey = resultSet.getInt("id");
                }

                updatePublisherForMagazine(generatedKey, magazine.getPublisher().getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the specified magazine entry in the database.
     */
    @Override
    public void update(Magazine magazine) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE magazines SET title=?, quantity=? WHERE id=?")) {

            preparedStatement.setString(1, magazine.getTitle());
            preparedStatement.setInt(2, magazine.getQuantity());
            preparedStatement.setInt(3, magazine.getId());
            preparedStatement.executeUpdate();

            // if there is publisher for this magazine in the magazine entity
            // then change the relevant entry in the magazines table
            updatePublisherForMagazine(magazine.getId(), magazine.getPublisher().getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes a magazine with the specified id from the database.
     */
    @Override
    public void delete(Integer id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM magazines WHERE id=?")) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds a publisher that relates to the magazine with the specified id.
     */
    private Publisher findPublisherForMagazine(Integer id) {
        Publisher publisher = new Publisher();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT publishers.id, publishers.name FROM magazines INNER JOIN publishers " +
                             "ON magazines.publisher_id = publishers.id WHERE magazines.id = ?")) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                publisher.setId(resultSet.getInt("id"));
                publisher.setName(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return publisher;
    }

    /**
     * Adds or removes a publisher from the magazine with the specified id.
     */
    private void updatePublisherForMagazine(Integer id, Integer publisherId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE magazines SET publisher_id=? WHERE id=?")) {

            if (publisherId != null) {
                preparedStatement.setInt(1, publisherId);
            } else {
                preparedStatement.setNull(1, java.sql.Types.INTEGER);
            }

            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
