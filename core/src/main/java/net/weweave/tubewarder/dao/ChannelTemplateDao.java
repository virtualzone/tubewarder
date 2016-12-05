package net.weweave.tubewarder.dao;

import net.weweave.tubewarder.domain.ChannelTemplate;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.util.DbValueRetriever;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ChannelTemplateDao extends AbstractDao<ChannelTemplate> {
    @Inject
    private ChannelDao channelDao;

    @Inject
    private TemplateDao templateDao;

    @Override
    public void initObject(ChannelTemplate obj) {
        if (obj != null) {
            getTemplateDao().initObject(obj.getTemplate());
            getChannelDao().initObject(obj.getChannel());
        }
    }

    public ChannelTemplate getChannelTemplateByNames(String template, String channel) throws ObjectNotFoundException {
        TypedQuery<ChannelTemplate> query = getEntityManager().createQuery("SELECT ct FROM ChannelTemplate ct " +
                "WHERE ct.template.name = :template AND ct.channel.name = :channel", ChannelTemplate.class);
        query.setMaxResults(1);
        query.setParameter("template", template);
        query.setParameter("channel", channel);
        ChannelTemplate result = (ChannelTemplate) DbValueRetriever.getObjectOrException(query);
        initObject(result);
        return result;
    }

    public ChannelTemplate getChannelTemplateById(String templateId, String channelId) throws ObjectNotFoundException {
        TypedQuery<ChannelTemplate> query = getEntityManager().createQuery("SELECT ct FROM ChannelTemplate ct " +
                "WHERE ct.template.exposableId = :templateId AND ct.channel.exposableId = :channelId", ChannelTemplate.class);
        query.setMaxResults(1);
        query.setParameter("templateId", templateId);
        query.setParameter("channelId", channelId);
        ChannelTemplate result = (ChannelTemplate) DbValueRetriever.getObjectOrException(query);
        initObject(result);
        return result;
    }

    public List<ChannelTemplate> getChannelTemplatesForTemplate(Long templateId) {
        TypedQuery<ChannelTemplate> query = getEntityManager().createQuery("SELECT ct FROM ChannelTemplate ct " +
                "WHERE ct.template.id = :templateId " +
                "ORDER BY ct.channel.name ASC", ChannelTemplate.class);
        query.setParameter("templateId", templateId);
        List<ChannelTemplate> result = query.getResultList();
        initObject(result);
        return result;
    }

    public List<ChannelTemplate> getChannelTemplatesForChannel(Long channelId) {
        TypedQuery<ChannelTemplate> query = getEntityManager().createQuery("SELECT ct FROM ChannelTemplate ct " +
                "WHERE ct.channel.id = :channelId " +
                "ORDER BY ct.template.name ASC", ChannelTemplate.class);
        query.setParameter("channelId", channelId);
        List<ChannelTemplate> result = query.getResultList();
        initObject(result);
        return result;
    }

    public ChannelDao getChannelDao() {
        return channelDao;
    }

    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
    }

    public TemplateDao getTemplateDao() {
        return templateDao;
    }

    public void setTemplateDao(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }
}
