package com.freshworks.fsa.filespliter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class DocumentField {
    @JsonProperty("column_name")
    private String name;

    @JsonProperty("suggested_mapping")
    private List<FsaModuleField> fsFields;

    public void addFsField(FsaModuleField fsField) {
        if (this.fsFields == null) {
            this.fsFields = new ArrayList<>();
        }
        this.fsFields.add(fsField);
    }
}
