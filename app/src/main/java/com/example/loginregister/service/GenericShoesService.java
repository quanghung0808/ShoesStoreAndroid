package com.example.loginregister.service;




import com.example.loginregister.model.Category;
import com.example.loginregister.model.GenericShoes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GenericShoesService {
    String GENERIC_SHOES = "GenericShoes";

    @GET(GENERIC_SHOES)
    Call<GenericShoes[]> getAllGenericShoes();

    @GET(GENERIC_SHOES + "/{id}")
    Call<GenericShoes> getAllGenericShoes(@Path("id") Object id);

    @GET(GENERIC_SHOES)
    Call<GenericShoes[]> getAllGenericShoesByName(@Query("name") String name);
}
