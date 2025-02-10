package FlyAway.payment;

import FlyAway.reservation.ReservationService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentWebhookService {

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentWebhookService.class);

    private final ReservationService reservationService;

    public PaymentWebhookService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public void handleStripeWebhook(String payload, String sigHeader) throws SignatureVerificationException, MessagingException {
        Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        String eventType = event.getType();
        LOGGER.info("Received event: {}", eventType);

        if ("checkout.session.completed".equals(eventType) || "checkout.session.expired".equals(eventType)) {
            handlePaymentEvent(event, eventType);
        } else {
            LOGGER.warn("Unhandled event type: {}", eventType);
        }

    }

    private void handlePaymentEvent(Event event, String eventType) throws MessagingException {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        Optional<StripeObject> optionalObject = deserializer.getObject();

        if (optionalObject.isEmpty()) {
            LOGGER.error("Failed to deserialize {} event", eventType);
            return;
        }

        StripeObject stripeObject = optionalObject.get();
        String reservationId = null;

        if (stripeObject instanceof Session session) {
            if (session.getMetadata() == null || !session.getMetadata().containsKey("reservation_id")) {
                LOGGER.warn("{} event received with missing reservation_id. Session ID: {}", eventType, session.getId());
                return;
            }

            reservationId = session.getMetadata().get("reservation_id");

            if ("checkout.session.completed".equals(eventType)) {
                LOGGER.info("Payment successful for reservation ID: {}", reservationId);
                reservationService.handlePaymentCompleted(reservationId);
            } else if ("checkout.session.expired".equals(eventType)) {
                LOGGER.info("Payment session expired for reservation ID: {}", reservationId);
                reservationService.handlePaymentExpired(reservationId);
            }
        } else {
            LOGGER.error("Unexpected object type for event {}: {}", eventType, stripeObject.getClass().getSimpleName());
        }
    }
}
