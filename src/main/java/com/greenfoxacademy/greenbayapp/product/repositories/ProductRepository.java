package com.greenfoxacademy.greenbayapp.product.repositories;

import com.greenfoxacademy.greenbayapp.product.models.Product;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query("SELECT c FROM Product c WHERE c.sold = false ORDER BY c.enlistingTime DESC")
  List<Product> filterUnsoldPaginated(Integer pageId, Pageable pageable);
}
