package com.ksmledger.utils;

public class UserTableModel {

    private String id;
    private String firstName;
    private String lastName;
    private String membershipId;
    private String email;
    private String phone;
    private String initiationDate;

    public UserTableModel(String id, String firstName, String lastName, String membershipId, String email, String phone, String initiationDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.membershipId = membershipId;
        this.email = email;
        this.phone = phone;
        this.initiationDate = initiationDate;
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

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInitiationDate() {
        return initiationDate;
    }

    public void setInitiationDate(String initiationDate) {
        this.initiationDate = initiationDate;
    }
}
