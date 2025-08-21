package com.du.besttrip.ordersb2c.repository;

import com.du.besttrip.ordersb2c.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseJpaRepository<T, ID> extends JpaRepository<T, ID> {
   default T getByIdOrElseThrow(ID id) throws NotFoundException {
       return findById(id)
               .orElseThrow(() -> new NotFoundException(String.valueOf(id)));
   }

   default void deleteByIdOrElseThrow(ID id) throws NotFoundException {
       delete(getByIdOrElseThrow(id));
   }
}
