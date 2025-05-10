package com.abhishek.OnlineOpenMarket.Repository;

import com.abhishek.OnlineOpenMarket.Data.Entities.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "category")
public interface CategoryRepository extends CrudRepository<Category, Long>, PagingAndSortingRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.name = ?1")
    public Category findByName(String name);
}
