package com.example.woekun.toeiconline.models;

import java.util.ArrayList;


public class SubQuestion {

    private int subQuestionID;
    private int questionID;
    private String content;
    private ArrayList<String> answerList;
    private int result;
    private String explainAnswer;
    private int answerPicked;

    public SubQuestion() {

    }

    public SubQuestion(int questionID, String content, ArrayList<String> answerList, int result, String explainAnswer) {
        this.questionID = questionID;
        this.content = content;
        this.answerList = answerList;
        this.result = result;
        this.explainAnswer = explainAnswer;
    }

    public SubQuestion(int subQuestionID, int questionID, String content, ArrayList<String> answerList, int result, String explainAnswer) {
        this.subQuestionID = subQuestionID;
        this.questionID = questionID;
        this.content = content;
        this.answerList = answerList;
        this.result = result;
        this.explainAnswer = explainAnswer;
    }

    public int getSubQuestionID() {
        return subQuestionID;
    }

    public void setSubQuestionID(int subQuestionID) {
        this.subQuestionID = subQuestionID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public ArrayList<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(ArrayList<String> answerList) {
        this.answerList = answerList;
    }

    public String getExplainAnswer() {
        return explainAnswer;
    }

    public void setExplainAnswer(String explainAnswer) {
        this.explainAnswer = explainAnswer;
    }

    public int getAnswerPicked() {
        return answerPicked;
    }

    public void setAnswerPicked(int answerPicked) {
        this.answerPicked = answerPicked;
    }
}
