package kr.co.fitapet.domain.domains.oauth.type.converter;

import com.kcy.fitapet.domain.oauth.type.ProviderType;
import com.kcy.fitapet.global.common.util.converter.AbstractLegacyEnumAttributeConverter;

public class ProviderTypeConverter extends AbstractLegacyEnumAttributeConverter<ProviderType> {
    private static final String ENUM_NAME = "제공자";

    public ProviderTypeConverter() {
        super(ProviderType.class, false, ENUM_NAME);
    }
}
