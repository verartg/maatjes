//package com.example.maatjes.util;
//
//public class CustomAccessDeniedHandler implements AccessDeniedHandler {
//
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        response.setContentType("application/json");
//        String message = "Access denied. You don't have sufficient privileges to access this resource.";
//
//        // Check if the access denied exception is your custom exception
//        if (accessDeniedException instanceof com.example.maatjes.exceptions.AccessDeniedException) {
//            message = accessDeniedException.getMessage();
//        }
//
//        response.getWriter().write(message);
//    }
//}




