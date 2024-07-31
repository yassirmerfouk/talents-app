package com.pulse.enumeration;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    CONFIRM_ACCOUNT("confirm_account"),
    RESET_PASSWORD("reset_password"),
    SUGGESTIONS("suggestions"),
    MEET("meet");

    private final String name;

    EmailTemplateName(String name){
        this.name = name;
    }
}
