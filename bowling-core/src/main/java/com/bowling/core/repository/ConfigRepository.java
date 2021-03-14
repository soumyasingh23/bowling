package com.bowling.core.repository;

import com.bowling.core.models.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Integer> {

    Config findByConfigId(String configId);
}
