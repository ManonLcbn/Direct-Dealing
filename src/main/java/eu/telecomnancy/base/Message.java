package eu.telecomnancy.base ;

import eu.telecomnancy.DAO.JdbcUser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private int id;
    private int senderID;
    private int recipientID;
    private int subject;
    private String body;
    private LocalDateTime dateUTC;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Constructeurs

    public Message(int id, int senderID, int recipientID, int subject, String body, LocalDateTime dateUTC) {
        this.id = id;
        this.senderID = senderID;
        this.recipientID = recipientID;
        this.subject = subject;
        this.body = body;
        this.dateUTC = dateUTC;
    }

    // Getters et setters

    public int getID(){return this.id ;}
    public void setID(int id){this.id = id ;}

    public int getSender(){return this.senderID ;}
    public void setSender(int id){this.senderID = id ;}

    public int getRecipient(){return this.recipientID ;}
    public void setRecipient(int id){this.recipientID = id ;}

    public int getSubject(){return this.subject ;}
    public void setSubject(int subject){this.subject = subject ;}

    public String getBody(){return this.body ;}
    public void setBody(String body){this.body = body ;}

    public LocalDateTime getDateUTC(){return this.dateUTC ;}
    public void setDate(LocalDateTime date){this.dateUTC = date ;}

    // Autres

    public String getSenderName() {
        // Appelle la méthode statique dans JdbcUser pour obtenir le nom de l'expéditeur
        String senderName = JdbcUser.getUserNameById(senderID);
        return senderName;
    }

    public String getRecipientName() {
        return JdbcUser.getUserNameById(recipientID);
    }

    public String getFormattedDate() {
        // Formate la date et l'heure d'envoi du message en tant que chaîne de caractères
        return dateUTC.format(DATE_TIME_FORMATTER);
    }

    public boolean isUserSender(int userId) {
        return this.senderID == userId;
    }

}
