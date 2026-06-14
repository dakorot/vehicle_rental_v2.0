package service;

import repository.IUserRepository;

public class UserService {
    private final IUserRepository userRepo;
    private final RentalService rentalService;

    public UserService(IUserRepository userRepo, RentalService rentalService) {
        this.userRepo = userRepo;
        this.rentalService = rentalService;
    }

    public void removeUser(String login) {
        if (login.equals("admin")) {
            throw new IllegalArgumentException("Unable to remove the main admin.");
        }
        if (rentalService.getUserRental(login) != null) {
            throw new IllegalStateException("Unable to remove the user, they haven't returned their vehicle yet!");
        }
        userRepo.remove(login);
        userRepo.save();
    }
}
