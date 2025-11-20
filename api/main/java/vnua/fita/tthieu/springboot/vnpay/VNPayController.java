package vnua.fita.tthieu.springboot.vnpay;

import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:5173") // Cho phép frontend Vue truy cập
public class VNPayController {

    private final VNPayService vnPayService;

    public VNPayController(VNPayService vnPayService) {
        this.vnPayService = vnPayService;
    }

    // API tạo URL thanh toán
    @PostMapping("/create")
    public Map<String, Object> createPayment(@RequestBody Map<String, Object> requestData) throws Exception {
        long amount = Long.parseLong(requestData.get("amount").toString());
        String bankCode = requestData.getOrDefault("bankCode", "NCB").toString();

        String paymentUrl = vnPayService.createPaymentUrl(amount, bankCode);
        return Map.of("paymentUrl", paymentUrl);
    }

    //  Callback từ VNPAY (sau khi nhập OTP) → redirect sang trang FE /payment-return
    @GetMapping("/payment-return")
    public void paymentReturn(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        // URL frontend hiển thị kết quả thanh toán
        String redirectUrl = "http://localhost:5173/payment-return";

        // Encode toàn bộ query params để tránh lỗi ký tự đặc biệt (ví dụ dấu + hoặc & trong vnp_OrderInfo)
        String queryString = params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String finalUrl = redirectUrl + "?" + queryString;

        // In log để kiểm tra trong console Spring Boot
        System.out.println("🔁 Redirecting to FE: " + finalUrl);

        // Thực hiện chuyển hướng về frontend
        response.sendRedirect(finalUrl);
    }
}
