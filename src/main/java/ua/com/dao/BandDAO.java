package ua.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.entity.Band;

public interface BandDAO extends JpaRepository<Band,Integer> {
}
