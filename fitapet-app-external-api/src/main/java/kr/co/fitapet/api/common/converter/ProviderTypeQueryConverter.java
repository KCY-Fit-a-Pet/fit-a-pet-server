package kr.co.fitapet.api.common.converter;


import kr.co.fitapet.domain.domains.oauth.type.ProviderType;
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
