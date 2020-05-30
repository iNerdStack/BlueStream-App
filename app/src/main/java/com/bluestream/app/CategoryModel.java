package com.bluestream.app;

public class CategoryModel
{
    private String CategoryName;
    private String CategoryImageUrl;
    private String CategoryId;


    public String getCategoryImageUrl() {
    return CategoryImageUrl;
}
    public String getCategoryName() {
        return CategoryName;
    }
    public String getCategoryId() {
        return CategoryId;
    }
    public CategoryModel(String CategoryName, String CategoryImageUrl, String CategoryId) {
        this.CategoryName = CategoryName;
        this.CategoryImageUrl = CategoryImageUrl;
        this.CategoryId = CategoryId;
    }

}
