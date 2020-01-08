package com.genius.dudm.domain;

import com.genius.dudm.service.EmployeeService;

import java.util.List;
import java.util.Objects;

public class Department {
	private long no;
	private String name;
	private String address;

	public Department(long no, String name, String address) {
		this.no = no;
		this.name = name;
		this.address = address;
	}

	public boolean move(int limitsEmployeeCount, String newAddress) {
		EmployeeService employeeService = new EmployeeService();
		List<Employee> employees = employeeService.findAllEmployeeByDepartment(this);
		if (employees.size() < limitsEmployeeCount + 1) {
			this.setAddress(newAddress);
			employeeService.printForMove(employees);
			return true;
		}
		return false;
	}

	public long getNo() {
		return this.no;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null || getClass() != other.getClass()) return false;
		Department department = (Department) other;
		return no == department.no && name.equals(department.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(no);
	}
}