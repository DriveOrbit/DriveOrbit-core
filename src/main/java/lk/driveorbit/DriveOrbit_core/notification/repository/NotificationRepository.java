package lk.driveorbit.DriveOrbit_core.notification.repository;

import lk.driveorbit.DriveOrbit_core.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    // Find notifications by user ID
    List<Notification> findByUserId(String userId);
    
    // Find notifications by user ID and read status
    List<Notification> findByUserIdAndIsRead(String userId, Boolean isRead);
    
    // Find notifications by type
    List<Notification> findByType(String type);
    
    // Count unread notifications for a user
    long countByUserIdAndIsRead(String userId, Boolean isRead);
    
    // Find notifications ordered by timestamp (newest first)
    @Query("SELECT n FROM Notification n WHERE n.userId = ?1 ORDER BY n.timestamp DESC")
    List<Notification> findByUserIdOrderByTimestampDesc(String userId);
    
    // Find latest notifications for a user (limited to a certain number)
    @Query(value = "SELECT * FROM notifications WHERE user_id = ?1 ORDER BY timestamp DESC LIMIT ?2", 
           nativeQuery = true)
    List<Notification> findLatestNotifications(String userId, int limit);
}
