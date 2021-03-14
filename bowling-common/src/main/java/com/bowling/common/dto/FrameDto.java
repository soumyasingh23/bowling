package com.bowling.common.dto;

public class FrameDto {

    private Integer frameNumber;
    private Integer noOfTry;
    private Integer score;

    public Integer getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(Integer frameNumber) {
        this.frameNumber = frameNumber;
    }

    public Integer getNoOfTry() {
        return noOfTry;
    }

    public void setNoOfTry(Integer noOfTry) {
        this.noOfTry = noOfTry;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
