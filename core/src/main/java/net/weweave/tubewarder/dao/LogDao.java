package net.weweave.tubewarder.dao;

import net.weweave.tubewarder.domain.Log;
import net.weweave.tubewarder.domain.User;
import org.apache.commons.validator.GenericValidator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class LogDao extends AbstractDao<Log> {
    @Inject
    private UserGroupDao userGroupDao;

    @Inject
    private TemplateDao templateDao;

    @Inject
    private ChannelDao channelDao;

    /**
     * @param startDate minimum date (inclusive)
     * @param endDate maximum date (inclusive)
     * @param searchString string to search for (in content, subject, recipient name/address, and details)
     * @param keyword keyword to search for
     * @param firstResult position of the first result, numbered from 0
     * @param maxResults maximum number of results to retrieve
     * @return
     */
    public List<Log> getLogs(User user, Date startDate, Date endDate, String keyword, String searchString, Integer firstResult, Integer maxResults) {
        List<Long> groupMembershipIds = getUserGroupDao().getGroupMembershipIds(user);
        List<Long> templateIds = getTemplateDao().getTemplateIdsWithGroups(groupMembershipIds);
        List<Long> channelIds = getChannelDao().getChannelIdsWithGroups(groupMembershipIds);

        // If channelIds or templateIds is empty, no access to logs is possible
        // To avoid JPQL Exception, return empty list instantly
        if (channelIds == null ||
                templateIds == null ||
                channelIds.isEmpty() ||
                templateIds.isEmpty()) {
            return new ArrayList<>();
        }

        if (firstResult < 0) {
            firstResult = 0;
        }
        if (maxResults > 1000 || maxResults < 1) {
            maxResults = 1000;
        }
        TypedQuery<Log> query = getEntityManager().createQuery("SELECT l FROM Log l " +
                "WHERE l.date >= :startDate AND l.date <= :endDate " +
                "AND l.channelIdInt IN :channelIds " +
                "AND l.templateIdInt IN :templateIds " +
                (GenericValidator.isBlankOrNull(keyword) ? "" : "AND l.keyword = :keyword ") +
                (GenericValidator.isBlankOrNull(searchString) ? "" : "AND (l.subject LIKE :searchString OR l.content LIKE :searchString OR l.recipientName LIKE :searchString OR l.recipientAddress LIKE :searchString OR l.details LIKE :searchString) ") +
                "ORDER BY l.date DESC", Log.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("channelIds", channelIds);
        query.setParameter("templateIds", templateIds);
        if (!GenericValidator.isBlankOrNull(keyword)) {
            query.setParameter("keyword", keyword);
        }
        if (!GenericValidator.isBlankOrNull(searchString)) {
            query.setParameter("searchString", "%" + searchString + "%");
        }
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    public UserGroupDao getUserGroupDao() {
        return userGroupDao;
    }

    public void setUserGroupDao(UserGroupDao userGroupDao) {
        this.userGroupDao = userGroupDao;
    }

    public TemplateDao getTemplateDao() {
        return templateDao;
    }

    public void setTemplateDao(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    public ChannelDao getChannelDao() {
        return channelDao;
    }

    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
    }
}
