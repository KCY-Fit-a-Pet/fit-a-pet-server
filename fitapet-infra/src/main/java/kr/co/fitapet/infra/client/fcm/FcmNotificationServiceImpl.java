package kr.co.fitapet.infra.client.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmNotificationServiceImpl implements NotificationService {
    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void sendNotification(NotificationRequest request) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setToken(request.token())
                .setNotification(request.toNotification())
                .build();

        firebaseMessaging.send(message);
    }
}
