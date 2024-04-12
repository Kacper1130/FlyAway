package FlyAway.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User addUser(User user){
        //todo set role
        User createdUser = userRepository.save(user);
        return createdUser;
    }
}
