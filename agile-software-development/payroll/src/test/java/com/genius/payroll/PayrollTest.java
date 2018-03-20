package com.genius.payroll;

import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class PayrollTest {

	@Test
	@Ignore
	public void testAddSalariedEmployee() {
		long empId = 1L;
		AddEmployeeTransaction addEmployeeTransaction = new AddSalariedEmployee(empId, "Bob", "Home", 1000.00);
		addEmployeeTransaction.execute();

		Employee employee = PayrollDatabase.getEmployee(empId);
		assertThat("Bob", is(employee.getName()));

		PaymentClassification paymentClassification = employee.getClassification();
		SalariedClassification salariedClassification = (SalariedClassification) paymentClassification;
		assertThat(1000.00, is(salariedClassification.getSalary()));

		PaymentSchedule paymentSchedule = employee.getSchedule();
		MonthlySchedule monthlySchedule = (MonthlySchedule) paymentSchedule;
		assertNotNull(monthlySchedule);

		PaymentMethod paymentMethod = employee.getPaymentMethod();
		HoldMethod holdMethod = (HoldMethod) paymentMethod;
		assertNotNull(holdMethod);
	}

	@Test
	@Ignore
	public void testDeleteEmployee() {
		long empId = 3;
		AddCommissionedEmployee addCommissionedEmployee = new AddCommissionedEmployee(empId, "Lance", "Home", 2500.00, 3.2);
		addCommissionedEmployee.execute();

		Employee employee = PayrollDatabase.getEmployee(empId);
		assertThat(employee.getAddress(), is("Home"));

		DeleteEmployeeTransaction deleteEmployeeTransaction = new DeleteEmployeeTransaction(empId);
		deleteEmployeeTransaction.execute();
		assertNull(PayrollDatabase.getEmployee(3));
	}

	@Test
	@Ignore
	public void testTimeCardTransaction() throws InvalidEmployeeException {
		long empId = 2;
		LocalDate date = LocalDate.of(2001, 12, 31);
		AddHourlyEmployee addHourlyEmployee = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
		addHourlyEmployee.execute();

		TimeCardTransaction timeCardTransaction = new TimeCardTransaction(date, 8.0, empId);
		timeCardTransaction.execute();

		Employee employee = PayrollDatabase.getEmployee(empId);
		assertNotNull(employee);

		HourlyClassification hourlyClassification = (HourlyClassification) employee.getClassification();
		TimeCard timeCard = hourlyClassification.getTimeCard(date);
		assertThat(8.0, is(timeCard.getHours()));
	}

	@Test
	@Ignore
	public void testAddServiceCharge() {
		long empId = 2;
		AddHourlyEmployee addHourlyEmployee = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
		addHourlyEmployee.execute();

		Employee employee = PayrollDatabase.getEmployee(empId);
		assertThat("Bill", is(employee.getName()));

		UnionAffiliation unionAffiliation = new UnionAffiliation(12.5);
		employee.setAffiliation(unionAffiliation);

		long memberId = 86l;
		LocalDate date = LocalDate.of(2001, 12, 1);
		PayrollDatabase.addUnionMember(memberId, employee);

		ServiceChargeTransaction serviceChargeTransaction = new ServiceChargeTransaction(memberId, date, 12.95);
		serviceChargeTransaction.execute();

		ServiceCharge serviceCharge = unionAffiliation.getServiceCharge(date);

		assertThat(12.95, is(serviceCharge.getAmount()));
	}

	@Test
	@Ignore
	public void testChangeNameTransaction() {
		long empId = 2;
		AddHourlyEmployee addHourlyEmployee = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
		addHourlyEmployee.execute();

		ChangeNameTransaction changeNameTransaction = new ChangeNameTransaction(empId, "Bob");
		changeNameTransaction.execute();

		Employee employee = PayrollDatabase.getEmployee(empId);
		assertThat("Bob", is(employee.getName()));
	}

	@Test
	@Ignore
	public void testChangeHourlyTransaction() {
		long empId = 4l;
		AddCommissionedEmployee addCommissionedEmployee = new AddCommissionedEmployee(empId, "Lance", "Home", 2500, 3.2);
		addCommissionedEmployee.execute();

		ChangeHourlyTransaction changeHourlyTransaction = new ChangeHourlyTransaction(empId, 27.52);
		changeHourlyTransaction.execute();

		Employee employee = PayrollDatabase.getEmployee(empId);

		HourlyClassification hourlyClassification = (HourlyClassification) employee.getClassification();
		assertThat(27.52, is(hourlyClassification.getRate()));

		WeeklySchedule paymentSchedule = (WeeklySchedule) employee.getSchedule();
		assertNotNull(paymentSchedule);
	}

	@Test
	@Ignore
	public void testChangeMemberTransaction() {
		long empId = 2;
		int memberId = 7734;
		AddHourlyEmployee addHourlyEmployee = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
		addHourlyEmployee.execute();

		ChangeMemberTransaction changeMemberTransaction = new ChangeMemberTransaction(empId, memberId, 99.42);
		changeMemberTransaction.execute();

		Employee employee = PayrollDatabase.getEmployee(empId);
		UnionAffiliation unionAffiliation = (UnionAffiliation) employee.getAffiliation();
		assertThat(99.42, is(unionAffiliation.getDues()));

		Employee member = PayrollDatabase.getUnionMember(memberId);
		assertThat(employee, is(member));
	}

	@Test
	@Ignore
	public void testPaySingleSalariedEmployee() {
		long empId = 1;
		AddSalariedEmployee addSalariedEmployee = new AddSalariedEmployee(empId, "Bob", "Home", 1000.00);
		addSalariedEmployee.execute();
		LocalDate payDate = LocalDate.of(2001, 11, 30);
		PaydayTransaction paydayTransaction = new PaydayTransaction(payDate);
		paydayTransaction.execute();

		Paycheck paycheck = paydayTransaction.getPayCheck(empId);

		//assertThat(paycheck.getPayDate(), is(payDate));
		assertThat(1000.00, is(paycheck.getGrossPay()));
		assertThat(0.0, is(paycheck.getDeductions()));
		assertThat(1000.00, is(paycheck.getNetPay()));
		assertThat("Hold", is(paycheck.getField("Disposition")));
	}

	@Test
	@Ignore
	public void testPaySingleSalariedEmployeeOnWrongDate() {
		long empId = 1;
		AddSalariedEmployee addSalariedEmployee = new AddSalariedEmployee(empId, "Bob", "Home", 1000.00);
		addSalariedEmployee.execute();
		LocalDate payDate = LocalDate.of(2001, 11, 29);
		PaydayTransaction paydayTransaction = new PaydayTransaction(payDate);
		paydayTransaction.execute();
		Paycheck paycheck = paydayTransaction.getPayCheck(empId);
		assertNull(paycheck);
	}

	@Test
	@Ignore
	public void testPaySingleHourlyEmployeeNoTimeCards() {
		long empId = 2;
		AddHourlyEmployee addHourlyEmployee = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
		addHourlyEmployee.execute();
		LocalDate payDate = LocalDate.of(2001, 11, 9);
		PaydayTransaction paydayTransaction = new PaydayTransaction(payDate);
		paydayTransaction.execute();

		validateHourlyPayCheck(paydayTransaction, empId, payDate, 0.0);
	}

	@Test
	@Ignore
	public void testPaySingleHourlyEmployeeOneTimeCard() throws InvalidEmployeeException {
		long empId = 2;
		AddHourlyEmployee addHourlyEmployee = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
		addHourlyEmployee.execute();
		LocalDate payDate = LocalDate.of(2001, 11, 9);

		TimeCardTransaction timeCardTransaction = new TimeCardTransaction(payDate, 2.0, empId);
		timeCardTransaction.execute();

		PaydayTransaction paydayTransaction = new PaydayTransaction(payDate);
		paydayTransaction.execute();
		validateHourlyPayCheck(paydayTransaction, empId, payDate, 30.5);
	}

	@Test
	@Ignore
	public void testPaySingleHourlyEmployeeOnWrongDate() throws InvalidEmployeeException {
		long empId = 2l;
		AddHourlyEmployee addHourlyEmployee = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
		addHourlyEmployee.execute();
		LocalDate payDate = LocalDate.of(2001, 11, 8);
		TimeCardTransaction timeCardTransaction = new TimeCardTransaction(payDate, 9.0, empId);
		timeCardTransaction.execute();
		PaydayTransaction paydayTransaction = new PaydayTransaction(payDate);
		paydayTransaction.execute();

		Paycheck paycheck = paydayTransaction.getPayCheck(empId);
		assertNull(paycheck);
	}

	@Test
	@Ignore
	public void testPaySingleHourlyEmployeeTwoTimeCards() throws InvalidEmployeeException {
		long empId = 2l;
		AddHourlyEmployee addHourlyEmployee = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
		addHourlyEmployee.execute();
		LocalDate payDate = LocalDate.of(2001, 11, 9);

		TimeCardTransaction timeCardTransaction = new TimeCardTransaction(payDate, 2.0, empId);
		timeCardTransaction.execute();

		TimeCardTransaction timeCardTransaction2 = new TimeCardTransaction(payDate, 7.0, empId);
		timeCardTransaction2.execute();

		PaydayTransaction paydayTransaction = new PaydayTransaction(payDate);
		paydayTransaction.execute();
		validateHourlyPayCheck(paydayTransaction, empId, payDate, 7 * 15.25);
	}

	@Test
	@Ignore
	public void testPaySingleHourlyEmployeeWithTimeCardsSpanningTwoPayPeriods() throws InvalidEmployeeException {
		long empId = 2l;
		AddHourlyEmployee addHourlyEmployee = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
		addHourlyEmployee.execute();

		LocalDate payDate = LocalDate.of(2001, 11, 9);
		LocalDate dateInPreviousPayPeriod = LocalDate.of(2001, 11, 2);

		TimeCardTransaction timeCardTransaction = new TimeCardTransaction(payDate, 2.0, empId);
		timeCardTransaction.execute();

		TimeCardTransaction timeCardTransaction2 = new TimeCardTransaction(dateInPreviousPayPeriod, 5.0, empId);
		timeCardTransaction2.execute();

		PaydayTransaction paydayTransaction = new PaydayTransaction(payDate);
		paydayTransaction.execute();
		validateHourlyPayCheck(paydayTransaction, empId, payDate, 2 * 15.25);
	}

	@Test
	@Ignore
	public void testSalariedUnionMemberDues() {
		long empId = 1;
		AddSalariedEmployee addSalariedEmployee = new AddSalariedEmployee(empId, "Bob", "Home", 1000.00);
		addSalariedEmployee.execute();

		long memberId = 7734;
		ChangeMemberTransaction changeMemberTransaction = new ChangeMemberTransaction(empId, memberId, 9.42);
		changeMemberTransaction.execute();

		LocalDate payDate = LocalDate.of(2001, 11, 30);
		int fridays = 5;
		PaydayTransaction paydayTransaction = new PaydayTransaction(payDate);
		paydayTransaction.execute();

		Paycheck paycheck = paydayTransaction.getPayCheck(empId);

		assertThat(1000 - (fridays * 9.42), is(paycheck.getNetPay()));
	}

	@Test
	@Ignore
	public void testHourlyUnionMemberServiceCharge() throws InvalidEmployeeException {
		long empId = 1l;
		AddHourlyEmployee addHourlyEmployee = new AddHourlyEmployee(empId, "Bill", "Home", 15.24);
		addHourlyEmployee.execute();

		int memberId = 7734;
		ChangeMemberTransaction changeMemberTransaction = new ChangeMemberTransaction(empId, memberId, 9.42);
		changeMemberTransaction.execute();
		LocalDate payDate = LocalDate.of(2001, 11, 9);
		ServiceChargeTransaction serviceChargeTransaction = new ServiceChargeTransaction(memberId, payDate, 19.42);
		serviceChargeTransaction.execute();
		TimeCardTransaction timeCardTransaction = new TimeCardTransaction(payDate, 8.0, empId);
		timeCardTransaction.execute();
		PaydayTransaction paydayTransaction = new PaydayTransaction(payDate);
		paydayTransaction.execute();
		Paycheck paycheck = paydayTransaction.getPayCheck(empId);
		assertThat(paycheck.getPayPeriodEndDate(), is(payDate));
		assertThat(8 * 15.24, is(paycheck.getGrossPay()));
		assertThat(9.42 + 19.42, is(paycheck.getDeductions()));
		assertThat((8 * 15.24) - (9.42 + 19.42), is(paycheck.getNetPay()));
	}

	@Test
	public void testServiceChargesSpanningMultiplePayPeriods() throws InvalidEmployeeException {
		long empId = 1l;
		AddHourlyEmployee addHourlyEmployee = new AddHourlyEmployee(empId, "Bill", "Home", 15.24);
		addHourlyEmployee.execute();
		int memberId = 7734;
		ChangeMemberTransaction changeMemberTransaction = new ChangeMemberTransaction(empId, memberId, 9.42);
		changeMemberTransaction.execute();

		LocalDate earlyDate = LocalDate.of(2001, 11, 2);
		LocalDate payDate = LocalDate.of(2001, 11, 9);
		LocalDate lateDate = LocalDate.of(2001, 11, 16);

		ServiceChargeTransaction serviceChargeTransaction = new ServiceChargeTransaction(memberId, payDate, 19.42);
		serviceChargeTransaction.execute();

		ServiceChargeTransaction serviceChargeTransactionEarly = new ServiceChargeTransaction(memberId, earlyDate, 100.00);
		serviceChargeTransactionEarly.execute();

		ServiceChargeTransaction serviceChargeTransactionLate = new ServiceChargeTransaction(memberId, lateDate, 200.00);
		serviceChargeTransactionLate.execute();

		TimeCardTransaction timeCardTransaction = new TimeCardTransaction(payDate, 8.0, empId);
		timeCardTransaction.execute();

		PaydayTransaction paydayTransaction = new PaydayTransaction(payDate);
		paydayTransaction.execute();

		Paycheck paycheck = paydayTransaction.getPayCheck(empId);

		assertThat(paycheck.getPayPeriodEndDate(), is(payDate));
		assertThat(8 * 15.24, is(paycheck.getGrossPay()));
		assertThat("Hold", is(paycheck.getField("Disposition")));
		assertThat(9.42 + 19.42, is(paycheck.getDeductions()));
		assertThat((8 * 15.24) - (9.42 + 19.42), is(paycheck.getNetPay()));

	}

	private void validateHourlyPayCheck(PaydayTransaction paydayTransaction, long empId, LocalDate payDate, double pay) {
		Paycheck paycheck = paydayTransaction.getPayCheck(empId);
		assertThat(payDate, is(paycheck.getPayPeriodEndDate()));
		assertThat(pay, is(paycheck.getGrossPay()));
		assertThat("Hold", is(paycheck.getField("Disposition")));
		assertThat(0.0, is(paycheck.getDeductions()));
		assertThat(pay, is(paycheck.getNetPay()));
	}
}