package com.shams.practice.spring.hateoas.entity;

import com.shams.practice.spring.hateoas.controller.PersonController;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Entity
public class Person extends RepresentationModel<Person> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;

   public Person() {
   }

   public Person(String firstName, String lastName) {
       this.firstName = firstName;
       this.lastName = lastName;
   }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Person addSelfLink() {
       return this.add(
           linkTo(
               methodOn(PersonController.class).getPerson(this.getId())
           ).withSelfRel().withType("GET")
       );
    }

    public Person addUpdateLink() {
       return this.add(
           linkTo(
               methodOn(PersonController.class).updatePerson(this, this.getId())
           ).withRel("update").withType("PUT")
       );
    }

    public Person addDeleteLink() {
       return this.add(
           linkTo(
               methodOn(PersonController.class).deletePerson(this.getId())
           ).withRel("delete").withType("DELETE")
       );
    }

    public Person addAllLinks() {
       return this
               .addSelfLink()
               .addUpdateLink()
               .addDeleteLink();
    }
}
