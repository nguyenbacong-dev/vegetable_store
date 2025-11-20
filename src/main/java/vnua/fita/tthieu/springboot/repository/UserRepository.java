package vnua.fita.tthieu.springboot.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vnua.fita.tthieu.springboot.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Tìm user theo email (dùng khi đăng nhập)
    Optional<User> findByEmail(String email);

    // Kiểm tra xem email đã tồn tại chưa
    boolean existsByEmail(String email);
}

