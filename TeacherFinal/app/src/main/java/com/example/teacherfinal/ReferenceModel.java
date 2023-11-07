package com.example.teacherfinal;

public class ReferenceModel {

    private String title;
    private String code;

    public ReferenceModel()
    {

    }

    public ReferenceModel(String title, String code)
    {
        this.title=title;
        this.code=code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
