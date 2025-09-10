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
        if (programName.indexOf(",") != -1){
            return "\"" + programName + "\"";
        }
        return programName;
    }
    @JsonProperty("Cust")
    public String getCust(){
        if (customer.indexOf(",") != -1){
            return "\"" + customer + "\"";
        }
        return customer;
    }
    @JsonProperty("Description")
    public String getDescription(){
        if (description.indexOf(",") != -1){
            return "\"" + description + "\"";
        }
        return description;
    }
    @JsonProperty("Updates to TTM")
    public String getUpdates(){
        if (updates.indexOf(",") != -1){
            return "\"" + updates + "\"";
        }
        return updates;
    }
    @JsonProperty("Type")
    public String getType(){
        if (type.indexOf(",") != -1){
            return "\"" + type + "\"";
        }
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

    @Override
    public boolean equals(Object other){
        if (other instanceof imsProgGui){
            imsProgGui other2 = (imsProgGui)other;
            return (programName.equals(other2.programName)
            && customer.equals(other2.customer)
            && description.equals(other2.description)
            && updates.equals(other2.updates)
            && type.equals(other2.type));
        } else {
            return false;
        }
    }

    
}
