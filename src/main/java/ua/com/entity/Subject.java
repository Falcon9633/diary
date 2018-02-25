package ua.com.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "group_subject",joinColumns =@JoinColumn(name="subject_id"),
            inverseJoinColumns = @JoinColumn(name="group_id"))
    private List<GroupStudent> groupStudents =new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "teacher_subject",joinColumns =@JoinColumn(name="subject_id"),
            inverseJoinColumns = @JoinColumn(name="teacher_id"))
    private List<Teacher> teachers =new ArrayList<>();

}
