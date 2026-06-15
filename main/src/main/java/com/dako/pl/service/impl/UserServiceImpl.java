package com.dako.pl.service.impl;

import com.dako.pl.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.dako.pl.repository.IUserRepository;
import com.dako.pl.service.IUserService;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepo;
    private final RentalServiceImpl rentalServiceImpl;

    public UserServiceImpl(IUserRepository userRepo, RentalServiceImpl rentalServiceImpl) {
        this.userRepo = userRepo;
        this.rentalServiceImpl = rentalServiceImpl;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.getUsers();
    }

    @Override
    public User getUser(String userId) {
        return userRepo.getUser(userId);
    }

    public void removeUser(String login) {
        if (login.equals("admin")) {
            throw new IllegalArgumentException("Unable to remove the main admin.");
        }
        if (rentalServiceImpl.getUserRental(login) != null) {
            throw new IllegalStateException("Unable to remove the user, they haven't returned their vehicle yet!");
        }
        userRepo.remove(login);
        userRepo.save();
    }
}
