package com.gakdevelopers.studotest.models;

public class CategoryModel {
    private String docId;
    private String name;
    private int noOfTest;

    public CategoryModel(String docId, String name, int noOfTest) {
        this.docId = docId;
        this.name = name;
        this.noOfTest = noOfTest;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoOfTest() {
        return noOfTest;
    }

    public void setNoOfTest(int noOfTest) {
        this.noOfTest = noOfTest;
    }
}
