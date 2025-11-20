package vnua.fita.tthieu.springboot.vnpay;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VNPayUtil {

    public static String hashAllFields(Map<String, String> fields, String secretKey) throws Exception {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        for (String name : fieldNames) {
            String value = fields.get(name);
            if (value != null && value.length() > 0) {
                sb.append(name).append("=").append(value);
                if (!name.equals(fieldNames.get(fieldNames.size() - 1))) {
                    sb.append("&");
                }
            }
        }
        return hmacSHA512(secretKey, sb.toString());
    }

    public static String hmacSHA512(String key, String data) throws Exception {
        javax.crypto.Mac hmac512 = javax.crypto.Mac.getInstance("HmacSHA512");
        javax.crypto.spec.SecretKeySpec secretKey = new javax.crypto.spec.SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac512.init(secretKey);
        byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hash = new StringBuilder();
        for (byte b : bytes) {
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }

    public static String getPaymentURL(String baseUrl, Map<String, String> params, String secretKey) throws Exception {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder query = new StringBuilder();
        for (String name : fieldNames) {
            String value = URLEncoder.encode(params.get(name), StandardCharsets.US_ASCII.toString());
            query.append(name).append("=").append(value);
            if (!name.equals(fieldNames.get(fieldNames.size() - 1))) {
                query.append("&");
            }
        }
        String hashData = hashAllFields(params, secretKey);
        return baseUrl + "?" + query.toString() + "&vnp_SecureHash=" + hashData;
    }
}
