package com.kgm.restful_web_services.service;

import com.kgm.restful_web_services.dto.CategoryDTO;
import com.kgm.restful_web_services.model.Category;
import com.kgm.restful_web_services.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImplementation implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;



    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryDTO.class);
    }

    @Override
    public CategoryDTO getOneCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with id: "+categoryId));
        return modelMapper.map(category,CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        if(categoryList.isEmpty()){
            throw new ResourceNotFoundException("No Category found");
        }
        List<CategoryDTO> categoryDTOList = categoryList.stream().map(category -> modelMapper.map(category,CategoryDTO.class)).collect(Collectors.toList());
        return categoryDTOList;
    }

    @Override
    public boolean deleteCategoryByid(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with id: "+categoryId));
        return true;
    }

    @Override
    public CategoryDTO updateCategoryById(Long categoryId, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with id: "+categoryId));

        Category updatedCategory = modelMapper.map(categoryDTO,Category.class);
        category.setId(categoryId);
        category.setName(updatedCategory.getName());
        category.setDescription(updatedCategory.getDescription());

        Category savedCategory = categoryRepository.save(category);

        return modelMapper.map(savedCategory,CategoryDTO.class);
    }

    @Override
    public boolean deleteAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        if(categoryList.isEmpty()){
            throw new ResourceNotFoundException("No Category found");
        }
        categoryRepository.deleteAll();
        return true;
    }


}
