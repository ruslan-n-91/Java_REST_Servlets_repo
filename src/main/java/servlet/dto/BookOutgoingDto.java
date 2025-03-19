package servlet.dto;

import java.util.Objects;
import java.util.Set;

public class BookOutgoingDto {
    private Integer id;
    private String title;
    private Integer quantity;
    private Set<AuthorOutgoingDto> authors;

    public BookOutgoingDto() {
    }

    public BookOutgoingDto(Integer id, String title, Integer quantity, Set<AuthorOutgoingDto> authors) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.authors = authors;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Set<AuthorOutgoingDto> getAuthors() {
        return authors;
    }

    @Override
    public String toString() {
        return "BookOutgoingDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", quantity=" + quantity +
                ", authors=" + authors +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookOutgoingDto that = (BookOutgoingDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(title, that.title)
                && Objects.equals(authors, that.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, authors);
    }
}
