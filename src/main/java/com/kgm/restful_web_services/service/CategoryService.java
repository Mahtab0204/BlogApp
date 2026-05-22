package com.kgm.restful_web_services.service;

import com.kgm.restful_web_services.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO addCategory(CategoryDTO categoryDTO);
    CategoryDTO getOneCategoryById(Long categoryId);
    List<CategoryDTO> getAllCategories();
    boolean deleteCategoryByid(Long categoryId);
    CategoryDTO updateCategoryById(Long categoryId,CategoryDTO categoryDTO);
    boolean deleteAllCategory();
}
