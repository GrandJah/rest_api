package ru.job4j.auth.control;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.PersonRepository;

@RestController
@RequestMapping("/person")
public class PersonControl {

  private final PersonRepository persons;

  public PersonControl(final PersonRepository persons) {
    this.persons = persons;
  }

  @GetMapping("/")
  public List<Person> findAll() {
    System.out.println("get");
    return StreamSupport.stream(this.persons.findAll()
                                            .spliterator(), false)
                        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Person> findById(@PathVariable int id) {
    Optional<Person> person = this.persons.findById(id);
    return new ResponseEntity<>(person.orElse(new Person()),
      person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
  }

  @PostMapping("/")
  public ResponseEntity<Person> create(@RequestBody Person person) {
    return new ResponseEntity<>(this.persons.save(person), HttpStatus.CREATED);
  }

  @PutMapping("/")
  public ResponseEntity<Void> update(@RequestBody Person person) {
    this.persons.save(person);
    return ResponseEntity.ok()
                         .build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    Person person = new Person();
    person.setId(id);
    this.persons.delete(person);
    return ResponseEntity.ok()
                         .build();
  }
}
