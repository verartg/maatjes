package com.example.maatjes.dtos;

public class AccountDto {
    public Long id;
    public String name;
    public int age;
    public char sex;
    public String phoneNumber;
    public String emailAddress;
    public String street;
    public String houseNumber;
    public String postalCode;
    public String city;
    //    private image profilePicture;
    public String bio;
    // private pdf identification;
    public boolean givesHelp;
    public boolean needsHelp;
    public String activitiesToGive;
    public String activitiesToReceive;
    public String availability;
    public String frequency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isGivesHelp() {
        return givesHelp;
    }

    public void setGivesHelp(boolean givesHelp) {
        this.givesHelp = givesHelp;
    }

    public boolean isNeedsHelp() {
        return needsHelp;
    }

    public void setNeedsHelp(boolean needsHelp) {
        this.needsHelp = needsHelp;
    }

    public String getActivitiesToGive() {
        return activitiesToGive;
    }

    public void setActivitiesToGive(String activitiesToGive) {
        this.activitiesToGive = activitiesToGive;
    }

    public String getActivitiesToReceive() {
        return activitiesToReceive;
    }

    public void setActivitiesToReceive(String activitiesToReceive) {
        this.activitiesToReceive = activitiesToReceive;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
