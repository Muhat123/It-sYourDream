package com.maju_mundur.MajuMundur.service.impl;

import com.maju_mundur.MajuMundur.dto.Request.CustomerRequest;
import com.maju_mundur.MajuMundur.dto.Response.CustomerResponse;
import com.maju_mundur.MajuMundur.entity.Customer;
import com.maju_mundur.MajuMundur.entity.User;
import com.maju_mundur.MajuMundur.exception.OurException;
import com.maju_mundur.MajuMundur.repository.CustomerRepository;
import com.maju_mundur.MajuMundur.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse create(CustomerRequest customerRequest, User user) {
        Customer newCustomer = Customer.builder()
                .name(customerRequest.getName())
                .phone(customerRequest.getPhone())
                .email(customerRequest.getEmail())
                .points(customerRequest.getPoints())
                .user(user)
                .build();

        Customer savedCustomer = customerRepository.saveAndFlush(newCustomer);

        return mapToResponse(savedCustomer);
    }

    @Override
    public List<CustomerResponse> getAll() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getCustomerById(String id) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        return customerOpt.map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public String deleteById(String id) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        customerOpt.ifPresent(customerRepository::delete);
        return id;
    }

    @Override
    public void updateStatusById(String id) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setPoints(0);
            customerRepository.save(customer);
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    @Override
    public CustomerResponse updateCustomerById(CustomerRequest customerRequest) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedInUser.getId());
        if (customer == null) {
            throw new OurException("Customer not found");
        }
        if (customerRequest.getName() != null && !customerRequest.getName().isBlank()) {
            customer.setName(customerRequest.getName());
        }
        if (customerRequest.getPhone() != null && !customerRequest.getPhone().isBlank()) {
            customer.setPhone(customerRequest.getPhone());
        }
        if (customerRequest.getEmail() != null && !customerRequest.getEmail().isBlank()) {
            customer.setEmail(customerRequest.getEmail());
        }
        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponse(savedCustomer);
    }




    // Helper method to map Customer entity to CustomerResponse DTO
    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .points(customer.getPoints())
                .build();
    }
}

