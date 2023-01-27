package com.stinkytooters.stinkytootersbot.service.user.data;

import com.stinkytooters.stinkytootersbot.api.internal.user.User;
import com.stinkytooters.stinkytootersbot.api.internal.user.UserStatus;

import java.util.StringJoiner;

public class UserData {

    private long id;
    private String name;
    private String status;

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setStatus(UserStatus.fromValue(status));
        return user;
    }

    public static UserData fromUser(User user) {
        UserData userData = new UserData();
        userData.setId(user.getId());
        userData.setName(user.getName());
        userData.setStatus(user.getStatus().getCode());
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserData.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("status='" + status + "'")
                .toString();
    }
}
