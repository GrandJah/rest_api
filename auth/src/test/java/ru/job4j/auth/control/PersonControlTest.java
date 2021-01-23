package ru.job4j.auth.control;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.job4j.auth.AuthApplication;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.PersonRepository;

@SpringBootTest(classes = AuthApplication.class)
@AutoConfigureMockMvc
class PersonControlTest {

  @MockBean
  private PersonRepository persons;

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void whenGetPersonThenOkAndReturnAllPerson() throws Exception {
    this.mockMvc.perform(get("/person/"))
                .andDo(print())
                .andExpect(status().isOk());
    verify(persons).findAll();
  }

  @Test
  public void whenGetPersonIdThenReturnPerson() throws Exception {
    when(persons.findById(any())).thenReturn(java.util.Optional.of(new Person()));
    this.mockMvc.perform(get("/person/123"))
                .andDo(print())
                .andExpect(status().isOk());
    ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
    verify(persons).findById(argument.capture());
    assertThat(argument.getValue(), is(123));
  }

  @Test
  public void whenPostPersonThenSavePerson() throws Exception {
    ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
    when(persons.save(argument.capture())).thenReturn(new Person());
    this.mockMvc.perform(post("/person/").contentType(MediaType.APPLICATION_JSON)
                                         .content("{\"login\":\"login\",\"password\":\"secret\"}"))
                .andDo(print())
                .andExpect(status().isCreated());
    verify(persons).save(argument.capture());
    assertThat(argument.getValue()
                       .getId(), nullValue());
    assertThat(argument.getValue()
                       .getLogin(), is("login"));
    assertThat(argument.getValue()
                       .getPassword(), is("secret"));
  }

  @Test
  public void whenPutPersonThenSaveIdPerson() throws Exception {
    ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
    when(persons.save(argument.capture())).thenReturn(new Person());
    this.mockMvc.perform(put("/person/").contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                          "{\"id\":117,\"login\":\"login\",\"password\":\"secret\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    verify(persons).save(argument.capture());
    assertThat(argument.getValue()
                       .getId(), is(117));
    assertThat(argument.getValue()
                       .getLogin(), is("login"));
    assertThat(argument.getValue()
                       .getPassword(), is("secret"));
  }

  @Test
  public void whenDeletePersonIdThenDeletePerson() throws Exception {
    this.mockMvc.perform(delete("/person/17"))
                .andDo(print())
                .andExpect(status().isOk());
    ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
    verify(persons).delete(argument.capture());
    assertThat(argument.getValue()
                       .getId(), is(17));
  }
}
