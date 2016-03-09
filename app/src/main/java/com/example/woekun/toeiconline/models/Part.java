package com.example.woekun.toeiconline.models;

public class Part {

    private String partName;
    private String partNumber;
    private int partResIdImage;
    private int progress;
    private int numberQuestion;

    public Part(int partResIdImage, String partName,int progress, int partNumber, int numberQuestion ) {
        this.progress = progress;
        this.partResIdImage = partResIdImage;
        this.partNumber = "Part " + partNumber;
        this.partName = partName;
        this.numberQuestion = numberQuestion;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getPartResIdImage() {
        return partResIdImage;
    }

    public void setPartResIdImage(int partResIdImage) {
        this.partResIdImage = partResIdImage;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public int getNumberQuestion() {
        return numberQuestion;
    }

    public void setNumberQuestion(int numberQuestion) {
        this.numberQuestion = numberQuestion;
    }
}
