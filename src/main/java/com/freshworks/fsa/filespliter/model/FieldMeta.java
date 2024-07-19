package com.freshworks.fsa.filespliter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FieldMeta {
    @JsonProperty("field_type")
    private FsaModuleFieldType fieldType;
}
