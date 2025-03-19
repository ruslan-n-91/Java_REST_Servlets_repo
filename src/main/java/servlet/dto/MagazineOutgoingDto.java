package servlet.dto;

import java.util.Objects;

public class MagazineOutgoingDto {
    private Integer id;
    private String title;
    private Integer quantity;
    private PublisherOutgoingDto publisher;

    public MagazineOutgoingDto() {
    }

    public MagazineOutgoingDto(Integer id, String title, Integer quantity, PublisherOutgoingDto publisher) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.publisher = publisher;
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

    public PublisherOutgoingDto getPublisher() {
        return publisher;
    }

    @Override
    public String toString() {
        return "MagazineOutgoingDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", quantity=" + quantity +
                ", publisher=" + publisher +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MagazineOutgoingDto that = (MagazineOutgoingDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(title, that.title)
                && Objects.equals(publisher, that.publisher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, publisher);
    }
}
