package ua.com.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "band")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "band_student",
            inverseJoinColumns = @JoinColumn(name = "band_id"),
            joinColumns = @JoinColumn(name = "student_id"))
    @JsonIgnoreProperties({"studentsList", "subjectList", "scheduleList"})
    private Band band;


}
