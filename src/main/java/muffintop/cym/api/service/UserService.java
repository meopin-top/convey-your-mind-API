package muffintop.cym.api.service;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.controller.request.SignInRequest;
import muffintop.cym.api.controller.request.SignUpRequest;
import muffintop.cym.api.domain.User;
import muffintop.cym.api.domain.enums.AuthMethod;
import muffintop.cym.api.domain.enums.UserStatus;
import muffintop.cym.api.domain.key.UserPk;
import muffintop.cym.api.exception.DuplicatedIdException;
import muffintop.cym.api.exception.IncorrectPasswordException;
import muffintop.cym.api.exception.InvalidFormatEmailException;
import muffintop.cym.api.exception.InvalidFormatIdException;
import muffintop.cym.api.exception.InvalidFormatPasswordException;
import muffintop.cym.api.exception.InvalidPasswordException;
import muffintop.cym.api.exception.NonExistIdException;
import muffintop.cym.api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WebClient webClient;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]*[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}$";
    private static final String ID_PATTERN = "^[a-zA-Z0-9!@#$%^&*()-_=+{}\\[\\]|\\\\;:'\",.<>/?]{6,20}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";


    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern idPattern = Pattern.compile(ID_PATTERN);
    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);

    private boolean isValidUserName(UserPk userPk) {
        return !userRepository.existsById(userPk);
    }


    //여기 서버 내려감감
   private String makeNickname() {

        final String path = "/";
        final String format = "text";
        final String count = "1";

        try{
            Mono<String> response = webClient.get().uri(uriBuilder ->
                uriBuilder
                    .path(path)
                    .queryParam("format", format)
                    .queryParam("count", count)
                    .build()).retrieve().bodyToMono(String.class);

            return response.block();
        } catch (Exception e){
            return "";
        }
    }

    private boolean isSamePassword(String password, String passwordCheck) {
        return password.equals(passwordCheck);
    }

    private boolean isValidPassword(String password) {
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.matches();
    }

    private boolean isValidId(String id) {
        Matcher matcher = idPattern.matcher(id);
        return matcher.matches();
    }

    private boolean isValidEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    @Transactional
    public User signUp(SignUpRequest request) {

        //User PK 생성
        UserPk userPk = new UserPk(request.getUserId(), AuthMethod.EMAIL.getValue());

        //Id 포맷 확인
        if(!isValidId(request.getUserId())){
            throw new InvalidFormatIdException();
        }
        //User 중복 확인
        if (!isValidUserName(userPk)) {
            throw new DuplicatedIdException();
        }

        //패스워드 포맷 확인
        if (!isValidPassword(request.getPassword())) {
            throw new InvalidFormatPasswordException();
        }

        //패스워드 일치 여부 확인
        if (!isSamePassword(request.getPassword(), request.getPasswordCheck())) {
            throw new IncorrectPasswordException();
        }

        //이메일 포맷 확인
        if(request.getEmail()!=null&&!isValidEmail(request.getEmail())){
            throw new InvalidFormatEmailException();
        }

        User newUser = User.builder()
            .userId(request.getUserId())
            .email(request.getEmail())
            .authMethod(AuthMethod.EMAIL.getValue())
            .nickName(makeNickname())
            .profile(null)
            .password(passwordEncoder.encode(request.getPassword()))
            .createdDatetime(LocalDateTime.now())
            .status(UserStatus.UNAUTHORIZED.getValue())
            .build();

        userRepository.save(newUser);

        return userRepository.save(newUser);

    }

    public User signIn(SignInRequest request) {
        UserPk userPk = new UserPk(request.getUserId(), AuthMethod.EMAIL.getValue());
        //해당 유저 찾기
        User user = getUserByUserPk(userPk);
        if (user == null) {
            throw new NonExistIdException();
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }
        return user;
    }

    public User getUserByUserPk(UserPk userPk) {
        return userRepository.findUserByUserIdAndAuthMethod(userPk.getUserId(),
            userPk.getAuthMethod()).orElse(null);
    }
}
