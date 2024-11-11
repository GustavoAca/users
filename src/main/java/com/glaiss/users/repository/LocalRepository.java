package com.glaiss.users.repository;

import com.glaiss.core.repository.BaseRepository;
import com.glaiss.users.model.Local;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LocalRepository extends BaseRepository<Local, UUID> {
}
