package com.openclassrooms.etudiant.service;

import com.openclassrooms.etudiant.entities.Student;
import com.openclassrooms.etudiant.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    // TEST 1 — findAll() retourne la liste des étudiants
    @Test
    void test_findAll_returns_student_list() {
        // GIVEN
        Student s1 = new Student();
        s1.setId(1L);
        s1.setFirstName("John");

        Student s2 = new Student();
        s2.setId(2L);
        s2.setFirstName("Jane");

        when(studentRepository.findAll()).thenReturn(List.of(s1, s2));

        // WHEN
        List<Student> result = studentService.findAll();

        // THEN
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(s1, s2);
    }

    // TEST 2 — save() sauvegarde un étudiant
    @Test
    void test_save_calls_repository_save() {
        // GIVEN
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("John");

        when(studentRepository.save(student)).thenReturn(student);

        // WHEN
        Student result = studentService.save(student);

        // THEN
        assertThat(result).isEqualTo(student);
        verify(studentRepository, times(1)).save(student);
    }

    // TEST 3 — delete() appelle deleteById()
    @Test
    void test_delete_calls_repository_deleteById() {
        // GIVEN
        Long id = 10L;

        // WHEN
        studentService.delete(id);

        // THEN
        verify(studentRepository, times(1)).deleteById(id);
    }
}
