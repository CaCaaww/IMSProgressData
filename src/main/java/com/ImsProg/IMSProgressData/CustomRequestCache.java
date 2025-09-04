package com.ImsProg.IMSProgressData;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
//import org.springframework.security.web.savedrequest.RequestCache;

public class CustomRequestCache extends HttpSessionRequestCache {

    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        if (uri != null && uri.contains("/.well-known/appspecific/com.chrome.devtools.json")) {
            // Skip saving this request
            return;
        }
        super.saveRequest(request, response);
    }
}