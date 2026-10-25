package com.smartcity.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Issue implements Serializable {
    private int id;
    private String category, location, description, department, photo;
    private Priority priority;
    private Status status = Status.PENDING;
    private String reporterEmail, reporterName;
    private LocalDateTime createdAt, updatedAt;

    public Issue() { }   // used by the DAO when mapping rows

    // Constructor overloading (Unit II): with and without a photo
    public Issue(String category, String location, String description, Priority priority, User reporter) {
        this(category, location, description, priority, null, reporter);
    }
    public Issue(String category, String location, String description, Priority priority,
                 String photo, User reporter) {
        this.category = category;
        this.location = location;
        this.description = description;
        this.priority = priority;
        this.photo = photo;
        this.reporterEmail = reporter.getEmail();
        this.reporterName = reporter.getName();
        this.createdAt = this.updatedAt = LocalDateTime.now();   // Date & Time API
    }

    public int getId() { return id; }                       public void setId(int id) { this.id = id; }
    public String getCategory() { return category; }        public void setCategory(String c) { category = c; }
    public String getLocation() { return location; }        public void setLocation(String l) { location = l; }
    public String getDescription() { return description; }  public void setDescription(String d) { description = d; }
    public String getDepartment() { return department; }    public void setDepartment(String d) { department = d; }
    public String getPhoto() { return photo; }              public void setPhoto(String p) { photo = p; }
    public Priority getPriority() { return priority; }      public void setPriority(Priority p) { priority = p; }
    public Status getStatus() { return status; }            public void setStatus(Status s) { status = s; }
    public String getReporterEmail() { return reporterEmail; } public void setReporterEmail(String e) { reporterEmail = e; }
    public String getReporterName() { return reporterName; }   public void setReporterName(String n) { reporterName = n; }
    public LocalDateTime getCreatedAt() { return createdAt; }  public void setCreatedAt(LocalDateTime t) { createdAt = t; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }  public void setUpdatedAt(LocalDateTime t) { updatedAt = t; }

    @Override public String toString() { return "#" + id + " " + category + " @ " + location + " [" + status + "]"; }
}
