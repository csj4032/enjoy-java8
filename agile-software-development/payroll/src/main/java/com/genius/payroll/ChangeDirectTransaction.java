package com.genius.payroll;

public class ChangeDirectTransaction extends ChangeMethodTransaction {

	private String bank;
	private String account;

	public ChangeDirectTransaction(long empId, String bank, String account) {
		super(empId);
		this.bank = bank;
		this.account = account;
	}

	public ChangeDirectTransaction(long empId) {
		super(empId);
	}

	public PaymentMethod getMethod() {
		return new DirectMethod(bank, account);
	}
}
