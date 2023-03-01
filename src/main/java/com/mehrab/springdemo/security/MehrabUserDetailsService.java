package com.mehrab.springdemo.security;

import com.mehrab.springdemo.model.Customer;
import com.mehrab.springdemo.model.CustomerUserDetails;
import com.mehrab.springdemo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MehrabUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Autowired
    public MehrabUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Optional<Customer> customer = customerRepository.getCustomersByEmail(username);
            // check and validate user pw ....
            if (!customer.isPresent()){
                throw new UsernameNotFoundException("user not found");
            }
        return new CustomerUserDetails(customer.get());
    }
}
