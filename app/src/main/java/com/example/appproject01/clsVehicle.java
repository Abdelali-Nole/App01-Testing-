package com.example.appproject01;

public class clsVehicle {


    private int id;
    private String model;
    private String color;
    private Double dpl;
    private String image;
    private String description;

    public clsVehicle( int id,  String model, String color,Double dpl,String image,String descr) {
        this.model = model;
        this.id = id;
        this.color=color;
        this.dpl = dpl;
        this.image=image;
        this.description=descr;
    }

   public clsVehicle(){

    }

    // just to test git at branch
    public String  TestGitChangedAtBranch01(){
        return "just for test brach project";
    }


    // just to test git
    public String  TestGitChanged(){
        return "just for test";
    }

    public clsVehicle( String model, String color,Double dpl,String image,String descr) {
        this.model = model;
        this.color=color;
        this.dpl = dpl;
        this.description=descr;
        this.image=image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getDpl() {
        return dpl;
    }

    public void setDpl(Double dpl) {
        this.dpl = dpl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

