package ua.com.service;

import org.springframework.data.domain.Sort;
import ua.com.entity.Band;

import java.util.List;
import java.util.Set;

public interface BandService {

    void save(Band band);

    Band findOne(int id);

    Band findByIdWithStudents(int id);

    Band findByIdWithSubject(int id);

    List<Band> findAll();

    List<Band> findAll(Sort sort);

    Set<Band> findAllWithAllNested();
}
