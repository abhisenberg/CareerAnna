package com.careeranna.careeranna.helper;

import com.careeranna.careeranna.data.FreeCourse;
import com.careeranna.careeranna.data.MyPaidCourse;
import com.careeranna.careeranna.data.PaidCourse;
import com.careeranna.careeranna.data.SubCategory;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface NewApi {

    String BASE_URL = "https://careeranna.com/api/";

    @GET("fetchFreeCoursesApp.php")
    Call<List<FreeCourse>> getFreeCourses();

    @GET("fetchPaidCoursesApp.php")
    Call<List<PaidCourse>> getPaidCourses();

    @GET("fetchPaidCourseExamCategoryApp.php")
    Call<List<SubCategory>> getPaidCourseSubCategory();


    @GET("fetchMyPaidCoursesIdApp.php")
    Call<List<MyPaidCourse>> getMyPaidCourse(@QueryMap Map<String, String> params);

}
