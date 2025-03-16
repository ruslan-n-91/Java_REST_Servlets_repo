package servlet.dto;

import java.util.Objects;
import java.util.Set;

public class AuthorOutgoingDto {
    private Integer id;
    private String name;
    private Set<BookOutgoingDto> books;

    public AuthorOutgoingDto() {
    }

    public AuthorOutgoingDto(Integer id, String name, Set<BookOutgoingDto> books) {
        this.id = id;
        this.name = name;
        this.books = books;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<BookOutgoingDto> getBooks() {
        return books;
    }

    @Override
    public String toString() {
        return "AuthorOutgoingDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthorOutgoingDto that = (AuthorOutgoingDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(books, that.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, books);
    }
}
