package com.santanna.pontoeletronico.service.impl;

import com.santanna.pontoeletronico.domain.dto.EmployeeDto;
import com.santanna.pontoeletronico.domain.dto.auth.RegisterDTO;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.domain.entity.EmployeeRole;
import com.santanna.pontoeletronico.repository.EmployeeRepository;
import com.santanna.pontoeletronico.service.exception.DataIntegrityViolationException;
import com.santanna.pontoeletronico.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    public static final UUID ID = UUID.randomUUID();
    public static final String EMPLOYEE = "Colaborador";
    public static final String EMPLOYEE_NOT_FOUND = "Colaborador não encontrado";
    private static final Integer INDEX = 0;
    public static final String PASSWORD = "123";
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private EmployeeRepository repository;

    private Employee employee;
    private EmployeeDto employeeDto;
    private Optional<Employee> employeeOptional;
    private RegisterDTO registerDTO;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startEmployee();

    }

    @Test
    @DisplayName("When Get Employee By Id Then Return Employee")
    void WhenGetEmployeeByIdThenReturnEmployee() {
        when(repository.findById(ID)).thenReturn(Optional.of(employee));

        // Usa o mesmo ID constante na chamada do serviço
        Employee response = employeeService.getEmployeeById(ID);

        assertNotNull(response);
        assertEquals(employee, response);
        assertEquals(employee.getId(), response.getId());
        assertEquals(employee.getName(), response.getName());
        assertEquals(Employee.class, response.getClass());
    }

    @Test
    @DisplayName("When Get all Employee then return an List of Users")
    void whenGetAllEmployeesThenReturnListOfUsers() {
        when(repository.findAll()).thenReturn(List.of(employee));
        List<Employee> response = employeeService.getAllEmployees();
        assertNotNull(response);
        assertEquals(employee, response.get(0));
        assertEquals(employee.getId(), response.get(0).getId());
        assertEquals(ID,response.get(INDEX).getId());
        assertEquals(EMPLOYEE,response.get(INDEX).getName());
    }

    @Test
    @DisplayName("when Get By Name Then Return Employee Name")
    void whenGetByNameThenReturnEmployeeName() {
        when(repository.findByNameContainsIgnoreCase(anyString())).thenReturn(Optional.of(employee));
        Employee response = employeeService.getByName(EMPLOYEE);
        assertNotNull(response);
        assertEquals(employee, response);
        assertEquals(employee.getId(), response.getId());
        assertEquals(employee.getName(), response.getName());
        assertEquals(ID,response.getId());
        assertEquals(EMPLOYEE,response.getName());
    }

    @Test
    @DisplayName(" when create Then Return Success")
    void whenCreateThenReturnSuccess() {
        when(repository.save(any())).thenReturn(employee);
        Employee response = employeeService.createEmployee(employeeDto);

        assertNotNull(response);
        assertEquals(Employee.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(EMPLOYEE, response.getName());
    }

    @Test
    @DisplayName("When Delete Employee Then Success")
    void deleteWithSuccess() {
        when(repository.findByNameContainsIgnoreCase(anyString())).thenReturn(employeeOptional);
        doNothing().when(repository).deleteById(UUID.randomUUID());

        employeeService.deleteEmployee(EMPLOYEE);

        verify(repository, times(1)).deleteById(employee.getId());
    }

    @Test
    @DisplayName("When Get Employee By Id Then Return Object not found")
    void WhenGetEmployeeByIdThenObjectNotFound() {
        when(repository.findById(UUID.randomUUID())).thenThrow(
                new ObjectNotFoundException((EMPLOYEE_NOT_FOUND)));
        try {
            employeeService.getEmployeeById(ID);
        }catch (Exception e) {
            assertNotNull(e);
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals(EMPLOYEE_NOT_FOUND, e.getMessage());
        }
    }

    @Test
    @DisplayName(" when Create Then Return Data integraty violation")
    void whenCreateThenReturnDataIntegratyViolation(){
        when(repository.findByNameContainsIgnoreCase(anyString())).thenReturn(employeeOptional);
      try {
          employeeOptional.get().setName("");
      }catch (Exception ex){
          assertNotNull(ex);
          assertEquals(DataIntegrityViolationException.class, ex.getClass());
          assertEquals(EMPLOYEE_NOT_FOUND, ex.getMessage());
      }
    }

    @Test
    @DisplayName(" when update Then Return Data integraty violation")
    void whenUpdateThenReturnDataIntegratyViolation(){
        when(repository.findById(UUID.randomUUID())).thenReturn(employeeOptional);
        try {
            employeeOptional.get().setId(UUID.randomUUID());
        }catch (Exception ex){
            assertNotNull(ex);
            assertEquals(DataIntegrityViolationException.class, ex.getClass());
            assertEquals(EMPLOYEE_NOT_FOUND, ex.getMessage());
        }
    }

    @Test
    @DisplayName("When Delete Employee Then Return Object Not Found Exception")
    void deleteWithObjectNotFoundException() {
        when(repository.findByNameContainsIgnoreCase(anyString())).thenThrow(new ObjectNotFoundException(EMPLOYEE_NOT_FOUND));

        try {
            employeeService.deleteEmployee(EMPLOYEE);
        } catch (Exception ex) {
            assertNotNull(ex);
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals(EMPLOYEE_NOT_FOUND, ex.getMessage());
        }
    }

    private void startEmployee() {
        employee = new Employee(ID, EMPLOYEE, PASSWORD, EmployeeRole.ADMIN, null);
        employeeDto = new EmployeeDto(ID, EMPLOYEE, PASSWORD, EmployeeRole.ADMIN);
        employeeOptional = Optional.of(employee);
        registerDTO = new RegisterDTO(EMPLOYEE, PASSWORD, EmployeeRole.ADMIN);
    }

}