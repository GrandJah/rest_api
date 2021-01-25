package ru.job4j.auth.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String firstName;

  private String lastName;

  private String INN;

  @OneToMany
  List<Person> accounts = new ArrayList<>();

  public List<Integer> getAccIds() {
    List<Integer> ids = new ArrayList<>();
    for (Person person : accounts) {
      ids.add(person.getId());
    }
    return ids;
  }
}
