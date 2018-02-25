package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.TableDAO;
import ua.com.entity.TTable;
import ua.com.service.TTableService;

@Service
@Transactional
public class TTableServiceImpl implements TTableService {

    @Autowired
    private TableDAO tableDAO;

    @Override
    public void save(TTable TTable) {
        tableDAO.save(TTable);
    }
}
