package com.bluestream.app;

public class SearchListModel {
    private String head;
    private String desc;
    private String PostDesc;
    private String PostImageUrl;
    private String PostDate;

    public String getHead() {
        return head;
    }
    public String getDesc() {
        return desc;
    }
    public String getPostDesc() {
        return PostDesc;
    }
    public String getPostImageUrl() {
        return PostImageUrl;
    }

    public String getPostDate() {
        return PostDate;
    }

    public SearchListModel(String head, String desc, String PostDesc,String PostImageUrl, String PostDate) {
        this.head = head;
        this.desc = desc;
        this.PostDesc = PostDesc;
        this.PostImageUrl = PostImageUrl;
        this.PostDate = PostDate;
    }

}
