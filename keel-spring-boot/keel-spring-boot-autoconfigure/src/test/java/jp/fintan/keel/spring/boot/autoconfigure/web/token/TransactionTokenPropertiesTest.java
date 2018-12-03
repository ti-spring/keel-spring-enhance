package jp.fintan.keel.spring.boot.autoconfigure.web.token;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Lists;

import org.junit.Test;

public class TransactionTokenPropertiesTest {

    private final TransactionTokenProperties properties = new TransactionTokenProperties();

    @Test
    public void defaultEnabled() {
        assertThat(properties.isEnabled()).isTrue();
    }

    @Test
    public void customEnabled() {
        properties.setEnabled(false);
        assertThat(properties.isEnabled()).isFalse();
    }

    @Test
    public void defaultPathPatterns() {
        assertThat(properties.getPathPatterns()).isEmpty();
    }

    @Test
    public void customPathPatterns() {
        properties.setPathPatterns(Lists.newArrayList("/user/**", "/item/**"));
        assertThat(properties.getPathPatterns()).containsExactly("/user/**", "/item/**");
    }

    @Test
    public void defaultExcludePathPatterns() {
        assertThat(properties.getExcludePathPatterns()).isEmpty();
    }

    @Test
    public void customExcludePathPatterns() {
        properties.setExcludePathPatterns(Lists.newArrayList("/admin/**", "/secure/**"));
        assertThat(properties.getExcludePathPatterns()).containsExactly("/admin/**", "/secure/**");
    }
}
