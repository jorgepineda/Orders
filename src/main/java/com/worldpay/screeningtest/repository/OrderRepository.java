package com.worldpay.screeningtest.repository;

import com.worldpay.screeningtest.domain.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE orders SET status = 'EXPIRED' " +
                   "WHERE DATEADD(DAY, coalesce(VALID_FOR_DAYS, 0), DATEADD(MONTH, coalesce(VALID_FOR_MONTHS,0), DATEADD(YEAR, coalesce(VALID_FOR_YEARS,0), VALID_FROM))) < :refDate " +
                   "AND status = 'VALID'",
           nativeQuery = true)
    public void expireOrders(@Param("refDate") Date refDate) ;

    @Modifying
    @Transactional
    @Query(value = "UPDATE orders SET status = 'VALID' " +
            "WHERE VALID_FROM <= :refDate AND status='NOT_STARTED' ",
            nativeQuery = true)
    public void activateOrders(@Param("refDate") Date refDate) ;

}
