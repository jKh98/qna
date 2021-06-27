package com.jkh98.qna.controller;

import com.jkh98.qna.exception.ForbiddenException;
import com.jkh98.qna.exception.ResourceNotFoundException;
import com.jkh98.qna.model.Category;
import com.jkh98.qna.model.Role;
import com.jkh98.qna.model.User;
import com.jkh98.qna.repository.CategoryRepository;
import com.jkh98.qna.repository.RoleRepository;
import com.jkh98.qna.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("api/v1")
@RestController
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/categories")
    public Page<Category> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @GetMapping("/categories/{categoryId}")
    public Category getCategoryById(@PathVariable UUID categoryId) {
        Optional<Category> categoryMaybe = categoryRepository.findById(categoryId);
        if (categoryMaybe.isEmpty()) {
            throw new ResourceNotFoundException("Category not found with id " + categoryId);
        }
        return categoryMaybe.get();
    }

    @PostMapping("/categories")
    public Category createCategory(@Valid @RequestBody Category category) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + userDetails.getUsername()));

        Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        if (user.getRoles().contains(adminRole)) {
            return categoryRepository.save(category);
        }
        throw new ForbiddenException("User is not admin");
    }

    @PutMapping("/categories/{categoryId}")
    public Category updateCategory(@PathVariable UUID categoryId,
                                   @Valid @RequestBody Category categoryRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + userDetails.getUsername()));

        Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        if (user.getRoles().contains(adminRole)) {
            return categoryRepository.findById(categoryId)
                    .map(category -> {
                        category.setDescription(categoryRequest.getDescription());
                        return categoryRepository.save(category);
                    }).orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));
        }

        throw new ForbiddenException("User is not admin");
    }


    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable UUID categoryId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + userDetails.getUsername()));

        Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));


        if (user.getRoles().contains(adminRole)) {
            return categoryRepository.findById(categoryId)
                    .map(category -> {
                        categoryRepository.delete(category);
                        return ResponseEntity.ok().build();
                    }).orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));
        }

        throw new ForbiddenException("User is not admin");

    }
}