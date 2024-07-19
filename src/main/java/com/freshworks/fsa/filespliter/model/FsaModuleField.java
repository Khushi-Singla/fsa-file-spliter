package com.freshworks.fsa.filespliter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FsaModuleField {
    @JsonProperty("field_id")
    private String id;

    @JsonProperty("field_name")
    private String name;

    @JsonProperty("field_label")
    private String label;

    @JsonProperty("field_type")
    private FsaModuleFieldType fieldType;

    @JsonProperty("meta")
    private FieldMeta fieldMeta;

    @JsonProperty("entity")
    private FsaModuleType type;
}
