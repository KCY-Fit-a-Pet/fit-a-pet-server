package kr.co.fitapet.common.execption;

public interface BaseErrorCode {
    CausedBy causedBy();
    String getExplainError() throws NoSuchFieldError;
}
