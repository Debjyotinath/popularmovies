package com.udacity.popularmovies.model;

/**
 * Created by debjyotinath on 11/02/17.
 */

public enum LanguageEnum {

    ENGLISH("en","English"),
    JAPANESE("ja","Japanese"),
    ITALIAN("it","Italian"),
    FRENCH("fr","French");
    String code;
    String name;


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    LanguageEnum(String code, String name) {
        this.name = name;
        this.code=code;
    }

    public static LanguageEnum findFromCode(String code)
    {
        switch (code)
        {
            case "en":
                return ENGLISH;
            case "it":
                return ITALIAN;
            case "fr":
                return FRENCH;
            case "ja":
                return JAPANESE;
            default:
                return ENGLISH;
        }
    }
}
