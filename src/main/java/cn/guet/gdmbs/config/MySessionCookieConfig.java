package cn.guet.gdmbs.config;

import javax.servlet.SessionCookieConfig;

/**
 *
 * @author liwei
 * 2019-10-08 02:53
 */
public interface MySessionCookieConfig extends SessionCookieConfig {

    String getDomainPattern();

    void setDomainPattern(String domainPattern);

    String getJvmRoute();

    void setJvmRoute(String jvmRoute);

    boolean isUseBase64Encoding();

    void setUseBase64Encoding(boolean useBase64Encoding);

}