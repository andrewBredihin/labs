package com.bav.testproject.service;

import com.bav.testproject.repository.UserRepository;
import com.bav.testproject.MongoUserDetails;
import com.bav.testproject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

//Поиск пользователя по имени
@Component
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException(username);
        else{
            MongoUserDetails details = new MongoUserDetails(user);
            System.out.println(details);
            return details;
        }
    }
}
