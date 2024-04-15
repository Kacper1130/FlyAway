package FlyAway.user;

import FlyAway.security.RoleRepository;
import FlyAway.user.dto.UserRegistrationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> getAll(){
        LOGGER.debug("Retrieving all users from repository");
        List<User> users = userRepository.findAll();
        LOGGER.info("Retrieved {} users from repository", users.size());
        return users;
    }

    public User addUser(UserRegistrationDto userRegistrationDto){
        LOGGER.debug("Adding new user");
        //  TODO
//        if (emailExist(accountDto.getEmail())) {
//            throw new EmailExistsException
//                    ("There is an account with that email adress: " + accountDto.getEmail());
//        }

        User createdUser = new User();
        createdUser.setFirstname(userRegistrationDto.firstname());
        createdUser.setLastname(userRegistrationDto.lastname());
        createdUser.setEmail(userRegistrationDto.email());
        createdUser.setPassword(userRegistrationDto.password());
        createdUser.setPhoneNumber(userRegistrationDto.phoneNumber());
        createdUser.setDayOfBirth(userRegistrationDto.dateOfBirth());
        var role = roleRepository.findByName("ROLE_USER");
        createdUser.setRoles(Set.of(role));
        userRepository.save(createdUser);
        LOGGER.info("Created new user with id {}", createdUser.getId());
        return createdUser;
    }
}
