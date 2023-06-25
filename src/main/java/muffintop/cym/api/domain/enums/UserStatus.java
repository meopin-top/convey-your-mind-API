package muffintop.cym.api.domain.enums;

public enum UserStatus {
    AUTHORIZED('A'),
    UNAUTHORIZED('U'),
    EXPIRED('E');

    public char getValue() {
        return value;
    }

    private final char value;

    UserStatus(char value) {
        this.value = value;
    }
}
