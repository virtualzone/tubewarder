package net.weweave.tubewarder.util;

import net.weweave.tubewarder.dao.SystemDao;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class AliveStatusUpdater {
    @Inject
    private SystemDao systemDao;

    @Schedule(minute = "*", hour = "*", second = "*/15", persistent = false)
    private void scheduledUpdateStatus() {
        getSystemDao().updateAliveStatus();
    }

    public SystemDao getSystemDao() {
        return systemDao;
    }

    public void setSystemDao(SystemDao systemDao) {
        this.systemDao = systemDao;
    }
}
