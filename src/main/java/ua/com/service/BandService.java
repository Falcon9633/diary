package ua.com.service;

import org.springframework.data.domain.Sort;
import ua.com.entity.Band;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BandService {

    void save(Band band);

    void saveSubjectToBand(int bandId, Map<String, String> requestParam);

    Band findOne(int id);

    Band findByIdWithStudents(int id);

    Band findByIdWithSubject(int id);

    List<Band> findAll();

    List<Band> findAll(Sort.Direction sortDirection, String property);

    Set<Band> findAllWithAllNested();
}
