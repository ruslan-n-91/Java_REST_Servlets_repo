package service.impl;

import dao.MagazineDao;
import dao.impl.MagazineDaoImpl;
import entity.Magazine;
import service.MagazineService;
import servlet.dto.MagazineIncomingDto;
import servlet.dto.MagazineOutgoingDto;
import servlet.mapper.MagazineDtoMapper;
import servlet.mapper.impl.MagazineDtoMapperImpl;

import java.util.List;

public class MagazineServiceImpl implements MagazineService {
    private MagazineDao magazineDao = new MagazineDaoImpl();
    private MagazineDtoMapper magazineDtoMapper = new MagazineDtoMapperImpl();

    @Override
    public List<MagazineOutgoingDto> findAll() {
        List<Magazine> listOfMagazines = magazineDao.findAll();
        return magazineDtoMapper.mapToListOfMagazineOutgoingDtos(listOfMagazines);
    }

    @Override
    public MagazineOutgoingDto findById(Integer id) {
        Magazine magazine = magazineDao.findById(id);
        return magazineDtoMapper.mapToMagazineOutgoingDto(magazine);
    }

    @Override
    public void save(MagazineIncomingDto magazineIncomingDto) {
        magazineDao.save(magazineDtoMapper.mapToMagazine(magazineIncomingDto));
    }

    @Override
    public void update(MagazineIncomingDto magazineIncomingDto) {
        magazineDao.update(magazineDtoMapper.mapToMagazine(magazineIncomingDto));
    }

    @Override
    public void delete(Integer id) {
        magazineDao.delete(id);
    }
}
