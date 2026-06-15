package service.impl;

import repository.IUserRepository;
import service.IUserService;

public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepo;
    private final RentalServiceImpl rentalServiceImpl;

    public UserServiceImpl(IUserRepository userRepo, RentalServiceImpl rentalServiceImpl) {
        this.userRepo = userRepo;
        this.rentalServiceImpl = rentalServiceImpl;
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
