package org.klb.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.klb.userservice.controller.dto.request.UserReq;
import org.klb.userservice.controller.dto.response.UserRes;
import org.klb.userservice.entity.User;
import org.klb.userservice.exception.DuplicateFieldException;
import org.klb.userservice.exception.UserNotFoundException;
import org.klb.userservice.repository.UserRepository;
import org.klb.userservice.service.UserService;
import org.klb.userservice.util.MessageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserserviceImpl implements UserService {

    private final UserRepository userRepository;
    private final MessageUtil messageUtil;
    private final ModelMapper modelMapper;

    @Override
    @Cacheable(value = "users", key = "#userId")
    public UserRes getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(messageUtil.getMessage("error.user.notfound", userId)));
        return modelMapper.map(user, UserRes.class);
    }

    @Override
    @Cacheable(value = "allUsers")
    public List<UserRes> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserRes.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Caching(
            put = { @CachePut(value = "users", key = "#result.id") },
            evict = { @CacheEvict(value = "allUsers", allEntries = true) }
    )
    public UserRes createUser(UserReq userReq) {
        if (userRepository.existsByName(userReq.getName())) {
            throw new DuplicateFieldException(messageUtil.getMessage("error.user.name.duplicate", userReq.getName()));
        }

        if (userRepository.existsByEmail(userReq.getEmail())) {
            throw new DuplicateFieldException(messageUtil.getMessage("error.user.email.duplicate", userReq.getEmail()));
        }

        User user = modelMapper.map(userReq, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserRes.class);
    }

    @Override
    @Transactional
    @Caching(
            put = { @CachePut(value = "users", key = "#result.id") },
            evict = { @CacheEvict(value = "allUsers", allEntries = true) }
    )
    public UserRes updateUser(Long userId, UserReq userReq) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(messageUtil.getMessage("error.user.notfound", userId)));

        if (!existingUser.getName().equals(userReq.getName()) && userRepository.existsByName(userReq.getName())) {
            throw new DuplicateFieldException(messageUtil.getMessage("error.user.name.duplicate", userReq.getName()));
        }

        if (!existingUser.getEmail().equals(userReq.getEmail()) && userRepository.existsByEmail(userReq.getEmail())) {
            throw new DuplicateFieldException(messageUtil.getMessage("error.user.email.duplicate", userReq.getEmail()));
        }

        existingUser.setName(userReq.getName());
        existingUser.setEmail(userReq.getEmail());
        existingUser.setPassword(userReq.getPassword());
        existingUser.setStatus(userReq.getStatus());

        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserRes.class);
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "users", key = "#userId"),
                    @CacheEvict(value = "allUsers", allEntries = true)
            }
    )
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(
                    messageUtil.getMessage("error.user.notfound", userId)
            );
        }
        userRepository.deleteById(userId);
    }
}
