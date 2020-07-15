package com.etxtechstack.api.easypos_application.services;

import com.etxtechstack.api.easypos_application.models.ActivityLog;
import com.etxtechstack.api.easypos_application.repositories.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogService {
    @Autowired
    private ActivityLogRepository activityLogRepository;

    public List<ActivityLog> getAllActivityLogs() {
        try {
            return activityLogRepository.findAll(Sort.by(Sort.Direction.DESC, "created_at"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ActivityLog createActivityLog(ActivityLog logModel) {
        System.out.println("==========activity Model: " + logModel.toString());
        try {
            return activityLogRepository.save(logModel);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
