package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ua.com.entity.Authority;
import ua.com.entity.Student;
import ua.com.service.StudentService;
import ua.com.service.TeacherService;

import java.util.HashSet;
import java.util.Set;


public class DiaryUserDetailsService implements UserDetailsService {

    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    public DiaryUserDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails student = studentService.findByEmail(username);
        UserDetails teacher = teacherService.findByEmail(username);

        if (student == null && teacher == null) {
            throw new UsernameNotFoundException("User not found by name: " + username);
        }

        if (student != null) {
            return toUserDetails(student);
        }

        return toUserDetails(teacher);
    }

    public UserDetails toUserDetails(UserDetails userDetails){
        return new org.springframework.security.core.userdetails.User(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }
}