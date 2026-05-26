package com.syncall.api.infra.security.userdetails;

import com.syncall.api.exception.ResourceNotFoundException;
import com.syncall.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).
                map(CustomUserDetails::new)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));
    }
}
