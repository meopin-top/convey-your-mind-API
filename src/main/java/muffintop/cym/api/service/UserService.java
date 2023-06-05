package muffintop.cym.api.service;

import lombok.RequiredArgsConstructor;
import muffintop.cym.api.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

}
