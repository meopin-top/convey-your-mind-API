package muffintop.cym.api.domain.enums;

public enum AuthMethod {
    EMAIL('E'),
    KAKAO('K');

    public char getValue() {
        return value;
    }

    private final char value;

    AuthMethod(char value) {
        this.value = value;
    }
}
