package com.stinkytooters.stinkytootersbot.api.internal.user;

public class UserBuilder {

    private User user;

    public UserBuilder() {
        user = new User();
    }

    public static UserBuilder newBuilder() {
        return new UserBuilder();
    }

    public UserBuilder name(String name) {
        user.setName(name);
        return this;
    }

    public UserBuilder id(long id) {
        user.setId(id);
        return this;
    }

    public UserBuilder status(UserStatus status) {
        user.setStatus(status);
        return this;
    }

    public User build() {
        User result = user;
        user = null;
        return result;
    }

}
