package ru.job4j.auth.control;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.model.Report;

@RestController
@RequestMapping("/report")
public class ReportsControl {
  private static final String API = "http://localhost:8080/person/";
  private static final String API_ID = "http://localhost:8080/person/{id}";
  private final RestTemplate rest;

  public ReportsControl(RestTemplate rest) {
    this.rest = rest;
  }

  @GetMapping("/")
  public List<Report> findAll() {
    List<Report> rsl = new ArrayList<>();
    List<Person> persons = rest.exchange(API, HttpMethod.GET, null,
      new ParameterizedTypeReference<List<Person>>() {
      })
                               .getBody();
    for (Person person : persons) {
      Report report = Report.of(1, "First", person);
      rsl.add(report);
    }
    return rsl;
  }

  @PostMapping("/")
  public ResponseEntity<Person> create(@RequestBody Person person) {
    Person rsl = rest.postForObject(API, person, Person.class);
    return new ResponseEntity<>(rsl, HttpStatus.CREATED);
  }

  @PutMapping("/")
  public ResponseEntity<Void> update(@RequestBody Person person) {
    rest.put(API, person);
    return ResponseEntity.ok()
                         .build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    rest.delete(API_ID, id);
    return ResponseEntity.ok()
                         .build();
  }
}
