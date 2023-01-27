package com.stinkytooters.stinkytootersbot.service.user;

import com.stinkytooters.stinkytootersbot.api.internal.exception.ServiceException;
import com.stinkytooters.stinkytootersbot.api.internal.user.User;
import com.stinkytooters.stinkytootersbot.service.user.data.UserDao;
import com.stinkytooters.stinkytootersbot.service.user.data.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Named
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserDao userDao;

    @Inject
    public UserService(UserDao userDao) {
        this.userDao = Objects.requireNonNull(userDao);
    }

    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public User getUser(User user) {
        logger.info("Getting user: {}", user);
        Optional<UserData> userDataOptional;
        try {
            userDataOptional = userDao.getUser(user);
            if (userDataOptional.isEmpty()) {
                String message = String.format("Could not find user: (%s)", user.getName());
                logger.info(message);
                throw new ServiceException(message);
            } else {
                return userDataOptional.get().toUser();
            }
        } catch (ServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            String message = String.format("An unexpected error occurred while trying to get user (%s)", user.getName());
            logger.error(message, ex);
            throw new ServiceException(message);
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public User createUser(User user) {
        logger.info("Creating user: {}", user);

        Optional<UserData> userDataOptional;
        try {
            userDataOptional = userDao.getUser(user);
            if (userDataOptional.isPresent()) {
                String message = "Cannot create user (%s), user already exists.";
                logger.info(message);
                throw new ServiceException(message);
            }
        } catch (ServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            String message = String.format("An unexpected error occurred while trying to create user (%s).", user.getName());
            logger.error(message, ex);
            throw new ServiceException(message);
        }

        try {
            userDao.insertUser(user);
            return getUser(user);
        } catch (Exception ex) {
            String message = String.format("An unexpected error occurred, could not create user (%s).", user.getName());
            logger.error(message, ex);
            throw new ServiceException(message);
        }
    }

    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public List<User> getAllUsers() {
        try {
            List<UserData> userData = userDao.getAllUsers();
            return userData.stream().map(UserData::toUser).collect(Collectors.toList());
        } catch (Exception ex) {
            String message = String.format("An unexpected error occurred, could not get all users.", ex.getMessage());
            logger.error(message, ex);
            throw new ServiceException(message);
        }

    }

    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void saveUser(User user) {
        try {
            userDao.updateUser(user);
        } catch (Exception ex) {
            String message = String.format("An unexpected error occurred, could not save user. %s", ex.getMessage());
            logger.error(message, ex);
            throw new ServiceException(message);
        }
    }

}
