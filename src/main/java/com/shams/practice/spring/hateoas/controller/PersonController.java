package com.shams.practice.spring.hateoas.controller;

import com.shams.practice.spring.hateoas.entity.Person;
import com.shams.practice.spring.hateoas.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @PostMapping("/insert")
    @ResponseStatus(HttpStatus.OK)
    public List<Person> insertData() {
        List<Person> persons = personRepository.findAll();
        if (persons.isEmpty()) {
            persons.add(new Person("Robert", "Downey"));
            persons.add(new Person("Johnny", "Depp"));
            persons.add(new Person("Chris", "Hemsworth"));
            persons.add(new Person("Bradley", "Cooper"));
            persons.add(new Person("Matt", "Damon"));
            persons.add(new Person("Will", "Smith"));
            persons.add(new Person("Leonardo", "DiCaprio"));
            personRepository.saveAll(persons);
        }
        return persons;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Person> getPersons() {
        return personRepository
                .findAll()
                .stream()
                .map(Person::addAllLinks)
                .collect(Collectors.toList());
    }

    @GetMapping("/{personId}")
    @ResponseStatus(HttpStatus.OK)
    public Person getPerson(@PathVariable Long personId) {
        Person person = personRepository
                .findById(personId)
                .get();
        return person.addAllLinks();
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Person createPerson(@RequestBody Person person) {
        personRepository.save(person);
        return person.addAllLinks();
    }

    @PutMapping("/{personId}")
    @ResponseStatus(HttpStatus.OK)
    public Person updatePerson(@RequestBody Person person, @PathVariable Long personId) {
        Person personToUpdate = personRepository
                .findById(personId)
                .get();
        personToUpdate.setFirstName(person.getFirstName());
        personToUpdate.setLastName(person.getLastName());
        personRepository.save(personToUpdate);
        return personToUpdate
                .addSelfLink()
                .addDeleteLink();
    }

    @DeleteMapping("/{personId}")
    @ResponseStatus(HttpStatus.OK)
    public Person deletePerson(@PathVariable Long personId) {
        Person personToDelete = personRepository
                .findById(personId)
                .get();
        personRepository.delete(personToDelete);
        return null;
    }
}
