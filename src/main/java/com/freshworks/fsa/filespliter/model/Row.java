package com.freshworks.fsa.filespliter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Row {
    private List<String> values;

    public Row() {
        this.setValues(new ArrayList<>());
    }

    public void addValue(String value) {
        this.values.add(value);
    }

    @Override
    public String toString() {
        return String.join(",", values);
    }
}
