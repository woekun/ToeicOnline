package com.example.woekun.toeiconline.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Question {

    private int questionID;
    private int level;
    private int part;
    private String paragraph;
    private String audio;
    private String image;
    private ArrayList<SubQuestion> subQuestionList;

    public Question(){

    }

    public Question(int questionID,int level, int part, String paragraph, ArrayList<SubQuestion> subQuestionList, String image, String audio) {
        this.questionID = questionID;
        this.level = level;
        this.part = part;
        this.paragraph = paragraph;
        this.subQuestionList = subQuestionList;
        this.image = image;
        this.audio = audio;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public ArrayList<SubQuestion> getSubQuestionList() {
        return subQuestionList;
    }

    public void setSubQuestionList(ArrayList<SubQuestion> subQuestionList) {
        this.subQuestionList = subQuestionList;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
