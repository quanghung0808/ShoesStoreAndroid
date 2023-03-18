package com.example.loginregister.repository;

import com.example.loginregister.api.ApiClient;
import com.example.loginregister.consturl.ConstUrl;
import com.example.loginregister.service.CategoryService;
import com.example.loginregister.service.GenericShoesService;

public class GenericShoesRepository {
    public static GenericShoesService getService() {
        return ApiClient.getClient(new ConstUrl().getBaseUrl()).create(GenericShoesService.class);


    }
}
