package vnua.fita.tthieu.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import vnua.fita.tthieu.springboot.dto.LoginRequest;
import vnua.fita.tthieu.springboot.dto.RegisterRequest;
import vnua.fita.tthieu.springboot.entity.User;
import vnua.fita.tthieu.springboot.security.JwtTokenProvider;
import vnua.fita.tthieu.springboot.service.UserService;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ===========================
    // 🔹 LOGIN
    // ===========================
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = tokenProvider.generateToken(userDetails);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("email", user.getEmail());
        response.put("fullname", user.getFullname());
        response.put("id", user.getId());
        response.put("roles", roles);

        return ResponseEntity.ok(response);
    }

    // ===========================
    // 🔹 REGISTER
    // ===========================
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("❌ Email đã tồn tại!");
        }

        User user = new User();
        user.setFullname(registerRequest.getFullname());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setAdress(registerRequest.getAdress());
        user.setPhone(registerRequest.getPhone());
        user.setRole("ROLE_USER"); // gán mặc định ROLE_USER

        userService.createUser(user);
        return ResponseEntity.ok("Đăng ký thành công!");
    }

    // ===========================
    // 🔹 LẤY TOÀN BỘ USER
    // ===========================
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // ===========================
    // 🔹 LẤY USER THEO ID
    // ===========================
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ===========================
    // 🔹 CẬP NHẬT USER
    // ===========================
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.getUserById(id).map(user -> {
            user.setFullname(updatedUser.getFullname());
            user.setEmail(updatedUser.getEmail());
            user.setAdress(updatedUser.getAdress());
            user.setPhone(updatedUser.getPhone());
            user.setRole(updatedUser.getRole());
            // chỉ đổi mật khẩu nếu có nhập mới
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            userService.createUser(user);
            return ResponseEntity.ok("Cập nhật thành công!");
        }).orElse(ResponseEntity.notFound().build());
    }

    // ===========================
    // 🔹 XÓA USER
    // ===========================
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);
        return ResponseEntity.ok("Đã xóa user có ID = " + id);
    }
}
