package com.example.les12.service;

import com.example.les12.dto.TeacherDto;
import com.example.les12.exception.DuplicateResourceException;
import com.example.les12.exception.ResourceNotFoundException;
import com.example.les12.model.Teacher;
import com.example.les12.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherService {

    // Service gekoppeld aan Repository.   Controller <-> Service <-> Repository
    // Controller gekoppeld aan Service
    // Model overgenomen door Dto
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    // Moet eigenlijk met een Optional
    public TeacherDto getTeacher(Long id) {
        Teacher t = teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.id = t.getId();
        teacherDto.firstName = t.getFirstName();
        teacherDto.lastName = t.getLastName();
        teacherDto.dob = t.getDob();

        return teacherDto;
    }

    // Repos zit nu niet meer in de Controllers, maar in de Services
    // Geen validatie beschikbaar (Dubbele namen of andere validatie)
    public Long createTeacher(TeacherDto teacherDto) {
        if (teacherRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(teacherDto.firstName, teacherDto.lastName)) {
            throw new DuplicateResourceException("This resource already exists!");
        }

        Teacher teacher = new Teacher();
        teacher.setFirstName(teacherDto.firstName);
        teacher.setLastName(teacherDto.lastName);
        teacher.setDob(teacherDto.dob);
        teacherRepository.save(teacher);
        return teacher.getId();
//        teacherDto.id = teacher.getId();
//        return teacherDto; Geeft com.example.les12.dto.TeacherDto@2a61bf3f in Postman


    }


    // Uit repo: https://github.com/robertjanelias/les12opdracht/blob/main/src/main/java/com/example/les11model/service/TeacherService.java
    public List<TeacherDto> getTeachers() {
        List<Teacher> teachers = teacherRepository.findAll(); // Er stond, met foutmelding: List<Teacher> teachers = repos.findAll();
        List<TeacherDto> teacherDtos = new ArrayList<>();
        for (Teacher t : teachers) {
            TeacherDto tdto = new TeacherDto();
            tdto.id = t.getId();
            tdto.dob = t.getDob();
            tdto.firstName = t.getFirstName();
            tdto.lastName = t.getLastName();
            teacherDtos.add(tdto);
        }
        return teacherDtos;
    }


}
