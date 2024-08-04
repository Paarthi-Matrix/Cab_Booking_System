package com.i2i.zapcab.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.i2i.zapcab.dto.ApiResponseDto;
import com.i2i.zapcab.dto.AuthenticationResponseDto;
import com.i2i.zapcab.dto.FetchAllPendingRequestsDto;
import com.i2i.zapcab.dto.UpdatePendingRequestDto;
import com.i2i.zapcab.exception.DatabaseException;
import com.i2i.zapcab.service.AdminService;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;


//    @Test
//    public void testFetchAllPendingRequests_DatabaseException() {
//        Page<FetchAllPendingRequestsDto> pendingRequests = new PageImpl<>(Collections.emptyList());
//        when(adminService.getAllPendingRequest(0, 10)).thenThrow(new DatabaseException("Database error"));
//
//        ApiResponseDto<Page<FetchAllPendingRequestsDto>> response = adminController.fetchAllPendingRequests(0, 10);
//
//        assertEquals(ApiResponseDto.statusInternalServerError(pendingRequests, new DatabaseException("Database error")), response);
//    }


}

