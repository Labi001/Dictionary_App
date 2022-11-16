package com.labinot.dictionary.model;

public class History {

    private final String en_word;
    private final String definition;

    public History(String en_word, String definition) {
        this.en_word = en_word;
        this.definition = definition;
    }

    public String getEn_word() {
        return en_word;
    }

    public String getDefinition() {
        return definition;
    }


}
