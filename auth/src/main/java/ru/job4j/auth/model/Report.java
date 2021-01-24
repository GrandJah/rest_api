package ru.job4j.auth.model;

import java.sql.Timestamp;
import java.time.Instant;
import lombok.Data;

@Data
public class Report {
  private int id;

  private String name;

  private Timestamp created;

  private Person person;

  public static Report of(int id, String name, Person person) {
    Report r = new Report();
    r.id = id;
    r.name = name;
    r.person = person;
    r.created = Timestamp.from(Instant.now());
    return r;
  }
}
