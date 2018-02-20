package ua.com.entity;


import lombok.*;

import javax.persistence.*;


@Entity

@NoArgsConstructor
@ToString(exclude = "groupStudent")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private GroupStudent groupStudent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public GroupStudent getGroupStudent() {
        return groupStudent;
    }

    public void setGroupStudent(GroupStudent groupStudent) {
        this.groupStudent = groupStudent;
    }
}
