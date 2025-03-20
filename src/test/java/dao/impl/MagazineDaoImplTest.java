package dao.impl;

import dao.MagazineDao;
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

import java.util.List;

@Testcontainers
class MagazineDaoImplTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    ).withInitScript("sql/sql_script.sql");

    static MagazineDao magazineDao;

    @BeforeAll
    static void setUp() {
        ConnectionManager connectionProvider = new ConnectionManagerImpl(
                postgres.getDriverClassName(),
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        magazineDao = new MagazineDaoImpl(connectionProvider);
    }

    @Test
    void findAllShouldReturnAllEntriesFromDb() {
        List<Magazine> magazines = magazineDao.findAll();

        Assertions.assertEquals(8, magazines.size());
        Assertions.assertEquals("National Geographic", magazines.get(0).getTitle());
        Assertions.assertEquals("National Trust Magazine", magazines.get(1).getTitle());
    }

    @Test
    void findByIdShouldReturnEntryWithSpecifiedIdFromDb() {
        Magazine magazine = magazineDao.findById(1);
        Publisher publisher = new Publisher(1, "National Geographic Society", null);

        Assertions.assertEquals(1, magazine.getId());
        Assertions.assertEquals("National Geographic", magazine.getTitle());
        Assertions.assertEquals(magazine.getPublisher(), publisher);
    }

    @Test
    void saveShouldCreateNewEntryInDb() {
        Magazine newMagazine1 = new Magazine(null, "Magazine 5555", 10, null);

        Publisher publisher = new Publisher(1, null, null);
        Magazine newMagazine2 = new Magazine(null, "Magazine 9999", 15, publisher);

        magazineDao.save(newMagazine1);
        magazineDao.save(newMagazine2);

        Magazine magazine1 = magazineDao.findById(7);
        Magazine magazine2 = magazineDao.findById(8);

        Assertions.assertEquals(7, magazine1.getId());
        Assertions.assertEquals("Magazine 5555", magazine1.getTitle());
        Assertions.assertEquals(10, magazine1.getQuantity());
        Assertions.assertEquals(new Publisher(null, null, null), magazine1.getPublisher());

        Assertions.assertEquals(8, magazine2.getId());
        Assertions.assertEquals("Magazine 9999", magazine2.getTitle());
        Assertions.assertEquals(15, magazine2.getQuantity());
        Assertions.assertEquals(new Publisher(1, "National Geographic Society", null),
                magazine2.getPublisher());
    }

    @Test
    void updateShouldChangeEntryInDb() {
        Magazine newMagazine = new Magazine(6, "Magazine 3333", 25,
                new Publisher(1, null, null));

        magazineDao.update(newMagazine);

        Magazine magazine = magazineDao.findById(6);

        Assertions.assertEquals(6, magazine.getId());
        Assertions.assertEquals("Magazine 3333", magazine.getTitle());
        Assertions.assertEquals(25, magazine.getQuantity());
        Assertions.assertEquals(new Publisher(1, "National Geographic Society", null),
                magazine.getPublisher());
    }

    @Test
    void deleteShouldRemoveEntryFromDb() {
        magazineDao.delete(7);
        magazineDao.delete(8);

        List<Magazine> magazines = magazineDao.findAll();

        Assertions.assertEquals(6, magazines.size());
    }
}
