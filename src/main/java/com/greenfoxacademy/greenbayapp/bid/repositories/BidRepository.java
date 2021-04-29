package com.greenfoxacademy.greenbayapp.bid.repositories;

import com.greenfoxacademy.greenbayapp.bid.models.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
}
