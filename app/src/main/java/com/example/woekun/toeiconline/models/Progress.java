package com.example.woekun.toeiconline.models;

/**
 * Created by woekun on 20/2/2016.
 */
public class Progress {
    private String email;
    private int subQuestionID;
    private int part;
    private int answerPicked;

    public Progress(){

    }

    public Progress(int subQuestionID, int answerPicked) {
        this.subQuestionID = subQuestionID;
        this.answerPicked = answerPicked;
    }

    public Progress(String email, int subQuestionID, int part, int answerPicked) {
        this.email = email;
        this.subQuestionID = subQuestionID;
        this.part = part;
        this.answerPicked = answerPicked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSubQuestionID() {
        return subQuestionID;
    }

    public void setSubQuestionID(int subQuestionID) {
        this.subQuestionID = subQuestionID;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public int getAnswerPicked() {
        return answerPicked;
    }

    public void setAnswerPicked(int answerPicked) {
        this.answerPicked = answerPicked;
    }
}
