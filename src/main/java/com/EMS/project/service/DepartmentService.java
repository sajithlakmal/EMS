package com.EMS.project.service;

import com.EMS.project.model.Department;
import com.EMS.project.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }


    @Transactional
    public Department createDepartment(Department department) {
        if (departmentRepository.findByName(department.getName()).isPresent()) {
            throw new IllegalArgumentException("Department with name '" + department.getName() + "' already exists.");
        }
        return departmentRepository.save(department);
    }


    @Transactional(readOnly = true)
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + id));
    }


    @Transactional
    public Department updateDepartment(Long id, Department department) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + id));

        Optional<Department> departmentByName = departmentRepository.findByName(department.getName());
        if (departmentByName.isPresent() && !departmentByName.get().getId().equals(id)) {
            throw new IllegalArgumentException("Another department with name '" + department.getName() + "' already exists.");
        }

        existingDepartment.setName(department.getName());


        return departmentRepository.save(existingDepartment);
    }


    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Department not found with ID: " + id);
        }
        departmentRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
}
