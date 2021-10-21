package com.stinkytooters.stinkytootersbot.service.user.data;

import com.stinkytooters.stinkytootersbot.api.internal.user.User;

public class UserData {

    private long id;
    private String name;

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        return user;
    }

    public static UserData fromUser(User user) {
        UserData userData = new UserData();
        userData.setId(user.getId());
        userData.setName(user.getName());
        return userData;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserData{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
