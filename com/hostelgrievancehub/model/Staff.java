package com.hostelgrievancehub.model;

public class Staff {
    private int id;
    private String name;
    private String email;
    private String password;
    private String scope; // e.g., GH1, GH2, GH3, BH, or ALL

    public Staff() {}

    public Staff(int id, String name, String email, String password, String scope) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.scope = scope;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }

    public String getRole() { return "Staff"; } // Add getRole method
}
