package com.dmall.blas.collect.core;

public class Blog {
    private Integer id;
    private String title;
    private String posttime;
    private String content;
    private String updatetime;

    public Blog() {
    }

    public Blog(Integer id, String title, String posttime, String content, String updatetime) {
        this.id = id;
        this.title = title;
        this.posttime = posttime;
        this.content = content;
        this.updatetime = updatetime;
    }

    public Integer getId() {
        return id;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}