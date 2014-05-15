package com.cista.pidb.core.mail;

public class MailTo {
    
    private String subject;
    private String text;
    private String[] assignEmail;
    
    public String[] getAssignEmail() {
        return assignEmail;
    }
    public void setAssignEmail(String[] assignEmail) {
        this.assignEmail = assignEmail;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    

}
