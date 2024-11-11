package com.glaiss.users.service.local;

import com.glaiss.core.service.BaseServiceImpl;
import com.glaiss.users.model.Local;
import com.glaiss.users.repository.LocalRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LocalServieImpl extends BaseServiceImpl<Local, UUID, LocalRepository> implements LocalService {

    protected LocalServieImpl(LocalRepository repo) {
        super(repo);
    }
}
