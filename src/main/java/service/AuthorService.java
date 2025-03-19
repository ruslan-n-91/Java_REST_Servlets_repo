package service;

import servlet.dto.AuthorIncomingDto;
import servlet.dto.AuthorOutgoingDto;

import java.util.List;

public interface AuthorService {
    List<AuthorOutgoingDto> findAll();

    AuthorOutgoingDto findById(Integer id);

    void save(AuthorIncomingDto authorIncomingDto);

    void update(AuthorIncomingDto authorIncomingDto);

    void delete(Integer id);
}
