package kr.co.fitapet.common.execption;

public record CausedBy(
        int code,
        String name,
        String message
) {
    public static CausedBy of(int code, String name, String message) {
        return new CausedBy(code, name, message);
    }
}
