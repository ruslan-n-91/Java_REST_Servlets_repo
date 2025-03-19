package servlet.mapper;

import entity.Author;
import servlet.dto.AuthorIncomingDto;
import servlet.dto.AuthorOutgoingDto;

import java.util.List;

public interface AuthorDtoMapper {
    Author mapToAuthor(AuthorIncomingDto authorIncomingDto);

    AuthorOutgoingDto mapToAuthorOutgoingDto(Author author);

    List<AuthorOutgoingDto> mapToListOfAuthorOutgoingDtos(List<Author> listOfAuthors);
}
