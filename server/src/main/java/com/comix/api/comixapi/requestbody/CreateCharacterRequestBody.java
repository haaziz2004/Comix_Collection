package com.comix.api.comixapi.requestbody;

public class CreateCharacterRequestBody {
    private String name;

    public CreateCharacterRequestBody() {
    }

    public CreateCharacterRequestBody(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }
}
