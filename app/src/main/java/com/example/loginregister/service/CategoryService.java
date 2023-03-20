package com.example.loginregister.service;




import com.example.loginregister.model.Category;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface CategoryService {
    String CATEGORY = "Category";

    @GET(CATEGORY)
    Call<Category[]> getAllCategories();

    @GET(CATEGORY + "/{id}")
    Call<Category> getAllCategories(@Path("id") Object id);

    @DELETE(CATEGORY + "/{id}")
    Call<Category> deleteCategories(@Path("id") Object id);
}
