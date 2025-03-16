package servlet.dto;

import java.util.Objects;
import java.util.Set;

public class PublisherOutgoingDto {
    private Integer id;
    private String name;
    private Set<MagazineOutgoingDto> magazines;

    public PublisherOutgoingDto() {
    }

    public PublisherOutgoingDto(Integer id, String name, Set<MagazineOutgoingDto> magazines) {
        this.id = id;
        this.name = name;
        this.magazines = magazines;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<MagazineOutgoingDto> getMagazines() {
        return magazines;
    }

    @Override
    public String toString() {
        return "PublisherOutgoingDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", magazines=" + magazines +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublisherOutgoingDto that = (PublisherOutgoingDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(magazines, that.magazines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, magazines);
    }
}
