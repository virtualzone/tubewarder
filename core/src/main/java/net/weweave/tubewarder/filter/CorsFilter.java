package net.weweave.tubewarder.filter;

import net.weweave.tubewarder.dao.ConfigItemDao;
import net.weweave.tubewarder.util.ConfigManager;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/rs/*")
public class CorsFilter implements Filter {
    @Inject
    private ConfigItemDao configItemDao;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Boolean corsEnabled = getConfigItemDao().getBool(ConfigManager.CONFIG_CORS_ENABLED, false);
        if (corsEnabled) {
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
            resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
            resp.addHeader("Access-Control-Max-Age", "1000");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    public ConfigItemDao getConfigItemDao() {
        return configItemDao;
    }

    public void setConfigItemDao(ConfigItemDao configItemDao) {
        this.configItemDao = configItemDao;
    }
}
