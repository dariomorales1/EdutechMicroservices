package cl.edutech.billingservice.service;

import cl.edutech.billingservice.DTO.CourseDTO;
import cl.edutech.billingservice.DTO.UserDTO;
import cl.edutech.billingservice.exception.ConflictException;
import cl.edutech.billingservice.exception.NotFoundException;
import cl.edutech.billingservice.model.Billing;
import cl.edutech.billingservice.repository.BillingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BillingServiceTest {
    @Mock private BillingRepository billingRepository;
    @Mock private WebClient userWebClient;
    @Mock private WebClient courseWebClient;
    @Mock private WebClient enrollmentWebClient;
    @InjectMocks private BillingService billingService;

    private WebClient.RequestHeadersUriSpec<?> uriSpec;
    private WebClient.RequestHeadersSpec<?> requestSpec;
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        requestSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);
        doReturn(uriSpec).when(userWebClient).get();
        doReturn(uriSpec).when(courseWebClient).get();
        doReturn(uriSpec).when(enrollmentWebClient).get();
        doReturn(requestSpec).when(uriSpec).uri(anyString(), any(Object[].class));
        doReturn(responseSpec).when(requestSpec).retrieve();
    }

    @Test
    void whenFindAll_thenReturnList() {
        Billing b = new Billing();
        b.setBillingId(1);
        b.setUserId("u");
        b.setCourseId("c");
        b.setEnrollmentId(10);
        b.setAmount(100);
        when(billingRepository.findAll()).thenReturn(List.of(b));
        var list = billingService.findAll();
        assertFalse(list.isEmpty());
    }

    @Test
    void whenFindByIdNotExists_thenThrowNotFound() {
        when(billingRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> billingService.findById(2));
    }

    @Test
    void whenCreateExists_thenThrowConflict() {
        Billing b = new Billing();
        b.setBillingId(3);
        when(billingRepository.existsById(3)).thenReturn(true);
        assertThrows(ConflictException.class, () -> billingService.create(b));
    }

    @Test
    void whenCreateCourseNotFound_thenThrowNotFound() {
        Billing b = new Billing();
        b.setBillingId(4);
        b.setCourseId("c4");
        when(billingRepository.existsById(4)).thenReturn(false);
        when(responseSpec.bodyToMono(CourseDTO.class)).thenReturn(Mono.empty());
        assertThrows(NotFoundException.class, () -> billingService.create(b));
    }

    @Test
    void whenCreatePriceMissing_thenThrowConflict() {
        Billing b = new Billing();
        b.setBillingId(5);
        b.setCourseId("c5");
        when(billingRepository.existsById(5)).thenReturn(false);
        CourseDTO c = new CourseDTO();
        c.setCourseId("c5");
        // price null by default
        when(responseSpec.bodyToMono(CourseDTO.class)).thenReturn(Mono.just(c));
        assertThrows(ConflictException.class, () -> billingService.create(b));
    }

    @Test
    void whenCreateValid_thenSaveWithAmountIncludingTax() {
        Billing b = new Billing();
        b.setBillingId(6);
        b.setCourseId("c6");
        when(billingRepository.existsById(6)).thenReturn(false);
        CourseDTO c = new CourseDTO();
        c.setCourseId("c6");
        c.setPrice(100);
        when(responseSpec.bodyToMono(CourseDTO.class)).thenReturn(Mono.just(c));
        when(billingRepository.save(any(Billing.class))).thenAnswer(inv -> inv.getArgument(0));
        var res = billingService.create(b);
        assertEquals(119, res.getAmount()); // 100 *1.19
    }

    @Test
    void whenUpdateNotExists_thenThrowNotFound() {
        when(billingRepository.findById(7)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> billingService.update(7, new Billing()));
    }

    @Test
    void whenUpdateNotFoundUser_thenThrowNotFound() {
        Billing existing = new Billing(); existing.setBillingId(8);
        when(billingRepository.findById(8)).thenReturn(Optional.of(existing));
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.empty());
        assertThrows(NotFoundException.class, () -> billingService.update(8, createBillingForUpdate()));
    }

    @Test
    void whenRemoveNotExists_thenThrowNotFound() {
        when(billingRepository.existsById(9)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> billingService.remove(9));
    }

    @Test
    void whenRemoveExists_thenDeleteCalled() {
        when(billingRepository.existsById(10)).thenReturn(true);
        billingService.remove(10);
        verify(billingRepository).deleteById(10);
    }

    private Billing createBillingForUpdate() {
        Billing b = new Billing();
        b.setUserId("u");
        b.setCourseId("c");
        b.setEnrollmentId(1);
        return b;
    }
}
