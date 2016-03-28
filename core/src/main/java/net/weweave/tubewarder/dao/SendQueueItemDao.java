package net.weweave.tubewarder.dao;

import net.weweave.tubewarder.domain.Attachment;
import net.weweave.tubewarder.domain.SendQueueItem;
import net.weweave.tubewarder.util.DbValueRetriever;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Stateless
public class SendQueueItemDao extends AbstractDao<SendQueueItem> {
    @Inject
    private AttachmentDao attachmentDao;

    @Override
    public void store(SendQueueItem item) {
        super.store(item);
        getEntityManager().flush();
    }

    @Override
    public void update(SendQueueItem item) {
        super.update(item);
        getEntityManager().flush();
    }

    public void recoverUnprocessedItems() {
        try {
            Query query = getEntityManager().createQuery("UPDATE SendQueueItem i SET i.inProcessing = 0 WHERE i.inProcessing = 1");
            query.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Long> getUnprocessedItemIds() {
        TypedQuery<Long> query = getEntityManager().createQuery("SELECT i.id FROM SendQueueItem i ORDER BY i.createDate ASC", Long.class);
        return query.getResultList();
    }

    public List<Long> getFailedUnqueuedItemIds(int minAgeSeconds) {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.SECOND, (-1)*minAgeSeconds);
        TypedQuery<Long> query = getEntityManager().createQuery("SELECT i.id FROM SendQueueItem i " +
                "WHERE i.inProcessing = 0 AND i.tryCount > 0 AND i.lastTryDate <= :refDate " +
                "ORDER BY i.lastTryDate ASC", Long.class);
        query.setParameter("refDate", cal.getTime());
        return query.getResultList();
    }

    public Long getTotalCount() {
        TypedQuery<Long> query = getEntityManager().createQuery("SELECT COUNT(i.id) FROM SendQueueItem i", Long.class);
        return DbValueRetriever.getLongValueOrZero(query);
    }

    public Long getRetryCount() {
        TypedQuery<Long> query = getEntityManager().createQuery("SELECT COUNT(i.id) FROM SendQueueItem i " +
                "WHERE i.tryCount > 0", Long.class);
        return DbValueRetriever.getLongValueOrZero(query);
    }

    public void setItemInProcessing(SendQueueItem item) {
        getEntityManager()
                .createQuery("UPDATE SendQueueItem i SET i.inProcessing = 1 WHERE i.id = :id")
                .setParameter("id", item.getId())
                .executeUpdate();
    }

    public void setItemNextTry(SendQueueItem item) {
        getEntityManager()
                .createQuery("UPDATE SendQueueItem i SET i.inProcessing = 0, i.lastTryDate = :date, i.tryCount = i.tryCount+1 WHERE i.id = :id")
                .setParameter("id", item.getId())
                .setParameter("date", new Date())
                .executeUpdate();
    }

    @Override
    public void delete(SendQueueItem item) {
        List<Attachment> attachments = item.getAttachments();
        if (attachments != null) {
            for (Attachment attachment : attachments) {
                getAttachmentDao().delete(attachment);
            }
        }
        getEntityManager()
                .createQuery("DELETE FROM SendQueueItem i WHERE i.id = :id")
                .setParameter("id", item.getId())
                .executeUpdate();
    }

    public AttachmentDao getAttachmentDao() {
        return attachmentDao;
    }

    public void setAttachmentDao(AttachmentDao attachmentDao) {
        this.attachmentDao = attachmentDao;
    }
}
