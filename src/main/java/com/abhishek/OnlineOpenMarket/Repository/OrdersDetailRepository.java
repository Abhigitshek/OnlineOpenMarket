package com.abhishek.OnlineOpenMarket.Repository;

import com.abhishek.OnlineOpenMarket.Data.Entities.OrderDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "orders")
public interface OrdersDetailRepository extends CrudRepository<OrderDetail, Long>, PagingAndSortingRepository<OrderDetail,Long> {
}
