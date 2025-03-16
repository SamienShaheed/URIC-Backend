package com.example.uric.service.impl;

import com.example.uric.dto.EmployeeDto;
import com.example.uric.entity.Employee;
import com.example.uric.exception.ResourceNotFoundException;
import com.example.uric.mapper.EmployeeMapper;
import com.example.uric.repository.EmployeeRepository;
import com.example.uric.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist"));
        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(EmployeeMapper::mapToEmployeeDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto newEmployeeDto) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee does not exist"));

        // Update only if the key has a value in the request
        if(newEmployeeDto.getFirstName() != null) {
            employee.setFirstName(newEmployeeDto.getFirstName());
        }
        if(newEmployeeDto.getLastName() != null) {
            employee.setLastName(newEmployeeDto.getLastName());
        }
        if(newEmployeeDto.getEmail() != null) {
            employee.setEmail(newEmployeeDto.getEmail());
        }

        Employee updatedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.mapToEmployeeDto(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee does not exist"));

        employeeRepository.deleteById(employeeId);
    }
}
