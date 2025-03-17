package dao.impl;

import dao.PublisherDao;
import db.ConnectionManager;
import db.ConnectionManagerImpl;
import entity.Publisher;
import entity.Magazine;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PublisherDaoImpl implements PublisherDao {
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();

    @Override
    public List<Publisher> findAll() {
        List<Publisher> listOfPublishers = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, name FROM publishers")) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Publisher publisher = new Publisher();
                publisher.setId(resultSet.getInt("id"));
                publisher.setName(resultSet.getString("name"));
                publisher.setMagazines(findAllMagazinesForPublisher(publisher.getId()));

                listOfPublishers.add(publisher);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listOfPublishers;
    }

    @Override
    public Publisher findById(Integer id) {
        Publisher publisher = null;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, name FROM publishers WHERE id = ?")) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                publisher = new Publisher();
                publisher.setId(resultSet.getInt("id"));
                publisher.setName(resultSet.getString("name"));
                publisher.setMagazines(findAllMagazinesForPublisher(publisher.getId()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return publisher;
    }

    @Override
    public void save(Publisher publisher) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO publishers (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, publisher.getName());
            preparedStatement.executeUpdate();

            if (publisher.getMagazines() != null) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();

                Integer generatedKey = null;

                if (resultSet.next()) {
                    generatedKey = resultSet.getInt("id");
                }

                removeMagazinesFromPublisher(generatedKey);
                addMagazinesToPublisher(generatedKey, publisher.getMagazines());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Publisher publisher) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE publishers SET name=? WHERE id=?")) {

            preparedStatement.setString(1, publisher.getName());
            preparedStatement.setInt(2, publisher.getId());
            preparedStatement.executeUpdate();

            if (publisher.getMagazines() != null) {
                removeMagazinesFromPublisher(publisher.getId());
                addMagazinesToPublisher(publisher.getId(), publisher.getMagazines());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM publishers WHERE id=?")) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Magazine> findAllMagazinesForPublisher(Integer id) {
        Set<Magazine> magazines = new HashSet<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT magazines.id, magazines.title, magazines.quantity FROM magazines INNER JOIN publishers " +
                             "ON magazines.publisher_id = publishers.id WHERE publishers.id = ?")) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Magazine magazine = new Magazine();
                magazine.setId(resultSet.getInt("id"));
                magazine.setTitle(resultSet.getString("title"));
                magazine.setQuantity(resultSet.getInt("quantity"));

                magazines.add(magazine);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return magazines;
    }

    private void addMagazinesToPublisher(Integer id, Set<Magazine> magazines) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE magazines SET publisher_id=? WHERE id=?")) {

            preparedStatement.setInt(1, id);

            for (Magazine magazine : magazines) {
                preparedStatement.setInt(2, magazine.getId());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeMagazinesFromPublisher(Integer id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE magazines SET publisher_id=null WHERE id=?")) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
