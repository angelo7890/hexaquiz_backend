package com.hexaquiz.enums;

public enum QuestionTypeEnum {
    MULTIPLE_CHOICE(1),
    TRUE_FALSE(2),
    GUESS_THE_WORD(3),
    WORDLE(4),
    ORDERING(5);
    private final int codigo;

    QuestionTypeEnum(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;

    }
    }