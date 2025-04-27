package com.example.NotificationService.exception;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Component
@Profile("dev")  // Active only when the "dev" profile is set
public class DevGlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    private static final Logger logger = LoggerFactory.getLogger(DevGlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex) throws Exception {
//        logger.error("Internal Server Error: ", ex);  // Logs the error

        // Just throw the exception in development mode for debugging
        throw ex;
    }
}

