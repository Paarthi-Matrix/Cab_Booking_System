package com.i2i.zapcab.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.i2i.zapcab.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerService customerService;

}