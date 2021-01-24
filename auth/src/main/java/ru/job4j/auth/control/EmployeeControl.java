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
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import ru.job4j.auth.model.Employee;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.EmployeeRepository;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeControl {
  private static final String API = "http://localhost:8080/person/";
  private static final String API_ID = "http://localhost:8080/person/{id}";
  private final RestTemplate rest;
  private final EmployeeRepository employeeRepository;

  public EmployeeControl(RestTemplate rest, EmployeeRepository employeeRepository) {
    this.rest = rest;
    this.employeeRepository = employeeRepository;
  }

  @GetMapping("/")
  public ResponseEntity<List<Employee>> findAll() {
    List<Employee> employees = new ArrayList<>();
    for (Employee employee : this.employeeRepository.findAll()) {
      getAccounts(employee);
      employees.add(employee);
    }
    return new ResponseEntity<>(employees, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Employee> findById(@PathVariable int id) {
    Optional<Employee> employee = this.employeeRepository.findById(id);
    if (employee.isPresent()) {
      getAccounts(employee.get());
      return new ResponseEntity<>(employee.get(), HttpStatus.OK);
    }
    return ResponseEntity.notFound()
                         .build();
  }

  private void getAccounts(Employee employee) {
    List<Person> accounts = new ArrayList<>();
    for (Person person : employee.getAccounts()) {
      accounts.add(rest.getForObject(API_ID, Person.class, person.getId()));
    }
    employee.setAccounts(accounts);
  }

  @PostMapping("/")
  public ResponseEntity<Employee> create(@RequestBody Employee employee) {
    List<Person> accounts = employee.getAccounts();
    for (Person person : accounts) {
      Person rsl = rest.postForObject(API, person, Person.class);
      log.info("person : {}", rsl);
      person.setId(rsl.getId());
    }
    this.employeeRepository.save(employee);
    return new ResponseEntity<>(employee, HttpStatus.CREATED);
  }

  @PutMapping("/")
  public ResponseEntity<Void> update(@RequestBody Employee employee) {
    List<Person> accounts = employee.getAccounts();
    for (Person person : accounts) {
      rest.put(API, person);
    }
    this.employeeRepository.save(employee);
    return ResponseEntity.ok()
                         .build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    Optional<Employee> employee = this.employeeRepository.findById(id);
    if (employee.isPresent()) {
      List<Person> accounts = new ArrayList<>(employee.get()
                                                      .getAccounts());
      this.employeeRepository.delete(employee.get());
      for (Person person : accounts) {
        rest.delete(API_ID, person.getId());
      }
    }
    return ResponseEntity.ok()
                         .build();
  }
}
