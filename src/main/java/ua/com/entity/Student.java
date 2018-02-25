package ua.com.entity;


import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "groupStudent")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private GroupStudent groupStudent;



}
