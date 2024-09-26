package com.github.supercoding.repository.users;

public interface UserRepository {
    UserEntity findUserById(Integer userId);
}
