package muffintop.cym.api.service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.controller.request.SignInRequest;
import muffintop.cym.api.controller.request.SignUpRequest;
import muffintop.cym.api.controller.request.UserUpdateRequest;
import muffintop.cym.api.domain.User;
import muffintop.cym.api.domain.enums.AuthMethod;
import muffintop.cym.api.domain.enums.UserStatus;
import muffintop.cym.api.domain.key.UserPk;
import muffintop.cym.api.exception.DuplicatedIdException;
import muffintop.cym.api.exception.ExistingEmailCodeException;
import muffintop.cym.api.exception.IncorrectPasswordException;
import muffintop.cym.api.exception.InvalidFormatEmailException;
import muffintop.cym.api.exception.InvalidFormatIdException;
import muffintop.cym.api.exception.InvalidFormatPasswordException;
import muffintop.cym.api.exception.InvalidPasswordException;
import muffintop.cym.api.exception.NonExistIdException;
import muffintop.cym.api.repository.UserRepository;
import muffintop.cym.api.service.dto.kakao.KakaoUser;
import muffintop.cym.api.service.dto.naver.NaverUser;
import muffintop.cym.api.service.dto.naver.NaverUserInfo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;
    private final WebClient webClient;

    private String EMAIL_PATTERN = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]*[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}$";

    private String ID_PATTERN = "^[a-zA-Z0-9!@#$%^&*()-_=+{}\\[\\]|\\\\;:'\",.<>/?]{6,20}$";

    private String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";


    private Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private Pattern idPattern = Pattern.compile(ID_PATTERN);
    private Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);

    private boolean isValidUserName(UserPk userPk) {
        return !userRepository.existsById(userPk);
    }


    //여기 서버 내려감감
    public String makeNickname() {

        final String path = "/";
        final String format = "text";
        final String count = "1";

        try {
            Mono<String> response = webClient.get().uri(uriBuilder ->
                uriBuilder
                    .path(path)
                    .queryParam("format", format)
                    .queryParam("count", count)
                    .build()).retrieve().bodyToMono(String.class);

            return response.block();
        } catch (Exception e) {
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
        if (!isValidId(request.getUserId())) {
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
        if (request.getEmail() != null && !isValidEmail(request.getEmail())) {
            throw new InvalidFormatEmailException();
        }

        User newUser = User.builder()
            .id(request.getUserId())
            .email(request.getEmail())
            .authMethod(AuthMethod.EMAIL.getValue())
            .nickName(makeNickname())
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
        return userRepository.findUserByIdAndAuthMethod(userPk.getId(),
            userPk.getAuthMethod()).orElse(null);
    }

    public User signInWithKakao(KakaoUser kakaoUser) {
        User user = userRepository.findUserByIdAndAuthMethod(kakaoUser.getId().toString(),
            AuthMethod.KAKAO.getValue()).orElse(null);

        //회원 가입 진행
        if (user == null) {
            User newUser = User.builder()
                .id(kakaoUser.getId().toString())
                .email(kakaoUser.getKakaoAccount().getEmail())
                .authMethod(AuthMethod.KAKAO.getValue())
                .nickName(kakaoUser.getKakaoAccount().getKakaoProfile().getNickname())
                .profile(kakaoUser.getKakaoAccount().getKakaoProfile().getProfileImageUrl())
                .password(passwordEncoder.encode(kakaoUser.getId().toString()))
                .createdDatetime(LocalDateTime.now())
                .status(UserStatus.UNAUTHORIZED.getValue())
                .build();
            user = newUser;
        } else {
            //프로필하고 닉네임 업데이트
            user.setProfile(kakaoUser.getKakaoAccount().getKakaoProfile().getProfileImageUrl());
            user.setNickName(kakaoUser.getKakaoAccount().getKakaoProfile().getNickname());
        }
        userRepository.save(user);
        return user;
    }

    public User signInWithNaver(NaverUser naverUser) {
        NaverUserInfo userInfo = naverUser.getResponse();
        User user = userRepository.findUserByIdAndAuthMethod(userInfo.getId(),
            AuthMethod.NAVER.getValue()).orElse(null);

        //회원 가입 진행
        if (user == null) {
            User newUser = User.builder()
                .id(userInfo.getId())
                .email(userInfo.getEmail())
                .authMethod(AuthMethod.NAVER.getValue())
                .nickName(userInfo.getNickName())
                .profile(userInfo.getProfileImage())
                .password(passwordEncoder.encode(userInfo.getId()))
                .createdDatetime(LocalDateTime.now())
                .status(UserStatus.UNAUTHORIZED.getValue())
                .build();
            user = newUser;
        } else {
            //프로필하고 닉네임 업데이트
            user.setProfile(userInfo.getProfileImage());
            user.setNickName(userInfo.getNickName());
        }
        userRepository.save(user);
        return user;
    }

    private void updatePassword(User user, String password) {
        User basicUser = userRepository.findUserByIdAndAuthMethod(user.getId(),
            user.getAuthMethod()).orElseThrow(NonExistIdException::new);
        basicUser.setPassword(passwordEncoder.encode(password));
    }

    private void updateEmail(User user, String email) {
        // TO DO :이메일 전송 추가 필요
        User basicUser = userRepository.findUserByIdAndAuthMethod(user.getId(),
            user.getAuthMethod()).orElseThrow(NonExistIdException::new);
        if (userRepository.existsByEmailAndAuthMethod(email, user.getAuthMethod())) {
            throw new ExistingEmailCodeException();
        }
        basicUser.setEmail(email);
    }

    private void updateNickname(User user, String nickName) {
        User basicUser = userRepository.findUserByIdAndAuthMethod(user.getId(),
            user.getAuthMethod()).orElseThrow(NonExistIdException::new);
        basicUser.setNickName(nickName);
    }

    private void updateProfile(User user, MultipartFile profile) {
        String uuid = UUID.randomUUID().toString();
        String url = fileService.makeUrl(uuid);
        fileService.upload(profile, uuid);
        User basicUser = userRepository.findUserByIdAndAuthMethod(user.getId(),
            user.getAuthMethod()).orElseThrow(NonExistIdException::new);
        basicUser.setProfile(url);

    }

    @Transactional
    public void updateUser(User user, UserUpdateRequest request, MultipartFile profile) {
        if (request.getEmail() != null) {
            updateEmail(user, request.getEmail());
        }
        if (request.getPassword() != null) {
            updatePassword(user, request.getPassword());
        }
        if (request.getNickname() != null) {
            updateNickname(user, request.getNickname());
        }
        if (profile != null) {
            updateProfile(user, profile);
        }
    }
}
