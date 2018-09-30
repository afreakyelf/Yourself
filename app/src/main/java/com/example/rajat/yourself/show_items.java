package com.example.rajat.yourself;


public class show_items {
    private String taskname;  //put this name same as Database Fields

    public show_items(String image_title ) {
       taskname = image_title;

    }
    public show_items()
    {
        //Empty Constructor Needed
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }
}

