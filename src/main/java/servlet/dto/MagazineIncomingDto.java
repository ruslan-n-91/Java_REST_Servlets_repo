package servlet.dto;

import java.util.Objects;

public class MagazineIncomingDto {
    private Integer id;
    private String title;
    private Integer quantity;
    private PublisherIncomingDto publisher;

    public MagazineIncomingDto() {
    }

    public MagazineIncomingDto(Integer id, String title, Integer quantity, PublisherIncomingDto publisher) {
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

    public PublisherIncomingDto getPublisher() {
        return publisher;
    }

    @Override
    public String toString() {
        return "MagazineIncomingDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", quantity=" + quantity +
                ", publisher=" + publisher +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MagazineIncomingDto that = (MagazineIncomingDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(title, that.title)
                && Objects.equals(publisher, that.publisher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, publisher);
    }
}
