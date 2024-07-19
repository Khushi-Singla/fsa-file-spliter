package com.freshworks.fsa.filespliter.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum FsaModuleFieldType {
    TEXT(1, "text"),
    DROP_DOWN(2, "dropdown"),
    EMAIL(3, "email"),
    PHONE_NUMBER(4, "phone_number"),
    CHECKBOX(5, "checkbox"),
    PARAGRAPH(6, "paragraph"),
    DATE_TIME(7, "date_time"),
    NUMBER(8, "number"),
    URL_SET(9, "url_set"),
    URL(10, "url"),
    ATTACHMENT(11, "attachment"),
    RADIO(12, "radio"),
    DECIMAL(13, "decimal"),
    SECTION(14, "section"),
    DEPENDENT_DROPDOWN(15, "dependent_dropdown"),
    AUTO_COMPLETE(16, "auto_complete"),
    DATE(17, "date"),
    MULTI_SELECT_DROPDOWN(18, "multi_select_dropdown"),
    DATE_TIME_SPLIT(19, "date_time_split"),
    BIG_NUMBER(20, "big_number"),
    GROUP_FIELD(21, "group_field");

    private final String description;

    private final int numericType;

    private static final ImmutableMap<Integer, FsaModuleFieldType> reverseLookup =
            Maps.uniqueIndex(Arrays.asList(FsaModuleFieldType.values()), FsaModuleFieldType::getNumericType);
    private static final ImmutableMap<String, FsaModuleFieldType> valueLookup =
            Maps.uniqueIndex(Arrays.asList(FsaModuleFieldType.values()), FsaModuleFieldType::getDescription);

    FsaModuleFieldType(int numericType, String value) {
        this.numericType = numericType;
        this.description = value;
    }

    public static FsaModuleFieldType getById(Integer id) {
        return reverseLookup.get(id);
    }

    public static FsaModuleFieldType getByName(String name) {
        return valueLookup.get(name);
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
