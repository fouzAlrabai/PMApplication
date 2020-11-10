package com.example.pmapplication;

public class Project {
    String ID;
    String ProjectName;
    String StartDate;
    String FinishDate;
    String Budget;
    String managerID;

    public Project(){

    }
    public Project( String ID,String ProjectName,String StartDate,String FinishDate,String Budget,String managerID){
        this.ID=ID;
        this.ProjectName=ProjectName;
        this.StartDate=StartDate;
        this.FinishDate=FinishDate;
        this.Budget=Budget;
        this.managerID=managerID;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public void setFinishDate(String finishDate) {
        FinishDate = finishDate;
    }

    public void setBudget(String budget) {
        Budget = budget;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getStartDate() {
        return StartDate;
    }

    public String getFinishDate() {
        return FinishDate;
    }

    public String getBudget() {
        return Budget;
    }

    public String getManagerID() {
        return managerID;
    }

    public void setManagerID(String managerID) {
        this.managerID = managerID;
    }

}
