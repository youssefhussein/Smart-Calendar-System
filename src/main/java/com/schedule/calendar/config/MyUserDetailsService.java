package com.schedule.calendar.config;


import com.schedule.calendar.Models.User;
import com.schedule.calendar.Repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
        
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.schedule.calendar.Models.User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            var userObj = user.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(getRole(userObj))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
    
    private String[] getRole(User user) {
        if (user.getUserRole() == null) {
            return new String[]{"USER"};
        }
        return user.getUserRole().split(",");
    }
    
}
