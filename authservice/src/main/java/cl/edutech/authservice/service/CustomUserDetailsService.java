package cl.edutech.authservice.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        //if ("felipe.gonzalez@example.com".equals(email)) {
        //    return User.withUsername(email)
        //            .password("123456")
                    //.roles("USER")
        //            .build();
        }
        throw new UsernameNotFoundException("Usuario no encontrado");
    }
}
