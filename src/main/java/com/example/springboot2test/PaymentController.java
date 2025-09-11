package com.example.springboot2test;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/user/payment")
public class PaymentController {

    @Autowired
    private RazorpayConfig razorpayConfig;

   @PostMapping("/order")
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        try {
            int amount = Integer.parseInt(data.get("amount").toString());
            int amountInPaise = amount * 100;

            RazorpayClient client = new RazorpayClient(
                    razorpayConfig.getKeyId(),
                    razorpayConfig.getKeySecret()
            );

            JSONObject options = new JSONObject();
            options.put("amount", amountInPaise); 
            options.put("currency", "INR");
            options.put("payment_capture", 1); 

            Order order = client.orders.create(options);

            response.put("id", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("status", "created");

        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "failed");
            response.put("message", e.getMessage());
        }
        return response;
    }
}
