package lk.driveorbit.DriveOrbit_core.notification.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class FirestoreNotification {
    private String id;
    private String userId;
    private String title;
    private String message;
    private String timestamp;
    private Boolean isRead;
    private String type;

    // Default constructor required for Firestore
    public FirestoreNotification() {
    }

    // Convert from Notification to FirestoreNotification
    public static FirestoreNotification fromNotification(Notification notification) {
        if (notification == null) {
            return null;
        }

        FirestoreNotification firestoreNotification = new FirestoreNotification();
        
        firestoreNotification.setId(notification.getId().toString());
        firestoreNotification.setUserId(notification.getUserId());
        firestoreNotification.setTitle(notification.getTitle());
        firestoreNotification.setMessage(notification.getMessage());
        
        // Convert LocalDateTime to ISO 8601 String with UTC timezone
        if (notification.getTimestamp() != null) {
            String isoTimestamp = notification.getTimestamp().atZone(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            firestoreNotification.setTimestamp(isoTimestamp);
        }
        
        firestoreNotification.setIsRead(notification.getIsRead());
        firestoreNotification.setType(notification.getType());

        return firestoreNotification;
    }

    // Convert from FirestoreNotification to Notification
    public Notification toNotification() {
        Notification notification = new Notification();
        
        if (this.getId() != null) {
            notification.setId(Long.parseLong(this.getId()));
        }
        
        notification.setUserId(this.getUserId());
        notification.setTitle(this.getTitle());
        notification.setMessage(this.getMessage());
        
        // Convert ISO 8601 String to LocalDateTime
        if (this.getTimestamp() != null) {
            LocalDateTime dateTime = LocalDateTime.parse(
                this.getTimestamp(), 
                DateTimeFormatter.ISO_OFFSET_DATE_TIME
            );
            notification.setTimestamp(dateTime);
        }
        
        notification.setIsRead(this.getIsRead());
        notification.setType(this.getType());
        
        return notification;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
