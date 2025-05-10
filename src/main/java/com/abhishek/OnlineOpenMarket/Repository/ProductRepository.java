package com.abhishek.OnlineOpenMarket.Repository;

import com.abhishek.OnlineOpenMarket.Data.Entities.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "product")
public interface ProductRepository extends CrudRepository<Product, Long>, PagingAndSortingRepository<Product,Long> {
}
