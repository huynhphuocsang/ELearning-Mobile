package com.example.elearningptit.model;

import java.util.List;

public class CreditClassPageForUser {
    int totalPage;
    List<CreditClass> creditClassDTOS;

    public CreditClassPageForUser() {
    }

    public CreditClassPageForUser(int totalPage, List<CreditClass> creditClassDTOS) {
        this.totalPage = totalPage;
        this.creditClassDTOS = creditClassDTOS;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<CreditClass> getCreditClassDTOS() {
        return creditClassDTOS;
    }

    public void setCreditClassDTOS(List<CreditClass> creditClassDTOS) {
        this.creditClassDTOS = creditClassDTOS;
    }

}
