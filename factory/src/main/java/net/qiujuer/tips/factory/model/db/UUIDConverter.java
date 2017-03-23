package net.qiujuer.tips.factory.model.db;


import com.raizlabs.android.dbflow.converter.TypeConverter;

import java.util.UUID;

/**
 * Created by JuQiu
 * on 16/6/25.
 */
public class UUIDConverter extends TypeConverter<String, UUID> {

    @Override
    public String getDBValue(UUID model) {
        return model.toString();
    }

    @Override
    public UUID getModelValue(String data) {
        return UUID.fromString(data);
    }
}
