package net.javaguides.springboot.mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.mindtree.employeemanagerapp.EmployeeManagementApplication;
import com.mindtree.employeemanagerapp.exception.ResourceNotFoundException;
import com.mindtree.employeemanagerapp.model.Employee;
import com.mindtree.employeemanagerapp.repository.EmployeeRepository;
import com.mindtree.employeemanagerapp.service.serviceimpl.EmployeeServiceImpl;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes=EmployeeManagementApplication.class)
public class SpringbootBackedApplicationMockitoTest {

	
	@Mock
	private EmployeeRepository employeeRepository;
	
	@InjectMocks
	private EmployeeServiceImpl employeeService;
	
	@Test
	public void saveEmployee() {
		Employee employee=new Employee();
		employee.setFirstName("AKASH");
		when(employeeRepository.save(ArgumentMatchers.any(Employee.class))).thenReturn(employee);
		Employee emp=employeeService.createEmployee(employee);
		assertEquals(emp.getFirstName(),employee.getFirstName());
		verify(employeeRepository).save(employee);
		
				
	}
	
	@Test
	 public void getEmployeeByIdTest()throws ResourceNotFoundException {
		
		Optional<Employee> employee=Optional.of(new Employee("challa","naveen","prveen@gmail.com"));
		when(employeeRepository.findById(5L)).thenReturn(employee).thenReturn(null);
		Employee emp=employeeService.getEmployeeById(5L);
		assertEquals("challa",emp.getFirstName());
		assertEquals("naveen",emp.getLastName());
		assertEquals("prveen@gmail.com",emp.getEmailId());
		}
		
	@Test
	public void deleteEmployeeById() {
		Optional<Employee> employee=Optional.of(new Employee("challa","naveen","prveen@gmail.com"));
		employee.get().setId(30L);
		when(employeeRepository.findById(employee.get().getId())).thenReturn(employee).thenReturn(null);
		boolean value= employeeService.deleteEmployee(employee.get().getId());
		//verify(employeeRepository).deleteById(employee.get().getId());
		assertTrue(value);
		
	}
	
	@Test(expected=RuntimeException.class)	
	public void deleteEmployeeByIdNotFound() {
		Employee employee=new Employee("challa","naveen","prveen@gmail.com");
		 employee.setId(1L);
		 when(employeeRepository.findById(employee.getId())).thenReturn(Optional.ofNullable(null));
		 employeeService.deleteEmployee(employee.getId());
		verify(employeeRepository,times(3)).deleteById(employee.getId());
		
	}

	@Test
	public void updateEmployeeById() {
		Employee employee=new Employee("hii","kii","h@gmail.com");
		employee.setId(12L);	
		Employee newemployee=new Employee();
		newemployee.setFirstName("king");
		newemployee.setId(12L);
		when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
		Employee updated=employeeService.updateEmployeeByIdWithNewEmployee(employee.getId(),newemployee);
		//verify(employeeRepository).save(newemployee);
		assertEquals(newemployee.getFirstName(),employee.getFirstName());
		
	}
	
	

}
