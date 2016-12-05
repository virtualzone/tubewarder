package net.weweave.tubewarder.dao;

import net.weweave.tubewarder.domain.AppToken;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class AppTokenDao extends AbstractDao<AppToken> {
    @Override
    public void initObject(AppToken obj) {
        // Nothing to do
    }

    public List<AppToken> getAll() {
        TypedQuery<AppToken> query = getEntityManager().createQuery("SELECT at FROM AppToken at ORDER BY at.name", AppToken.class);
        List<AppToken> result = query.getResultList();
        initObject(result);
        return result;
    }
}
