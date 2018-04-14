package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.BandDAO;
import ua.com.entity.Band;
import ua.com.service.BandService;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class BandServiceImpl implements BandService {

    @Autowired
    private BandDAO bandDAO;
    @Override
    public void save(Band band) {
        bandDAO.save(band);
    }

    @Override
    public Band findOne(int id) {
        return bandDAO.findOne(id);
    }

    @Override
    public Band findByIdWithSubject(int id) {
        return bandDAO.findByIdWithSubject(id);
    }

    @Override
    public List<Band> findAll() {
        return bandDAO.findAll();
    }

    @Override
    public List<Band> findAll(Sort sort) {
        return bandDAO.findAll(sort);
    }

    @Override
    public List<Band> findAllWithAllNested() {
        return bandDAO.findAllWithAllNested();
    }


}
