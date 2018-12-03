package jp.fintan.keel.spring.boot.autoconfigure.web.token;

import java.util.Collections;
import java.util.List;

import jp.fintan.keel.spring.web.token.transaction.TransactionTokenInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;

/**
 * トランサクショントークン機能のAuto-configurationで使用する設定値を保持するクラス。
 */
@ConfigurationProperties(prefix = "keel.transaction.token")
public class TransactionTokenProperties {

    public static final String TRANSACTION_TOKEN_PREFIX = "keel.transaction.token";

    /**
     * トランザクショントークン機能のAuto-configurationを有効にするか否かの設定値。
     */
    private boolean enabled = true;

    /**
     * {@link TransactionTokenInterceptor}を適用するパスのパターン。
     * @see InterceptorRegistration#addPathPatterns(String...)
     */
    private List<String> pathPatterns = Collections.emptyList();

    /**
     * {@link TransactionTokenInterceptor}の適用を除外するパスのパターン。
     * @see InterceptorRegistration#excludePathPatterns(String...)
     */
    private List<String> excludePathPatterns = Collections.emptyList();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getPathPatterns() {
        return pathPatterns;
    }

    public void setPathPatterns(List<String> pathPatterns) {
        this.pathPatterns = pathPatterns;
    }

    public List<String> getExcludePathPatterns() {
        return excludePathPatterns;
    }

    public void setExcludePathPatterns(List<String> excludePathPatterns) {
        this.excludePathPatterns = excludePathPatterns;
    }

}
