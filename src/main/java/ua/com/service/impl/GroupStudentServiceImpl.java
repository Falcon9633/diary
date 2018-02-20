package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.GroupStudentDAO;
import ua.com.entity.GroupStudent;
import ua.com.service.GroupStudentService;

import java.util.List;

@Service
@Transactional
public class GroupStudentServiceImpl implements GroupStudentService {

    @Autowired
    private GroupStudentDAO groupStudentDAO;
    @Override
    public void save(GroupStudent groupStudent) {
        groupStudentDAO.save(groupStudent);
    }

    @Override
    public GroupStudent findOne(int id) {
        return groupStudentDAO.findOne(id);
    }

    @Override
    public List<GroupStudent> findAll() {
        return groupStudentDAO.findAll();
    }
}
