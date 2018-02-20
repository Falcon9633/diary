package ua.com.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "studentsList")
public class GroupStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//    @Column(unique = true)
    private String nameOfGroup;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy="groupStudent")
    private List<Student> studentsList=new ArrayList<>();


    public GroupStudent(String nameOfGroup) {
        this.nameOfGroup = nameOfGroup;
    }
}
