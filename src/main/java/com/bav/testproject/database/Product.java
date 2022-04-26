package com.bav.testproject.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;

//Продукты магазина
@Document(collection = "products")
public class Product {

    //id
    @Id
    private long id;

    //название товара
    @Field(value = "title")
    private String title;

    //Информация о товаре
    @Field(value = "info")
    private String info;

    //Характеристики продукта
    @Field(value = "characteristics")
    private String[] characteristics;

    //Цена продукта
    @Field(value = "cost")
    private String cost;

    //Теги (для поиска)
    @Field(value = "tags")
    private String[] tags;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", info='" + info + '\'' +
                ", characteristics=" + Arrays.toString(characteristics) +
                ", cost='" + cost + '\'' +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }

    public String getCharacteristicsString(){
        return Arrays.toString(characteristics);
    }

    public String getTagsString(){
        return Arrays.toString(tags);
    }

    public String getTitleAndCharacteristics(){
        return title + " " + Arrays.toString(characteristics);
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }

    public String[] getCharacteristics() {
        return characteristics;
    }
    public void setCharacteristics(String[] characteristics) {
        this.characteristics = characteristics;
    }

    public String getCost() {
        return cost;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }

    public String[] getTags() {
        return tags;
    }
    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
