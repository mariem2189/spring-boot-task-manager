package com.task.manager.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "tasks")
@JsonIgnoreProperties(value = {"createdAt", "updatedAt", "status" , "creator" },
        allowGetters = true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    private String description;

    private LocalDate dueDate;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String creator;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private Date updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    public enum Status {
        NEW, INPROGRESS, DONE;
    }

    public Task(Long id, String subject, String description, LocalDate dueDate,
         String creator, Date createdAt, Date updatedAt, Status status) {

        this.id = id;
        this.subject = subject;
        this.description = description;
        this.dueDate = dueDate;
        this.creator = creator;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.status = status;
    }


    public Task() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String deString) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Optional<Date> createdAt) {
        this.createdAt = createdAt.isEmpty() ? new Date() : createdAt.get();
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Optional<Date> updatedAt) {
        this.updatedAt = updatedAt.isEmpty() ? new Date() : updatedAt.get();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
