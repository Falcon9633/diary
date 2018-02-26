package ua.com.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<Band> bandList =new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "teacher_subject",joinColumns =@JoinColumn(name="subject_id"),
            inverseJoinColumns = @JoinColumn(name="teacher_id"))
    private List<Teacher> teacherList =new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subject")
    private List<Schedule> scheduleList = new ArrayList<>();

}
