package com.example.readiscache.controller;

import com.example.readiscache.entity.Person;
import com.example.readiscache.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user")
public class PersonController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final PersonRepository personRepository;

    @Autowired
    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Cacheable(value = "users", key = "#userId", unless = "#result.followers < 12000")
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public Person getUser(@PathVariable String userId) {
        LOG.info("Getting user with ID {}.", userId);
        Optional<Person> optionalPerson = personRepository.findById(Long.valueOf(userId));
        if (optionalPerson.isEmpty()) {
            return new Person();
        } else {
            return optionalPerson.get();
        }
    }

    @GetMapping
    public List<Person> getAllPerson() {
        LOG.info("Getting all user");
        List<Person> personList = personRepository.findAll();
        return personList;
    }

    @PostMapping(value = "/save")
    public ResponseEntity<Person> savePerson(@RequestBody Person person) {
        LOG.info("Saving the person {}", person.getName());
        try {
            personRepository.save(person);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
