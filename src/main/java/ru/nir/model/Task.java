package ru.nir.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    /*@Id
    @GeneratedValue*/
    private Long id;
    private String name;
    private String description;
}
