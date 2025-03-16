package entity;

import java.util.Objects;
import java.util.Set;

public class Book {
    private Integer id;
    private String title;
    private Integer quantity;
    private Set<Author> authors;

    public Book() {
    }

    public Book(Integer id, String title, Integer quantity, Set<Author> authors) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.authors = authors;
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

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", quantity=" + quantity +
                ", authors=" + authors +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id)
                && Objects.equals(title, book.title)
                && Objects.equals(quantity, book.quantity)
                && Objects.equals(authors, book.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, quantity, authors);
    }
}
