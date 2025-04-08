package fr.jokay03j.myblog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class EnvironmentConfig {
  @Autowired
  private Environment environment;

  public String getProperty(String key) {
    return environment.getProperty(key);
  }

  public String getProperty(String key, String defaultValue) {
    return environment.getProperty(key, defaultValue);
  }

  public void setProperty(String key, String value) {
    System.setProperty(key, value);
  }

}
