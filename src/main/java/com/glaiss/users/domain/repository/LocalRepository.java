package com.glaiss.users.domain.repository;

import com.glaiss.core.domain.repository.BaseRepository;
import com.glaiss.users.domain.model.Local;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LocalRepository extends BaseRepository<Local, UUID> {
}
