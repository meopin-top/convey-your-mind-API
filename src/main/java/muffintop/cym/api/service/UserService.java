package muffintop.cym.api.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.controller.enums.ResponseCode;
import muffintop.cym.api.controller.request.SignUpRequest;
import muffintop.cym.api.domain.User;
import muffintop.cym.api.domain.key.UserPk;
import muffintop.cym.api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private boolean isValidUserName(UserPk userPk) {
        return !userRepository.existsById(userPk);
    }

    private boolean isValidPassword(String password, String passwordCheck) {
        return password.equals(passwordCheck);
    }

    @Transactional
    public ResponseCode signUp(SignUpRequest request){

        UserPk userPk = new UserPk(request.getUserId(),'D');
        if(!isValidUserName(userPk)){
            return ResponseCode.DUPLICATED_ID;
        }
        if(!isValidPassword(request.getPassword(), request.getPasswordCheck())){
            return ResponseCode.INCORRECT_PASSWORD;
        }

        User newUser = User.builder()
            .userId(request.getUserId())
            .nickName(request.getNickName())
            .authMethod('D')
            .password(passwordEncoder.encode(request.getPassword()))
            .createdDatetime(LocalDateTime.now())
            .build();
        userRepository.save(newUser);

        return ResponseCode.SIGN_UP_SUCCESS;

    }
}
