package com.ksmledger.utils;

public class LedgerTableModel {
    private String id;
    private String transactionDate;
    private String membershipId;
    private String firstName;
    private String lastName;
    private String debit;
    private String credit;
    private String particulars;

    public LedgerTableModel(String id, String transactionDate, String membershipId, String firstName, String lastName, String debit, String credit, String particulars) {
        this.id = id;
        this.transactionDate = transactionDate;
        this.membershipId = membershipId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.debit = debit;
        this.credit = credit;
        this.particulars = particulars;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }
}
