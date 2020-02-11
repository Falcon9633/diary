package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.BandDAO;
import ua.com.dao.SubjectDAO;
import ua.com.entity.Band;
import ua.com.entity.Subject;
import ua.com.service.BandService;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class BandServiceImpl implements BandService {

    @Autowired
    private BandDAO bandDAO;

    @Autowired
    private SubjectDAO subjectDAO;

    @Override
    public void save(Band band) {
        bandDAO.save(band);
    }

    @Override
    public void saveSubjectToBand(int bandId, Map<String, String> requestParam) {
        Band selectedBand = bandDAO.findByIdWithSubject(bandId);
        Set<Subject> subjectSet = selectedBand.getSubjectSet();

        for (String key : requestParam.keySet()) {
            if (key.contains("subjectId-")) {
                Subject subject = subjectDAO.findOne(Integer.parseInt(requestParam.get(key)));
                subjectSet.add(subject);
            }
        }
        selectedBand.setSubjectSet(subjectSet);
        bandDAO.save(selectedBand);
    }

    @Override
    public Band findOne(int id) {
        return bandDAO.findOne(id);
    }

    @Override
    public Band findByIdWithStudents(int id) {
        return bandDAO.findByIdWithStudents(id);
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
    public List<Band> findAll(Sort.Direction sortDirection, String property) {
        Sort.Order byProperty = new Sort.Order(sortDirection, property);
        Sort orders = new Sort(byProperty);
        return bandDAO.findAll(orders);
    }

    @Override
    public Set<Band> findAllWithAllNested() {
        return bandDAO.findAllWithAllNested();
    }
}
