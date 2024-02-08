package kr.co.fitapet.domain.common.converter;


import kr.co.fitapet.domain.common.util.converter.AbstractLegacyEnumAttributeConverter;
import kr.co.fitapet.domain.domains.oauth.type.ProviderType;

public class ProviderTypeConverter extends AbstractLegacyEnumAttributeConverter<ProviderType> {
    private static final String ENUM_NAME = "제공자";

    public ProviderTypeConverter() {
        super(ProviderType.class, false, ENUM_NAME);
    }
}
