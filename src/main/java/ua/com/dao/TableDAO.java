package ua.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.entity.TTable;

public interface TableDAO extends JpaRepository<TTable, Integer> {
}
