package ua.com.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude={"bandList", "teacherList"})
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "band_subject",joinColumns =@JoinColumn(name="subject_id"),
            inverseJoinColumns = @JoinColumn(name="band_id"))
    private Set<Band> bandList =new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "teacher_subject",joinColumns =@JoinColumn(name="subject_id"),
            inverseJoinColumns = @JoinColumn(name="teacher_id"))
    private Set<Teacher> teacherList =new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subject")
    private Set<Schedule> scheduleList = new HashSet<>();

}
