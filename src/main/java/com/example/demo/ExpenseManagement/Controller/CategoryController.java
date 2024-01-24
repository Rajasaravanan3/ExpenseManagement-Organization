package com.example.demo.ExpenseManagement.Controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.ExpenseManagement.DTO.CategoryDTO;
import com.example.demo.ExpenseManagement.Service.CategoryService;


@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable("categoryId") Long categoryId) {

        CategoryDTO categoryDTO = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {

        List<CategoryDTO> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    

    //admin's access
    @PostMapping
    public ResponseEntity<Void> addCategory(@RequestBody CategoryDTO categoryDTO) {

        categoryService.addCategory(categoryDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //admin's access
    @PutMapping("/{adminId}")
    public ResponseEntity<Void> updateCategory(@PathVariable("adminId") Long adminId, @RequestBody CategoryDTO categoryDTO) {

        categoryService.updateCategory(adminId, categoryDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
