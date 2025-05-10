package com.abhishek.OnlineOpenMarket.Repository;

import com.abhishek.OnlineOpenMarket.Data.Entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "user")
public interface UserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User,Long> {
}
