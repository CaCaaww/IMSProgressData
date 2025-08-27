package com.ImsProg.IMSProgressData.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class imsProgGui {
    @JsonProperty("Program Name") private String programName;
    @JsonProperty("Cust") private String customer;
    @JsonProperty("Description") private String description;
    @JsonProperty("Updates to TTM") private String updates;
    @JsonProperty("Type") private String type;

    public imsProgGui(@JsonProperty("Program Name") String programName, @JsonProperty("Cust") String customer, 
    @JsonProperty("Description") String description, @JsonProperty("Updates to TTM")  String updates, 
    @JsonProperty("Type") String type ){
        this.programName = programName;
        this.customer = customer;
        this.description = description;
        this.updates = updates;
        this.type = type;
    }

    @JsonProperty("Program Name")
    public String getProgramName(){
        return programName;
    }
    @JsonProperty("Cust")
    public String getCust(){
        return customer;
    }
    @JsonProperty("Description")
    public String getDescription(){
        return description;
    }
    @JsonProperty("Updates to TTM")
    public String getUpdates(){
        return updates;
    }
    @JsonProperty("Type")
    public String getType(){
        return type;
    }

    @JsonProperty("Program Name")
    public void setProgramName(String programName){
        this.programName = programName;
    }
    @JsonProperty("Cust")
    public void setCust(String customer){
        this.customer = customer;
    }
    @JsonProperty("Description")
    public void setDescription(String description){
        this.description = description;
    }
    @JsonProperty("Updates to TTM")
    public void getUpdates(String updates){
        this.updates = updates;
    }
    @JsonProperty("Type")
    public void setType(String type){
        this.type = type;
    }

    @Override
    public String toString(){
        return "imsProgGui[programName = " + programName + ", customer = " + customer + ", description = " + description + ", updates = " + updates + ", type = " + type + "]";
    }

    
}
