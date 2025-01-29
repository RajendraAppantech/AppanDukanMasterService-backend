package com.appan.user.model;

public class SmsMonthlyData {
    private String monthName;
    private int smsCompleted;
    private int smsRejected;
    private int smsPending;
    private int smsReceived;

    // Getters and Setters

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public int getSmsCompleted() {
        return smsCompleted;
    }

    public void setSmsCompleted(int smsCompleted) {
        this.smsCompleted = smsCompleted;
    }

    public int getSmsRejected() {
        return smsRejected;
    }

    public void setSmsRejected(int smsRejected) {
        this.smsRejected = smsRejected;
    }

    public int getSmsPending() {
        return smsPending;
    }

    public void setSmsPending(int smsPending) {
        this.smsPending = smsPending;
    }

    public int getSmsReceived() {
        return smsReceived;
    }

    public void setSmsReceived(int smsReceived) {
        this.smsReceived = smsReceived;
    }
}
