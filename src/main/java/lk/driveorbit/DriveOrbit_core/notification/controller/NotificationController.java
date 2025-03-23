package lk.driveorbit.DriveOrbit_core.notification.controller;

import lk.driveorbit.DriveOrbit_core.notification.model.Notification;
import lk.driveorbit.DriveOrbit_core.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    /**
     * Get all notifications for a user
     */
    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications(
            @RequestParam String userId,
            @RequestParam(required = false, defaultValue = "10") int limit) {
        logger.info("Received request to get notifications for user: {}", userId);
        
        if (limit > 0) {
            return ResponseEntity.ok(notificationService.getLatestNotifications(userId, limit));
        }
        
        return ResponseEntity.ok(notificationService.getNotificationsByUser(userId));
    }

    /**
     * Get a specific notification by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(@PathVariable Long id) {
        logger.info("Received request to get notification with ID: {}", id);
        
        Notification notification = notificationService.getNotificationById(id);
        if (notification == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Notification not found with ID: " + id);
        }
        
        return ResponseEntity.ok(notification);
    }

    /**
     * Create a new notification
     */
    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody Notification notification) {
        try {
            logger.info("Received request to create notification with title: {}", notification.getTitle());
            
            Notification createdNotification = notificationService.createNotification(notification);
            return new ResponseEntity<>(createdNotification, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating notification: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating notification: " + e.getMessage());
        }
    }

    /**
     * Mark a notification as read
     */
    @PatchMapping("/{id}/read")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long id) {
        try {
            logger.info("Received request to mark notification {} as read", id);
            
            Notification updatedNotification = notificationService.markNotificationAsRead(id);
            if (updatedNotification == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Notification not found with ID: " + id);
            }
            
            return ResponseEntity.ok(updatedNotification);
        } catch (Exception e) {
            logger.error("Error marking notification as read: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error marking notification as read: " + e.getMessage());
        }
    }

    /**
     * Delete a notification
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        try {
            logger.info("Received request to delete notification with ID: {}", id);
            
            boolean deleted = notificationService.deleteNotification(id);
            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Notification not found with ID: " + id);
            }
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Notification deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error deleting notification: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting notification: " + e.getMessage());
        }
    }

    /**
     * Get unread notifications for a user
     */
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@RequestParam String userId) {
        logger.info("Received request to get unread notifications for user: {}", userId);
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    /**
     * Count unread notifications for a user
     */
    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Long>> countUnreadNotifications(@RequestParam String userId) {
        logger.info("Received request to count unread notifications for user: {}", userId);
        
        long count = notificationService.countUnreadNotifications(userId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get notifications by type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Notification>> getNotificationsByType(@PathVariable String type) {
        logger.info("Received request to get notifications with type: {}", type);
        return ResponseEntity.ok(notificationService.getNotificationsByType(type));
    }

    /**
     * Mark all notifications as read for a user
     */
    @PatchMapping("/mark-all-read")
    public ResponseEntity<Map<String, String>> markAllNotificationsAsRead(@RequestParam String userId) {
        try {
            logger.info("Received request to mark all notifications as read for user: {}", userId);
            
            notificationService.markAllNotificationsAsRead(userId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "All notifications marked as read");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error marking all notifications as read: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error marking all notifications as read: " + e.getMessage()));
        }
    }
}
