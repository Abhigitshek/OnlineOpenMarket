package com.abhishek.OnlineOpenMarket.Repository;

import com.abhishek.OnlineOpenMarket.Data.Entities.SubCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "subcategory")
public interface SubCategoryRepository extends CrudRepository<SubCategory,Long>, PagingAndSortingRepository<SubCategory,Long> {
    @Query("SELECT c FROM SubCategory c WHERE c.name = ?1")
    public SubCategory findByName(String name);
}
