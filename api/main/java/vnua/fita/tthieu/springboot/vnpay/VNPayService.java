package vnua.fita.tthieu.springboot.vnpay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayService {

    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.payUrl}")
    private String vnp_Url;

    @Value("${vnpay.returnUrl}")
    private String vnp_ReturnUrl;

    public String createPaymentUrl(long amount, String bankCode) throws Exception {
        // ✅ Tạo map tham số theo chuẩn tài liệu
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // nhân 100 vì đơn vị VNPAY là đồng *100
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }

        vnp_Params.put("vnp_TxnRef", String.valueOf(System.currentTimeMillis()));
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang #" + System.currentTimeMillis());
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh")); // ✅ CHUẨN GIỜ VIỆT NAM

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));

        cld.add(Calendar.MINUTE, 15); // hạn 15 phút
        vnp_Params.put("vnp_ExpireDate", formatter.format(cld.getTime()));

        // ✅ Sắp xếp key theo thứ tự A-Z trước khi ký
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String name : fieldNames) {
            String value = vnp_Params.get(name);
            if (value != null && !value.isEmpty()) {
                hashData.append(name).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
                query.append(URLEncoder.encode(name, StandardCharsets.US_ASCII))
                     .append('=')
                     .append(URLEncoder.encode(value, StandardCharsets.US_ASCII))
                     .append('&');
            }
        }

        // Xóa ký tự '&' cuối cùng
        hashData.setLength(hashData.length() - 1);
        query.setLength(query.length() - 1);

        // Tạo chữ ký SHA512
        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        // Trả về URL thanh toán hoàn chỉnh
        return vnp_Url + "?" + query.toString();
    }

    // Hàm ký SHA512
    private String hmacSHA512(String key, String data) throws Exception {
        Mac hmac512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac512.init(secretKey);
        byte[] hashBytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();
        for (byte b : hashBytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
