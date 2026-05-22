package com.kgm.restful_web_services.controller;

import com.kgm.restful_web_services.ResponseMessage.MessageResponse;
import com.kgm.restful_web_services.dto.CategoryDTO;
import com.kgm.restful_web_services.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/jpa/add-category")
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO){
        CategoryDTO savedCategory = categoryService.addCategory(categoryDTO);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/jpa/get-one-category/{categoryId}")
    public ResponseEntity<CategoryDTO> getOneCategory(@PathVariable Long categoryId){
        CategoryDTO category = categoryService.getOneCategoryById(categoryId);
        return new ResponseEntity<>(category,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/jpa/get-all-category")
    public ResponseEntity<List<CategoryDTO>> getAllCategory(){
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/jpa/delete-one-category/{categoryId}")
    public ResponseEntity<MessageResponse> deleteCategoryById(@PathVariable Long categoryId){
        if(categoryService.deleteCategoryByid(categoryId)){
            MessageResponse response = new MessageResponse();
            response.setMessage("Category deleted successfully");
            return new ResponseEntity<>(response,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/jpa/update-category/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategoryById(
            @PathVariable Long categoryId,
            @RequestBody CategoryDTO categoryDTO){

        CategoryDTO category = categoryService.updateCategoryById(categoryId,categoryDTO);
        return new ResponseEntity<>(category,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/jpa/delete-all-category")
    public ResponseEntity<MessageResponse> deleteAllCategory(){
        if(categoryService.deleteAllCategory()){
            MessageResponse response = new MessageResponse();
            response.setMessage("All Category deleted successfully");
            return new ResponseEntity<>(response,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
