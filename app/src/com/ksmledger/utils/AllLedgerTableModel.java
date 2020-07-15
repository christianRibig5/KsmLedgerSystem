package com.ksmledger.utils;

public class AllLedgerTableModel {
    private String id;
    private String firstName;
    private String lastName;
    private String previousOutstanding;
    private String yearlyBudget;
    private String hallLevy;
    private String otherLevies;
    private String totalDues;
    private String totalDuesPaid;
    private String totalUnpaidDues;

    public AllLedgerTableModel(String id, String firstName, String lastName, String previousOutstanding, String yearlyBudget, String hallLevy, String otherLevies, String totalDues, String totalDuesPaid, String totalUnpaidDues) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.previousOutstanding = previousOutstanding;
        this.yearlyBudget = yearlyBudget;
        this.hallLevy = hallLevy;
        this.otherLevies = otherLevies;
        this.totalDues = totalDues;
        this.totalDuesPaid = totalDuesPaid;
        this.totalUnpaidDues = totalUnpaidDues;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPreviousOutstanding() {
        return previousOutstanding;
    }

    public void setPreviousOutstanding(String previousOutstanding) {
        this.previousOutstanding = previousOutstanding;
    }

    public String getYearlyBudget() {
        return yearlyBudget;
    }

    public void setYearlyBudget(String yearlyBudget) {
        this.yearlyBudget = yearlyBudget;
    }

    public String getHallLevy() {
        return hallLevy;
    }

    public void setHallLevy(String hallLevy) {
        this.hallLevy = hallLevy;
    }

    public String getOtherLevies() {
        return otherLevies;
    }

    public void setOtherLevies(String otherLevies) {
        this.otherLevies = otherLevies;
    }

    public String getTotalDues() {
        return totalDues;
    }

    public void setTotalDues(String totalDues) {
        this.totalDues = totalDues;
    }

    public String getTotalDuesPaid() {
        return totalDuesPaid;
    }

    public void setTotalDuesPaid(String totalDuesPaid) {
        this.totalDuesPaid = totalDuesPaid;
    }

    public String getTotalUnpaidDues() {
        return totalUnpaidDues;
    }

    public void setTotalUnpaidDues(String totalUnpaidDues) {
        this.totalUnpaidDues = totalUnpaidDues;
    }
}
