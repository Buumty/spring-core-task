package org.example.model;

public abstract class User {
    private final long userId;
    private String firstName;
    private String lastName;
    private final String username;
    private final String password;
    private boolean isActive;


    protected User(long userId, String firstName, String lastName, String username, String password, boolean isActive) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getUserId() {
        return userId;
    }
    @Override
    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        User user = (User) object;
        return userId == user.userId;
    }
    @Override
    public final int hashCode() {
        int result = getClass().hashCode();
        result = 31 * result + Long.hashCode(userId);
        return result;
    }
}
