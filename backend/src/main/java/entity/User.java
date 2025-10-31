package entity;

import java.util.List;

/**
 * The representation of a password-protected user for our program.
 */
public class User {

    private int userId;
    private String username;
    private String password;
    private String email;
    private String preferences;
    private List<Integer> savedRecipes;

    public User(String username, String password) {

        this.userId = 0;
        this.username = username;
        this.password = password;
        this.email = "";
        this.preferences = null;
        this.savedRecipes = null;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public List<Integer> getSavedRecipes() {
        return savedRecipes;
    }

    public void setSavedRecipes(List<Integer> savedRecipes) {
        this.savedRecipes = savedRecipes;
    }
}
