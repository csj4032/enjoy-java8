package com.genius.condition;

import org.jetbrains.annotations.NotNull;

public class SafetyNumberCondition {

	@NotNull
	private final boolean safetyNoUseYn;
	@NotNull
	private final IssueType issueType;
	@NotNull
	private final IssueStatus issueStatus;
	@NotNull
	private final ExpiredType expiredType;
	@NotNull
	private final String issuePhone;

	public SafetyNumberCondition(@NotNull boolean safetyNoUseYn, @NotNull IssueType issueType, @NotNull IssueStatus issueStatus, @NotNull ExpiredType expiredType, @NotNull String issuePhone) {
		this.safetyNoUseYn = safetyNoUseYn;
		this.issueType = issueType;
		this.issueStatus = issueStatus;
		this.expiredType = expiredType;
		this.issuePhone = issuePhone;
	}

	public boolean isSafetyNoUseYn() {
		return safetyNoUseYn;
	}

	public IssueType getIssueType() {
		return issueType;
	}

	public IssueStatus getIssueStatus() {
		return issueStatus;
	}

	public ExpiredType getExpiredType() {
		return expiredType;
	}

	public String getIssuePhone() {
		return issuePhone;
	}
}
