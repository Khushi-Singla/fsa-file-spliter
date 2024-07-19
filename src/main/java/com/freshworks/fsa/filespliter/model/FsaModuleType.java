package com.freshworks.fsa.filespliter.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FsaModuleType {
    CONTACT("Contact"),
    SALES_ACCOUNT("SalesAccount");

    private final String description;

    FsaModuleType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }

    @JsonValue
    public String value() {
        return this.description;
    }
}
