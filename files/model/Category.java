package com.orimex.orimex.model;

/**
 * Created by Mi sesión on 15/11/2016.
 */

public class Category {

    private String id;
    private String title;
    private String iconUrl;
    private String imageUrl;
    private String item_type;

    public Category() {

    }

    /**
     * @brief methods for getting category id
     * @return id in String
     */
    public String getId() {
        return id;
    }

    /**
     * @brief methods for setting category id
     * @param id in String
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @brief methods for getting title of category
     * @return title in String
     */
    public String getTitle() {
        return title;
    }

    /**
     * @brief methods for setting category title
     * @return title in String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @brief methods for getting imageUrl of category
     * @return imageUrl in String
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @brief methods for setting category imageUrl
     * @return imageUrl in String
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * @brief methods for getting iconUrl of category
     * @return iconUrl in String
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * @brief methods for setting category iconUrl
     * @return iconUrl in String
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }
}

