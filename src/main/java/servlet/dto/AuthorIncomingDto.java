package servlet.dto;

import java.util.Objects;
import java.util.Set;

public class AuthorIncomingDto {
    private Integer id;
    private String name;
    private Set<BookIncomingDto> books;

    public AuthorIncomingDto() {
    }

    public AuthorIncomingDto(Integer id, String name, Set<BookIncomingDto> books) {
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

    public Set<BookIncomingDto> getBooks() {
        return books;
    }

    @Override
    public String toString() {
        return "AuthorIncomingDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthorIncomingDto that = (AuthorIncomingDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(books, that.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, books);
    }
}
