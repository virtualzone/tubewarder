
package net.weweave.tubewarder.test;

import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

@ApplicationScoped
public class DbTestAssist {
    @PersistenceContext
    private EntityManager entityManager;

    public void cleanDb() {
        EntityManager em = getEntityManager();
        try {
            UserTransaction tx = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
            tx.begin();
            em.createNativeQuery("SET DATABASE REFERENTIAL INTEGRITY FALSE").executeUpdate();
            em.createNativeQuery("DELETE FROM AppToken").executeUpdate();
            em.createNativeQuery("DELETE FROM Attachment").executeUpdate();
            em.createNativeQuery("DELETE FROM Channel").executeUpdate();
            em.createNativeQuery("DELETE FROM ChannelTemplate").executeUpdate();
            em.createNativeQuery("DELETE FROM ConfigItem").executeUpdate();
            em.createNativeQuery("DELETE FROM Log").executeUpdate();
            em.createNativeQuery("DELETE FROM SendQueueItem").executeUpdate();
            em.createNativeQuery("DELETE FROM Session").executeUpdate();
            em.createNativeQuery("DELETE FROM Template").executeUpdate();
            em.createNativeQuery("DELETE FROM User").executeUpdate();
            em.createNativeQuery("DELETE FROM UserGroup").executeUpdate();
            em.createNativeQuery("DELETE FROM UserGroup_User").executeUpdate();
            // em.createNativeQuery("UPDATE hibernate_sequences SET sequence_next_hi_value = 1").executeUpdate();
            em.createNativeQuery("SET DATABASE REFERENTIAL INTEGRITY TRUE").executeUpdate();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execUpdate(String queryString) {
        EntityManager em = getEntityManager();
        try {
            UserTransaction tx = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
            tx.begin();
            em.createQuery(queryString).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
