package com.bav.testproject.basket;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

//Продукт в корзине
@Document(collection = "basket")
public class ProductOnBasket {

    //id
    @Id
    private long id;

    //id пользователя
    @Field(value = "userId")
    private long userId;

    //id продукта
    @Field(value = "productId")
    private long productId;

    @Override
    public String toString() {
        return "ProductOnBasket{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                '}';
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
