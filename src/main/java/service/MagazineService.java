package service;

import servlet.dto.MagazineIncomingDto;
import servlet.dto.MagazineOutgoingDto;

import java.util.List;

public interface MagazineService {
    List<MagazineOutgoingDto> findAll();

    MagazineOutgoingDto findById(Integer id);

    void save(MagazineIncomingDto magazineIncomingDto);

    void update(MagazineIncomingDto magazineIncomingDto);

    void delete(Integer id);
}
