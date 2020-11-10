package com.example.pmapplication;

public class Resource {
    String ID,Cost,ProjectID,ResourceName,ResourceType,TimePerDay;

    public Resource(String ID, String cost, String projectID, String resourceName, String resourceType, String timePerDay) {
        this.ID = ID;
        Cost = cost;
        ProjectID = projectID;
        ResourceName = resourceName;
        ResourceType = resourceType;
        TimePerDay = timePerDay;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public String getResourceName() {
        return ResourceName;
    }

    public void setResourceName(String resourceName) {
        ResourceName = resourceName;
    }

    public String getResourceType() {
        return ResourceType;
    }

    public void setResourceType(String resourceType) {
        ResourceType = resourceType;
    }

    public String getTimePerDay() {
        return TimePerDay;
    }

    public void setTimePerDay(String timePerDay) {
        TimePerDay = timePerDay;
    }
}
