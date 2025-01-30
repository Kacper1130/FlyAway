package FlyAway.payment;

import com.stripe.exception.SignatureVerificationException;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentWebhookService paymentWebhookService;

    public PaymentController(PaymentWebhookService paymentWebhookService) {
        this.paymentWebhookService = paymentWebhookService;
    }


    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) throws MessagingException  {

        if (sigHeader == null) {
            LOGGER.info("Webhook received without Stripe-Signature header");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing Stripe-Signature header");
        }

        try {
            paymentWebhookService.handleStripeWebhook(payload, sigHeader);
            return ResponseEntity.ok("Event processed");
        } catch (SignatureVerificationException e) {
            LOGGER.warn("Invalid Stripe signature: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Stripe signature");
        }
    }

}
