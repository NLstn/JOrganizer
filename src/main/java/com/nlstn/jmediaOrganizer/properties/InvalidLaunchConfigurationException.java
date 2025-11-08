package com.nlstn.jmediaOrganizer.properties;

/**
 * Exception representing invalid launch configuration arguments.
 */
public class InvalidLaunchConfigurationException extends IllegalArgumentException {

        private static final long serialVersionUID = 1L;

        private final String helpText;

        public InvalidLaunchConfigurationException(String message, String helpText) {
                super(message);
                this.helpText = helpText;
        }

        public InvalidLaunchConfigurationException(String message, String helpText, Throwable cause) {
                super(message, cause);
                this.helpText = helpText;
        }

        public String getHelpText() {
                return helpText;
        }
}
