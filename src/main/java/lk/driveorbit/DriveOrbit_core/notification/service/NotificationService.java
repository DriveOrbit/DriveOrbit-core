package lk.driveorbit.DriveOrbit_core.notification.service;

import com.google.cloud.firestore.Firestore;
import lk.driveorbit.DriveOrbit_core.notification.model.FirestoreNotification;
import lk.driveorbit.DriveOrbit_core.notification.model.Notification;
import lk.driveorbit.DriveOrbit_core.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static final String COLLECTION_NAME = "notifications";
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired(required = false)
    private Firestore firestore;
    
    /**
     * Create a new notification
     */
    public Notification createNotification(Notification notification) {
        logger.info("Creating new notification with title: {}", notification.getTitle());
        
        // Set timestamp if not provided
        if (notification.getTimestamp() == null) {
            notification.setTimestamp(LocalDateTime.now());
        }
        
        // Set default isRead value if not provided
        if (notification.getIsRead() == null) {
            notification.setIsRead(false);
        }
        
        // Save to PostgreSQL
        Notification savedNotification = notificationRepository.save(notification);
        
        // Save to Firestore if available
        if (firestore != null) {
            try {
                FirestoreNotification firestoreNotification = FirestoreNotification.fromNotification(savedNotification);
                firestore.collection(COLLECTION_NAME).document(firestoreNotification.getId())
                    .set(firestoreNotification).get();
                logger.info("Notification {} saved to Firestore", notification.getId());
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error saving notification to Firestore: {}", e.getMessage(), e);
            }
        }
        
        return savedNotification;
    }
    
    /**
     * Get all notifications for a user
     */
    public List<Notification> getNotificationsByUser(String userId) {
        logger.info("Fetching notifications for user: {}", userId);
        return notificationRepository.findByUserIdOrderByTimestampDesc(userId);
    }
    
    /**
     * Get notification by ID
     */
    public Notification getNotificationById(Long id) {
        logger.info("Fetching notification with ID: {}", id);
        
        Optional<Notification> notification = notificationRepository.findById(id);
        return notification.orElse(null);
    }
    
    /**
     * Mark a notification as read
     */
    public Notification markNotificationAsRead(Long id) {
        logger.info("Marking notification {} as read", id);
        
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        if (!notificationOpt.isPresent()) {
            logger.warn("Notification with ID {} not found", id);
            return null;
        }
        
        Notification notification = notificationOpt.get();
        notification.setIsRead(true);
        
        // Save to PostgreSQL
        Notification updatedNotification = notificationRepository.save(notification);
        
        // Update in Firestore if available
        if (firestore != null) {
            try {
                FirestoreNotification firestoreNotification = FirestoreNotification.fromNotification(updatedNotification);
                firestore.collection(COLLECTION_NAME).document(firestoreNotification.getId())
                    .set(firestoreNotification).get();
                logger.info("Notification {} updated in Firestore", id);
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error updating notification in Firestore: {}", e.getMessage(), e);
            }
        }
        
        return updatedNotification;
    }
    
    /**
     * Delete a notification
     */
    public boolean deleteNotification(Long id) {
        logger.info("Deleting notification with ID: {}", id);
        
        if (!notificationRepository.existsById(id)) {
            logger.warn("Notification with ID {} not found", id);
            return false;
        }
        
        // Delete from PostgreSQL
        notificationRepository.deleteById(id);
        
        // Delete from Firestore if available
        if (firestore != null) {
            try {
                firestore.collection(COLLECTION_NAME).document(id.toString()).delete().get();
                logger.info("Notification {} deleted from Firestore", id);
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error deleting notification from Firestore: {}", e.getMessage(), e);
            }
        }
        
        return true;
    }
    
    /**
     * Get unread notifications for a user
     */
    public List<Notification> getUnreadNotifications(String userId) {
        logger.info("Fetching unread notifications for user: {}", userId);
        return notificationRepository.findByUserIdAndIsRead(userId, false);
    }
    
    /**
     * Count unread notifications for a user
     */
    public long countUnreadNotifications(String userId) {
        logger.info("Counting unread notifications for user: {}", userId);
        return notificationRepository.countByUserIdAndIsRead(userId, false);
    }
    
    /**
     * Get notifications by type
     */
    public List<Notification> getNotificationsByType(String type) {
        logger.info("Fetching notifications with type: {}", type);
        return notificationRepository.findByType(type);
    }
    
    /**
     * Get latest notifications for a user
     */
    public List<Notification> getLatestNotifications(String userId, int limit) {
        logger.info("Fetching latest {} notifications for user: {}", limit, userId);
        return notificationRepository.findLatestNotifications(userId, limit);
    }
    
    /**
     * Mark all notifications for a user as read
     */
    public void markAllNotificationsAsRead(String userId) {
        logger.info("Marking all notifications as read for user: {}", userId);
        
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndIsRead(userId, false);
        for (Notification notification : unreadNotifications) {
            notification.setIsRead(true);
            
            // Update in Firestore as well
            if (firestore != null) {
                try {
                    FirestoreNotification firestoreNotification = FirestoreNotification.fromNotification(notification);
                    firestore.collection(COLLECTION_NAME).document(firestoreNotification.getId())
                        .set(firestoreNotification).get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("Error updating notification in Firestore: {}", e.getMessage(), e);
                }
            }
        }
        
        // Save all to PostgreSQL
        notificationRepository.saveAll(unreadNotifications);
    }
}
