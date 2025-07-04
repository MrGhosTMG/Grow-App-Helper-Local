package org.jdta.growapp.DTO;

import java.time.LocalDateTime;

public class User {

    private int id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    //парсить строку в LocalDate или LocalDateTime (можно через DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
