package app.web.filter;

import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Cross-Origin Resource Sharing (CORS)
 *
 * @see <a href="http://www.html5rocks.com/en/tutorials/cors/#toc-adding-cors-support-to-the-server">Using CORS</a>
 */
public class CorsFilter implements Filter {

    public static final int ONE_SECOND = 60;
    public static final int ONE_WEEK_IN_SECONDS = ONE_SECOND * 60 * 24 * 7;

    public static final Set<String> DEFAULT_CUSTOM_HEADERS = new HashSet<String>(Arrays.asList(
            "X-Requested-With"  // jquery header X-Requested-With: XMLHttpRequest
    ));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (isPreflight(request)) {
            emitAllowOrigin(request, response);
            emitAllowHeaders(request, response);
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Max-Age", Integer.toString(ONE_WEEK_IN_SECONDS));

            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        if (isCors(request)) {
            emitAllowOrigin(request, response);
        }

        chain.doFilter(request, response);
    }

    private void emitAllowHeaders(HttpServletRequest request, HttpServletResponse response) {
        Set<String> headers = new HashSet<String>(DEFAULT_CUSTOM_HEADERS);
        String requestedHeaders = request.getHeader("Access-Control-Request-Headers");
        for (String h : requestedHeaders.split(",")) {
            headers.add(h.trim());
        }
        response.addHeader("Access-Control-Allow-Headers", StringUtils.join(headers, ", "));
    }

    private void emitAllowOrigin(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        if (allowOrigin(origin)) {
            response.addHeader("Access-Control-Allow-Origin", origin);
        }
    }

    private boolean allowOrigin(String origin) {
//        return origin.endsWith(".ingenuity.com");
        return true;
    }

    private boolean isCors(HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        return StringUtils.isNotBlank(origin);
    }

    private boolean isPreflight(HttpServletRequest request) {
        String requestMethod = request.getMethod();
        String acrHeaders = request.getHeader("Access-Control-Request-Headers");

        boolean isCors = isCors(request);
        boolean isOptionsRequest = "OPTIONS".equals(requestMethod);
        boolean hasAcRHeader = StringUtils.isNotBlank(acrHeaders);

        return isCors && isOptionsRequest && hasAcRHeader;
    }

    @Override
    public void destroy() {

    }
}
