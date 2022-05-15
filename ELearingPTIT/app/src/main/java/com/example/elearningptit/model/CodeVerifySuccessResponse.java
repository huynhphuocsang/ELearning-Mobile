package com.example.elearningptit.model;

public class CodeVerifySuccessResponse {
    String valueKey ;
    String codeValue;
    public CodeVerifySuccessResponse() {
    }

    public CodeVerifySuccessResponse(String valueKey, String key2) {
        this.valueKey = valueKey;
        this.codeValue = key2;
    }

    public String getValueKey() {
        return valueKey;
    }

    public void setValueKey(String valueKey) {
        this.valueKey = valueKey;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }
}
