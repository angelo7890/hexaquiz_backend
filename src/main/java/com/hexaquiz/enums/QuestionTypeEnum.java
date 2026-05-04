package com.hexaquiz.enums;

public enum QuestionTypeEnum {
    MULTIPLE_CHOICE(1),
    TRUE_FALSE(2),
    GUESS_THE_WORD(3),
    WORDLE(4),
    ORDERING(5);
    private final int code;

    QuestionTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    public static QuestionTypeEnum fromCode(int code) {
        for (QuestionTypeEnum type : QuestionTypeEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Código de tipo de questão inválido: " + code);
    }
    }