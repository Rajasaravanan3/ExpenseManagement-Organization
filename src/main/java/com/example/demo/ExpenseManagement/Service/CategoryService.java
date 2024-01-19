package com.example.demo.ExpenseManagement.Service;

import java.time.ZonedDateTime;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.ExpenseManagement.DTO.CategoryDTO;
import com.example.demo.ExpenseManagement.Entity.Category;
import com.example.demo.ExpenseManagement.ExceptionController.ApplicationException;
import com.example.demo.ExpenseManagement.ExceptionController.ValidationException;
import com.example.demo.ExpenseManagement.Repository.CategoryRepository;
import com.example.demo.ExpenseManagement.Repository.OrganizationRepository;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DozerBeanMapper mapper;

    @Autowired
    private OrganizationRepository organizationRepository;

    public CategoryDTO getCategoryById(Long categoryId) {
        
        Category category = null;
        try {
            category = categoryRepository.findCategoryById(categoryId);
            if(category == null) {
                throw new ValidationException("No record found for the category id " + categoryId, HttpStatus.NOT_FOUND);
            }
            return this.mapCategoryToCategoryDTO(category);
        }
        catch (ValidationException e) {
            throw e;
        }
        // catch (Exception e) {
        //     throw new ApplicationException("An unexpected error occurred while retrieving category by Id "+ categoryId);
        // }
    }

    public void addCategory(CategoryDTO categoryDTO) {

        Category category = null;
        try {
            if(categoryDTO == null || categoryDTO.getCategoryId() instanceof Long ||
                (categoryDTO.getCategoryName() instanceof String && (categoryDTO.getCategoryName().isEmpty() || categoryDTO.getCategoryName().length() > 40)) ||
                (categoryDTO.getCategoryDescription() instanceof String && categoryDTO.getCategoryDescription().length() > 300) ||
                (categoryDTO.getOrganizationId() == null))
                    
                    throw new ValidationException("Non null field value must not be null or empty and characters must not exceed limit.", HttpStatus.BAD_REQUEST);
            
            category = this.mapCategoryDTOToCategory(categoryDTO);
            category.setOrganization(organizationRepository.findOrganizationById(categoryDTO.getOrganizationId()));
            categoryRepository.saveAndFlush(category);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while saving category.");
        }
    }

    public void updateCategory(CategoryDTO updatedCategoryDTO) {
        Category existingCategory = null;
        try {
            existingCategory = categoryRepository.findCategoryById(updatedCategoryDTO.getCategoryId());
            if(existingCategory == null) {
                throw new ValidationException("No record found to update for the catgeory id" + updatedCategoryDTO.getCategoryId(), HttpStatus.NOT_FOUND);
            }

            if(updatedCategoryDTO.getCategoryName() instanceof String && ! (updatedCategoryDTO.getCategoryName().isEmpty())) {

                if(updatedCategoryDTO.getCategoryName().length() > 40)
                    throw new ValidationException("category name must not exceed 40 characters", HttpStatus.BAD_REQUEST);

                existingCategory.setCategoryName(updatedCategoryDTO.getCategoryName());
            }

            if(updatedCategoryDTO.getCategoryDescription() instanceof String) {

                if(updatedCategoryDTO.getCategoryDescription().length() > 300)
                    throw new ValidationException("category description must not exceed 300 characters", HttpStatus.BAD_REQUEST);

                existingCategory.setCategoryDescription(updatedCategoryDTO.getCategoryDescription());
            }

            if(updatedCategoryDTO.getIsActive() instanceof Boolean) {
                existingCategory.setIsActive(updatedCategoryDTO.getIsActive());
            }

            if(updatedCategoryDTO.getCreatedDate() instanceof ZonedDateTime) {
                existingCategory.setCreatedDate(updatedCategoryDTO.getCreatedDate());
            }

            if(updatedCategoryDTO.getModifiedDate() instanceof ZonedDateTime) {
                existingCategory.setModifiedDate(updatedCategoryDTO.getModifiedDate());
            }

            if(updatedCategoryDTO.getOrganizationId() instanceof Long) {
                existingCategory.setOrganization(organizationRepository.findOrganizationById(updatedCategoryDTO.getOrganizationId()));
            }
            categoryRepository.save(existingCategory);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while updating category.");
        }
    }

    public CategoryDTO mapCategoryToCategoryDTO(Category category) {

        CategoryDTO categoryDTO = mapper.map(category, CategoryDTO.class);
        categoryDTO.setOrganizationId(category.getOrganization().getOrganizationId());
        
        return categoryDTO;
    }

    public Category mapCategoryDTOToCategory(CategoryDTO categoryDTO) {

        Category category = mapper.map(categoryDTO, Category.class);
        category.setOrganization(organizationRepository.findOrganizationById(categoryDTO.getOrganizationId()));
        return category;
    }
}
