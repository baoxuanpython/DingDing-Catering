package com.sky.service.admin.impl;

import com.sky.mapper.admin.WorkspaceMapper;
import com.sky.service.admin.WorkspaceService;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    private final WorkspaceMapper workspaceMapper;
    public WorkspaceServiceImpl(WorkspaceMapper workspaceMapper) {
        this.workspaceMapper = workspaceMapper;
    }
}
