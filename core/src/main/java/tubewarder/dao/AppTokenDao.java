package tubewarder.dao;

import tubewarder.domain.AppToken;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.List;

@ApplicationScoped
public class AppTokenDao extends AbstractDao<AppToken> {
    public List<AppToken> getAll() {
        TypedQuery<AppToken> query = getEntityManager().createQuery("SELECT at FROM AppToken at ORDER BY at.name", AppToken.class);
        return query.getResultList();
    }
}
