package net.weweave.tubewarder.util;

import net.weweave.tubewarder.dao.ConfigItemDao;
import net.weweave.tubewarder.dao.SendQueueItemDao;
import net.weweave.tubewarder.dao.SystemDao;
import net.weweave.tubewarder.domain.System;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

@Singleton
public class AliveStatusUpdater {
    private static final Logger LOG = Logger.getLogger(AliveStatusUpdater.class.getName());

    @Inject
    private SystemDao systemDao;

    @Inject
    private SendQueueItemDao sendQueueItemDao;

    @Inject
    private ConfigItemDao configItemDao;

    @Schedule(minute = "*", hour = "*", second = "*/5", persistent = false)
    private void scheduledUpdateStatus() {
        getSystemDao().updateAliveStatus(SystemIdentifier.getIdentifier());
    }

    @Schedule(minute = "*", hour = "*", second = "*/15", persistent = false)
    private void scheduledMoveItemsOfDeadSystems() {
        if (isMaster()) {
            LOG.info("Checking for queue items of dead systems in cluster (this is the cluster's master: ID="+SystemIdentifier.getIdentifier()+")");
            List<System> deadSystems = getSystemDao().getAllDeadWithQueueItems(getSystemDeadSeconds());
            if (deadSystems != null && deadSystems.size() > 0) {
                List<System> aliveSystems =  getSystemDao().getAllAlive(getSystemDeadSeconds());
                for (System deadSystem : deadSystems) {
                    Integer sourceId = deadSystem.getSystemId();
                    Integer targetId = aliveSystems.get(ThreadLocalRandom.current().nextInt(aliveSystems.size())).getSystemId();
                    LOG.info("Moving dangling queue items from source ID=" + sourceId + " to target ID=" + targetId);
                    getSendQueueItemDao().moveItems(sourceId, targetId);
                }
                LOG.info("Finished moving dangling queue items");
            }
        }
    }

    public void initAliveStatus() {
        Integer id = SystemIdentifier.getIdentifier();
        LOG.info("Initially updating cluster information by adding this instance with ID="+id+" to the list of system");
        getSystemDao().updateAliveStatus(id);
        List<System> aliveSystems =  getSystemDao().getAllAlive(getSystemDeadSeconds());
        StringBuilder sb = new StringBuilder("Cluster consists of "+aliveSystems.size()+" alive systems:");
        for (System system : aliveSystems) {
            sb.append("\n---> ID = "+system.getSystemId());
        }
        LOG.info(sb.toString());
    }

    private boolean isMaster() {
        return getSystemDao().isMasterOfAliveSystems(SystemIdentifier.getIdentifier(), getSystemDeadSeconds());
    }

    private Integer getSystemDeadSeconds() {
        return getConfigItemDao().getInt(ConfigManager.CONFIG_SYSTEM_DEAD_SECONDS, 60*5);
    }

    public SystemDao getSystemDao() {
        return systemDao;
    }

    public void setSystemDao(SystemDao systemDao) {
        this.systemDao = systemDao;
    }

    public SendQueueItemDao getSendQueueItemDao() {
        return sendQueueItemDao;
    }

    public void setSendQueueItemDao(SendQueueItemDao sendQueueItemDao) {
        this.sendQueueItemDao = sendQueueItemDao;
    }

    public ConfigItemDao getConfigItemDao() {
        return configItemDao;
    }

    public void setConfigItemDao(ConfigItemDao configItemDao) {
        this.configItemDao = configItemDao;
    }
}
