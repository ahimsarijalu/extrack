package com.ahimsarijalu.extrack.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ahimsarijalu.extrack.user.UserUtils.mapUserToDTO;
import static com.ahimsarijalu.extrack.utils.MapperUtil.mapDTOToEntity;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserUtils::mapUserToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(String id) {
        return userRepository.findById(UUID.fromString(id))
                .map(UserUtils::mapUserToDTO)
                .orElse(null);
    }

    public UserDTO saveUser(UserDTO userDTO) {
        User user = mapDTOToEntity(userDTO, User.class);
        user = userRepository.save(user);

        return mapUserToDTO(user);
    }

    public void updateUser(String id, UserDTO userDTO) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new UserNotFoundException(id));

        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getAge() != null) {
            user.setAge(userDTO.getAge());
        }
        mapUserToDTO(userRepository.save(user));
    }


    public void deleteUser(String id) {
        if (userRepository.findById(UUID.fromString(id)).isEmpty()) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(UUID.fromString(id));
    }
}
