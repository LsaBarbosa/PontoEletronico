package com.santanna.pontoeletronico.service.impl;

import com.santanna.pontoeletronico.domain.dto.EmployeeDto;
import com.santanna.pontoeletronico.domain.entity.Employee;
import com.santanna.pontoeletronico.repository.EmployeeRepository;
import com.santanna.pontoeletronico.service.exception.DataIntegrityViolationException;
import com.santanna.pontoeletronico.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    public static final long ID = 1L;
    public static final String COLABORADOR = "Colaborador";
    public static final String COLABORADOR_NAO_ENCONTRADO = "Colaborador não encontrado";
    private static final Integer INDEX   = 0;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private EmployeeRepository repository;

    private Employee employee;
    private EmployeeDto employeeDto;
    private Optional<Employee> employeeOptional;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startEmployee();

    }

    @Test
    @DisplayName("When Get Employee By Id Then Return Employee")
    void WhenGetEmployeeByIdThenReturnEmployee() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(employee));

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
        assertEquals(COLABORADOR,response.get(INDEX).getName());
    }

    @Test
    @DisplayName("when Get By Name Then Return Employee Name")
    void whenGetByNameThenReturnEmployeeName() {
        when(repository.findByName(anyString())).thenReturn(Optional.of(employee));
        Employee response = employeeService.getByName(COLABORADOR);
        assertNotNull(response);
        assertEquals(employee, response);
        assertEquals(employee.getId(), response.getId());
        assertEquals(employee.getName(), response.getName());
        assertEquals(ID,response.getId());
        assertEquals(COLABORADOR,response.getName());
    }

    @Test
    @DisplayName(" when create Then Return Success")
    void whenCreateThenReturnSuccess() {
        when(repository.save(any())).thenReturn(employee);
        Employee response = employeeService.createEmployee(employeeDto);

        assertNotNull(response);
        assertEquals(Employee.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(COLABORADOR, response.getName());
    }

    @Test
    @DisplayName(" when update Then Return Success")
    void whenUpdateThenReturnSuccess() {
        when(repository.save(any())).thenReturn(employee);
        when(repository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(modelMapper.map(employee, EmployeeDto.class)).thenReturn(employeeDto);
        Employee response = employeeService.updateEmployee(employeeDto);
        assertNotNull(response);
        assertEquals(Employee.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(COLABORADOR, response.getName());
    }

    @Test
    @DisplayName("delete with success")
    void deleteWithSuccess() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(employee));
        doNothing().when(repository).deleteById(anyLong());
        employeeService.deleteEmployee(ID);
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("When Get Employee By Id Then Return Object not found")
    void WhenGetEmployeeByIdThenObjectNotFound() {
        when(repository.findById(anyLong())).thenThrow(
                new ObjectNotFoundException((COLABORADOR_NAO_ENCONTRADO)));
        try {
            employeeService.getEmployeeById(ID);
        }catch (Exception e) {
            assertNotNull(e);
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals(COLABORADOR_NAO_ENCONTRADO, e.getMessage());
        }
    }

    @Test
    @DisplayName(" when Create Then Return Data integraty violation")
    void whenCreateThenReturnDataIntegratyViolation(){
        when(repository.findByName(anyString())).thenReturn(employeeOptional);
      try {
          employeeOptional.get().setName("");
      }catch (Exception ex){
          assertNotNull(ex);
          assertEquals(DataIntegrityViolationException.class, ex.getClass());
          assertEquals("COLABORADOR_NAO_ENCONTRADO", ex.getMessage());
      }
    }

    @Test
    @DisplayName(" when update Then Return Data integraty violation")
    void whenUpdateThenReturnDataIntegratyViolation(){
        when(repository.findById(anyLong())).thenReturn(employeeOptional);
        try {
            employeeOptional.get().setId(2L);
        }catch (Exception ex){
            assertNotNull(ex);
            assertEquals(DataIntegrityViolationException.class, ex.getClass());
            assertEquals(COLABORADOR_NAO_ENCONTRADO, ex.getMessage());
        }
    }

    @Test
    @DisplayName("delete with Object not found exception")
    void deleteWithObjectNotFoundException(){
        when(repository.findById(anyLong())).thenThrow( new ObjectNotFoundException(COLABORADOR_NAO_ENCONTRADO));

        try {
            employeeService.deleteEmployee(ID);
        }catch (Exception ex){
            assertEquals(ObjectNotFoundException.class,ex.getClass());
            assertEquals(COLABORADOR_NAO_ENCONTRADO,ex.getMessage());
        }
    }

    private void startEmployee() {
        employee = new Employee(ID, COLABORADOR, null);
        employeeDto = new EmployeeDto(ID, COLABORADOR);
        employeeOptional = Optional.of(employee);
    }

}