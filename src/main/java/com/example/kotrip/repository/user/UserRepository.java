package com.example.kotrip.repository.user;

import com.example.kotrip.entity.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByKakaoUserId(String kakaoUserId);
    Optional<User> findUserByNickname(String username);
}
