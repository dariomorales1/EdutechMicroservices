package cl.edutech.courseservice.service;

import cl.edutech.courseservice.model.Course;
import cl.edutech.courseservice.repository.CourseRepository;
import cl.edutech.courseservice.service.CourseService;
import cl.edutech.courseservice.exception.NotFoundException;
import cl.edutech.courseservice.exception.ConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock private CourseRepository courseRepository;
    @InjectMocks private CourseService courseService;

    @BeforeEach void init() { MockitoAnnotations.openMocks(this); }

    @Test void whenFindAll_thenReturnsList() {
        when(courseRepository.findAll()).thenReturn(List.of(new Course("1","N","T","D","I",100)));
        var list = courseService.findAll();
        assertFalse(list.isEmpty());
    }

    @Test void whenFindByIdExists_thenReturn() {
        Course c = new Course("2","N","T","D","I",200);
        when(courseRepository.findById("2")).thenReturn(Optional.of(c));
        var res = courseService.findById("2");
        assertEquals("2", res.getCourseId());
    }

    @Test void whenFindByIdNotExists_thenThrowNotFound() {
        when(courseRepository.findById("3")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> courseService.findById("3"));
    }

    @Test void whenCreateExists_thenThrowConflict() {
        Course c = new Course("4","N","T","D","I",400);
        when(courseRepository.existsById("4")).thenReturn(true);
        assertThrows(ConflictException.class, () -> courseService.create(c));
    }

    @Test void whenCreateValid_thenSave() {
        Course c = new Course("5","N","T","D","I",500);
        when(courseRepository.existsById("5")).thenReturn(false);
        when(courseRepository.save(c)).thenReturn(c);
        var res = courseService.create(c);
        assertEquals("5", res.getCourseId());
    }

    @Test void whenUpdateNotFound_thenThrowNotFound() {
        when(courseRepository.findById("6")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> courseService.update("6", new Course()));
    }

    @Test void whenPartialUpdateValid_thenSaveUpdated() {
        Course ex = new Course("7","Old","T","D","I",700);
        Course req = new Course(null,"New","NT","ND","NI",null);
        when(courseRepository.findById("7")).thenReturn(Optional.of(ex));
        courseService.partialUpdate("7", req);
        assertEquals("New", ex.getNameCourse());
    }

    @Test void whenRemoveNotExists_thenThrowNotFound() {
        when(courseRepository.existsById("8")).thenReturn(false);
        assertThrows(NotFoundException.class, () -> courseService.remove("8"));
    }

    @Test void whenRemoveExists_thenDeleteByIdCalled() {
        when(courseRepository.existsById("9")).thenReturn(true);
        courseService.remove("9");
        verify(courseRepository).deleteById("9");
    }
}