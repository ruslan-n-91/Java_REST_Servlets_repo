package servlet.dto;

import java.util.Objects;
import java.util.Set;

public class PublisherIncomingDto {
    private Integer id;
    private String name;
    private Set<MagazineIncomingDto> magazines;

    public PublisherIncomingDto() {
    }

    public PublisherIncomingDto(Integer id, String name, Set<MagazineIncomingDto> magazines) {
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

    public Set<MagazineIncomingDto> getMagazines() {
        return magazines;
    }

    @Override
    public String toString() {
        return "PublisherIncomingDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", magazines=" + magazines +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublisherIncomingDto that = (PublisherIncomingDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(magazines, that.magazines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, magazines);
    }
}
