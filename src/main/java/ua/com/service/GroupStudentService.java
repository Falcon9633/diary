package ua.com.service;

import ua.com.entity.GroupStudent;

import java.util.List;

public interface GroupStudentService {
    void save(GroupStudent groupStudent);
    GroupStudent findOne(int id);
    List<GroupStudent> findAll();
}
