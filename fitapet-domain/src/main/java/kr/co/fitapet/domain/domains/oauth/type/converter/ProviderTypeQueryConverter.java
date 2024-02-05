package kr.co.fitapet.domain.domains.oauth.type.converter;

import com.kcy.fitapet.domain.oauth.type.ProviderType;
import org.springframework.core.convert.converter.Converter;

public class ProviderTypeQueryConverter implements Converter<String, ProviderType> {
    @Override
    public ProviderType convert(String source) {
        try {
            return ProviderType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw null;
        }
    }
}
