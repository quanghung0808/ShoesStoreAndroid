package com.example.loginregister.repository;

import com.example.loginregister.api.ApiClient;
import com.example.loginregister.consturl.ConstUrl;
import com.example.loginregister.service.CategoryService;

public class CategoryRepository {
    public static CategoryService getService() {
        return ApiClient.getClient(new ConstUrl().getBaseUrl()).create(CategoryService.class);


    }
}
