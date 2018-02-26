package ua.com.service;

import ua.com.entity.Band;

import java.util.List;

public interface BandService {
    void save(Band band);
    Band findOne(int id);
    List<Band> findAll();
}
