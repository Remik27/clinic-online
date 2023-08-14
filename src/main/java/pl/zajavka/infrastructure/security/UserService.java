package pl.zajavka.infrastructure.security;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public Integer getUserId(String username) {
    return userRepository.findByUserName(username).getId();
    }
}
