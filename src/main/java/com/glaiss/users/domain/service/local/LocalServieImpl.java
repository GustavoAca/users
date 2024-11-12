package com.glaiss.users.domain.service.local;

import com.glaiss.core.domain.service.BaseServiceImpl;
import com.glaiss.users.domain.model.Local;
import com.glaiss.users.domain.repository.LocalRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LocalServieImpl extends BaseServiceImpl<Local, UUID, LocalRepository> implements LocalService {

    protected LocalServieImpl(LocalRepository repo) {
        super(repo);
    }
}
