package eu.telecomnancy.test.base;

public class Message {
    private int messageId;
    private int senderUserId;
    private int receiverUserId;
    private int subject;
    private String messageText;
    private int messageDate;

    // Constructeur
    public Message(int messageId, int senderUserId, int receiverUserId, int subject, String messageText, int messageDate) {
        this.messageId = messageId;
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.subject = subject;
        this.messageText = messageText;
        this.messageDate = messageDate;
    }

    // Getters et Setters

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(int senderUserId) {
        this.senderUserId = senderUserId;
    }

    public int getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(int receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public int getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(int messageDate) {
        this.messageDate = messageDate;
    }
}