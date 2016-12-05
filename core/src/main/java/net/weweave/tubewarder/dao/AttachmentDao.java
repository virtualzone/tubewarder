package net.weweave.tubewarder.dao;

import net.weweave.tubewarder.domain.Attachment;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class AttachmentDao extends AbstractDao<Attachment> {
    @Inject
    private SendQueueItemDao sendQueueItemDao;

    @Override
    public void initObject(Attachment obj) {
        if (obj != null) {
            getSendQueueItemDao().initObject(obj.getSendQueueItem());
        }
    }

    public List<Attachment> getAttachmentsForSendQueueItem(Long sendQueueItemId) {
        TypedQuery<Attachment> query = getEntityManager().createQuery("SELECT a FROM Attachment a " +
                "WHERE a.sendQueueItem.id = :id", Attachment.class);
        query.setParameter("id", sendQueueItemId);
        List<Attachment> result = query.getResultList();
        initObject(result);
        return result;
    }

    public SendQueueItemDao getSendQueueItemDao() {
        return sendQueueItemDao;
    }

    public void setSendQueueItemDao(SendQueueItemDao sendQueueItemDao) {
        this.sendQueueItemDao = sendQueueItemDao;
    }
}
