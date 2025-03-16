package entity;

import java.util.Objects;
import java.util.Set;

/**
 * Publisher entity
 * <p>
 * Relation:
 * <p>
 * One to Many - Publisher to Magazine
 */
public class Publisher {
    private Integer id;
    private String name;
    private Set<Magazine> magazines;

    public Publisher() {
    }

    public Publisher(Integer id, String name, Set<Magazine> magazines) {
        this.id = id;
        this.name = name;
        this.magazines = magazines;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Magazine> getMagazines() {
        return magazines;
    }

    public void setMagazines(Set<Magazine> magazines) {
        this.magazines = magazines;
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", magazines=" + magazines +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Publisher publisher = (Publisher) o;
        return Objects.equals(id, publisher.id)
                && Objects.equals(name, publisher.name)
                && Objects.equals(magazines, publisher.magazines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, magazines);
    }
}
