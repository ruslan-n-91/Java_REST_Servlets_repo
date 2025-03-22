package dao.impl;

import dao.Dao;
import db.ConnectionManager;
import db.ConnectionManagerImpl;
import entity.Magazine;
import entity.Publisher;
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
class PublisherDaoImplTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    ).withInitScript("sql/sql_script.sql");

    static Dao<Publisher, Integer> publisherDao;

    @BeforeAll
    static void setUp() {
        ConnectionManager connectionProvider = new ConnectionManagerImpl(
                postgres.getDriverClassName(),
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        publisherDao = new PublisherDaoImpl(connectionProvider);
    }

    @Test
    void findAllShouldReturnAllEntriesFromDb() {
        List<Publisher> publishers = publisherDao.findAll();

        Assertions.assertEquals(5, publishers.size());
        Assertions.assertEquals("National Geographic Society", publishers.get(0).getName());
    }

    @Test
    void findByIdShouldReturnEntryWithSpecifiedIdFromDb() {
        Publisher publisher = publisherDao.findById(1);
        Magazine magazine = new Magazine(1, "National Geographic", 50, null);

        Assertions.assertEquals(1, publisher.getId());
        Assertions.assertEquals("National Geographic Society", publisher.getName());
        Assertions.assertTrue(publisher.getMagazines().contains(magazine));
    }

    @Test
    void saveShouldCreateNewEntryInDb() {
        Publisher newPublisher1 = new Publisher(null, "Publisher 5555", null);

        Set<Magazine> magazines = new HashSet<>();
        magazines.add(new Magazine(5, null, null, null));
        magazines.add(new Magazine(6, null, null, null));
        Publisher newPublisher2 = new Publisher(null, "Publisher 9999", magazines);

        publisherDao.save(newPublisher1);
        publisherDao.save(newPublisher2);

        Publisher publisher1 = publisherDao.findById(4);
        Publisher publisher2 = publisherDao.findById(5);

        Assertions.assertEquals(4, publisher1.getId());
        Assertions.assertEquals("Publisher 5555", publisher1.getName());
        Assertions.assertEquals(0, publisher1.getMagazines().size());

        Assertions.assertEquals(5, publisher2.getId());
        Assertions.assertEquals("Publisher 9999", publisher2.getName());
        Assertions.assertEquals(2, publisher2.getMagazines().size());
        Assertions.assertTrue(publisher2.getMagazines().contains(new Magazine(5, "BBC Gardeners' World",
                20, null)));
        Assertions.assertTrue(publisher2.getMagazines().contains(new Magazine(6, "BBC Top Gear Magazine",
                20, null)));
    }

    @Test
    void updateShouldChangeEntryInDb() {
        Set<Magazine> magazines = new HashSet<>();
        magazines.add(new Magazine(2, null, null, null));
        Publisher newPublisher = new Publisher(3, "Publisher 3333", magazines);

        publisherDao.update(newPublisher);

        Publisher publisher = publisherDao.findById(3);

        Assertions.assertEquals(3, publisher.getId());
        Assertions.assertEquals("Publisher 3333", publisher.getName());
        Assertions.assertEquals(1, publisher.getMagazines().size());
        Assertions.assertTrue(publisher.getMagazines().contains(new Magazine(2, "National Trust Magazine",
                40, null)));
    }

    @Test
    void deleteShouldRemoveEntryFromDb() {
        publisherDao.delete(4);
        publisherDao.delete(5);

        List<Publisher> publishers = publisherDao.findAll();

        Assertions.assertEquals(3, publishers.size());
    }
}
