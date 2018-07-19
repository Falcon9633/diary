package ua.com.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"studentsList", "subjectList", "scheduleList"})
public class Band {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //    @Column(unique = true)
    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "band")
    @JsonIgnoreProperties("band")
    @OrderBy("name ASC")
    private Set<Student> studentsList = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "band_subject",
            joinColumns = @JoinColumn(name = "band_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    @JsonIgnoreProperties({"bandList", "teacherList", "scheduleList"})
    @OrderBy("name ASC")
    private Set<Subject> subjectList = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "band")
    @JsonIgnoreProperties({"band", "subject", "teacher"})
    private Set<Schedule> scheduleList = new HashSet<>();

//    public void addStudent(Student student) {
//        addStudent(student, true);
//    }
//
//    void addStudent(Student student, boolean set){
//        if (student != null){
//            getStudentsList().add(student);
//            if (set){
//                student.setBand(this, false);
//            }
//        }
//    }
//
//    public void removeStudent(Student student){
//        getStudentsList().remove(student);
//        student.setBand(null);
//    }
}
