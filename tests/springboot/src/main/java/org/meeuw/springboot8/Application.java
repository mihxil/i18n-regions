package org.meeuw.springboot8;

import org.meeuw.i18n.openlocationcode.OpenLocationProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = org.meeuw.springutils.RegionsController.class)
public class Application {
    public static void main(String[] args) {
        OpenLocationProvider.setMaxLength(1);
        SpringApplication.run(Application.class, args);
    }
}
