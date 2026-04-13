package com.buenaventura.erp.auth.service;

import com.buenaventura.erp.auth.dto.LoginRequest;
import com.buenaventura.erp.auth.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}