package vnua.fita.tthieu.springboot;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CheckPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String encoded = encoder.encode("Hoan78@Q122222232");
        System.out.println(encoded);
    }
}
