package net.weweave.tubewarder.dao;

import net.weweave.tubewarder.domain.Log;
import org.apache.commons.validator.GenericValidator;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class LogDao extends AbstractDao<Log> {
    /**
     * @param startDate minimum date (inclusive)
     * @param endDate maximum date (inclusive)
     * @param searchString string to search for (in content, subject, recipient name/address, and details)
     * @param keyword keyword to search for
     * @param firstResult position of the first result, numbered from 0
     * @param maxResults maximum number of results to retrieve
     * @return
     */
    public List<Log> getLogs(Date startDate, Date endDate, String keyword, String searchString, Integer firstResult, Integer maxResults) {
        if (firstResult < 0) {
            firstResult = 0;
        }
        if (maxResults > 1000 || maxResults < 1) {
            maxResults = 1000;
        }
        TypedQuery<Log> query = getEntityManager().createQuery("SELECT l FROM Log l " +
                "WHERE l.date >= :startDate AND l.date <= :endDate " +
                (GenericValidator.isBlankOrNull(keyword) ? "" : "AND l.keyword = :keyword ") +
                (GenericValidator.isBlankOrNull(searchString) ? "" : "AND (l.subject LIKE :searchString OR l.content LIKE :searchString OR l.recipientName LIKE :searchString OR l.recipientAddress LIKE :searchString OR l.details LIKE :searchString) ") +
                "ORDER BY l.date DESC", Log.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
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
}
