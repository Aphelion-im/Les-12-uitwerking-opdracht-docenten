package com.example.les12.controller;

import com.example.les12.dto.TeacherDto;
import com.example.les12.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    // Oude manier:
    // @Autowired
    // The @Autowired annotation is performing Dependency Injection.
    // https://stackoverflow.com/questions/18072917/what-is-the-use-of-autowired-annotation-in-spring-what-is-meant-by-the-term-aut
    // private TeacherRepository repos;

    // Werk in controller niet met entity class, maar met Dto class.
    // Service gekoppeld aan Repository.   Controller <-> Service <-> Repository
    // Controller gekoppeld aan Service
    // Model overgenomen door Dto
    private final TeacherService teacherService;

    // Construction injection i.p.v. @Autowired
    public TeacherController(TeacherService service) {
        this.teacherService = service;
    }

// Oude manier les 11-:
//    @GetMapping
//    public ResponseEntity<Iterable<Teacher>> getTeachers() {
//        return ResponseEntity.ok(repos.findAll());
//    }

    // Nieuwe methode met service.
    // Uit Repo: https://github.com/robertjanelias/les12opdracht/blob/main/src/main/java/com/example/les11model/controller/TeacherController.java
    @GetMapping
    public ResponseEntity<List<TeacherDto>> getAllTeachers() {
        return ResponseEntity.ok().body(teacherService.getTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getTeacher(@PathVariable Long id) {

        TeacherDto teacherDto = teacherService.getTeacher(id); // getTeacher service heeft geen Optional

        return ResponseEntity.ok().body(teacherDto);
    }


    // Robert-Jan heeft alleen createTeacher method herschreven naar de nieuwe manier van les 12.
    // Oude code uit les 11: Docenten workshop
    // Oude werkt met direct met een Teacher model. Nieuw met: TeacherDto.

    //    @PostMapping
    //    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher t) {
    //        repos.save(t);
    //        URI uri = URI.create(ServletUriComponentsBuilder
    //                .fromCurrentRequest().path("/" + t.getId()).toUriString());
    //        return ResponseEntity.created(uri).body(t);
    //    }

    // @Valid notatie? JSR-303’s @Valid annotation for method level validation.
    // BindingResult? BindingResult holds the result of a validation and binding and contains errors that may have occurred. The BindingResult must come right after the model object that is validated or else Spring fails to validate the object and throws an exception.
    // https://zetcode.com/spring/bindingresult/
    // Zo te zien is de controller 'dom' gehouden en heeft alleen statements die te maken hebben met user feedback, zoals errors, URI.
    // Functies om te verwerken staan nu in Service.
    // Zo blijven de binnenkomende requests van de controller laag gescheiden van de 'intelligente' Service-laag
    // https://stackoverflow.com/questions/3595160/what-does-the-valid-annotation-indicate-in-spring
    @PostMapping
    public ResponseEntity<Object> createTeacher(@Valid @RequestBody TeacherDto teacherDto, BindingResult br) {
        // Zorgt er voor dat na validatie met @Valid en validatie in TeacherDto er nette
        // user feedback wordt gegeven (In Postman)
        if (br.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fe : br.getFieldErrors()) {
                sb.append(fe.getField());
                sb.append(": ");
                sb.append(fe.getDefaultMessage());
                sb.append("\n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
            // return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
        } else {
            // Onderstaande ipv repos.save(t);
            Long id = teacherService.createTeacher(teacherDto); // Daarom moet createTeacher in TeachService een Long returnen
            // teacherDto = teacherService.createTeacher(teacherDto); Geeft in Postman: com.example.les12.dto.TeacherDto@2a61bf3f
            // teacherDto.id = id;
            // Meegeven met elke @PostMapping in verband met REST-richtlijnen
            // Te zien in Postman > Headers > Location > value
            URI uri = URI.create(ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/" + id).toUriString());

            return ResponseEntity.created(uri).body("Teacher added with id: " + id);
        }
    }


//    @GetMapping("/before")
//    public ResponseEntity<Iterable<Teacher>> getTeachersBefore(@RequestParam LocalDate date) {
//        return ResponseEntity.ok(repos.findByDobBefore(date));
//    }
}
