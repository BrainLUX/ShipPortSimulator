package com.dmitriev.oop.repository;

import com.dmitriev.oop.entity.Release;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseRepository extends CrudRepository<Release, Long> {
}
