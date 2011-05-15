package com.pwned.aggrogamer;

public class NewsItem {
	   
    private String orderName;
    private String orderStatus;
    private String category;
    private String link;
   
    public String getTitle() {
        return orderName;
    }
    public void setTitle(String orderName) {
        this.orderName = orderName;
    }
    public String getDescription() {
        return orderStatus;
    }
    public void setDescription(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getCategory() {
    	return category;
    }
    public void setCategory(String im) {
    	this.category = im;
    }
    
    public String getLink(){
    	return link;
    }
    
    public void setLink(String link){
    	this.link = link;
    }
}