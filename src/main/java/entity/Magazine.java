package entity;

import java.util.Objects;

/**
 * Magazine entity
 * <p>
 * Relation:
 * <p>
 * Many to One - Magazine to Publisher
 */
public class Magazine {
    private Integer id;
    private String title;
    private Integer quantity;
    private Publisher publisher;

    public Magazine() {
    }

    public Magazine(Integer id, String title, Integer quantity, Publisher publisher) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.publisher = publisher;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Magazine{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", quantity=" + quantity +
                ", publisher=" + publisher +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Magazine magazine = (Magazine) o;
        return Objects.equals(id, magazine.id)
                && Objects.equals(title, magazine.title)
                && Objects.equals(publisher, magazine.publisher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, publisher);
    }
}
