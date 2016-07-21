package net.weweave.tubewarder.filter;

import net.weweave.tubewarder.dao.ConfigItemDao;
import net.weweave.tubewarder.util.ConfigManager;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/rs/*")
public class TermsFilter implements Filter {
    @Inject
    private ConfigItemDao configItemDao;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        String path = req.getPathInfo();
        Boolean termsAccepted = getConfigItemDao().getBool(ConfigManager.CONFIG_TERMS_ACCEPTED, false);
        if (!termsAccepted && !isAllowedPathForUnacceptedTerms(path)) {
            HttpServletResponse resp = (HttpServletResponse)servletResponse;
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isAllowedPathForUnacceptedTerms(String path) {
        if (path.endsWith("/checklicense")) return true;
        if (path.endsWith("/acceptterms")) return true;
        if (path.endsWith("/logout")) return true;
        return false;
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
