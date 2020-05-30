package com.bluestream.app;

public class HomeListModel {
    private String head;
    private String desc;
    private String PostDesc;
    private String PostImageUrl;
    private String PostDate;
    private String PostId;
    private String PostSlug;

    public  String getPostId()
    {
        return PostId;
    }



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
    public String getPostSlug() {
        return PostSlug;
    }

    public String getPostDate() {
        return PostDate;
    }

    public HomeListModel(String head, String desc, String PostDesc,String PostImageUrl, String PostDate, String PostId, String PostSlug) {
        this.head = head;
        this.desc = desc;
        this.PostDesc = PostDesc;
        this.PostImageUrl = PostImageUrl;
        this.PostDate = PostDate;
        this.PostId = PostId;
        this.PostSlug = PostSlug;
    }

}
