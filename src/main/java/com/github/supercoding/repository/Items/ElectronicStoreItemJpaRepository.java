package com.github.supercoding.repository.Items;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectronicStoreItemJpaRepository extends JpaRepository<ItemEntity, Integer> {
}
