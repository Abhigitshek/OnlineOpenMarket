package com.abhishek.OnlineOpenMarket.Repository;

import com.abhishek.OnlineOpenMarket.Data.Entities.MembershipTier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "membershiptier")
public interface MembershipTierRepository extends CrudRepository<MembershipTier, Long>, PagingAndSortingRepository<MembershipTier,Long> {
    @Query("SELECT m FROM MembershipTier m WHERE m.name = ?1")
    public MembershipTier findByName(String name);
}
