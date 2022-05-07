package com.example.elearningptit.model;

import java.util.List;

public class CreditClassDetailDTO {
    private List<PostDTO> listPost;

    public List<PostDTO> getListPost() {
        return listPost;
    }

    public void setListPost(List<PostDTO> listPost) {
        this.listPost = listPost;
    }

    public CreditClassDetailDTO() {
    }

    public CreditClassDetailDTO(List<PostDTO> listPost) {
        this.listPost = listPost;
    }
}
