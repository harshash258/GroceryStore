package com.mycompany.grocerystore.Models;

public class ProductInfo {
    private String product_name;
    private String product_price;
    private String product_description;
    private String image_url;
    private String productID;


    public ProductInfo(String product_name, String product_price, String product_description, String image_url, String productID) {
        if(product_name.trim().equals("")){
            product_name =  "New Product";
        }
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_description = product_description;
        this.image_url = image_url;
        this.productID = productID;
    }

    public ProductInfo() {}

    public String getProduct_name() {

        return product_name;
    }
    public String getProductID() {
        return productID;
    }

    public String getProduct_price() {
        return product_price;
    }

    public String getProduct_description() {
        return product_description;
    }

    public String getImage_url() {
        return image_url;
    }

}


