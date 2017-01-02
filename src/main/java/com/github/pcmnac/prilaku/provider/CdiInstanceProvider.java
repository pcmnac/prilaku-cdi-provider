package com.github.pcmnac.prilaku.provider;

import javax.enterprise.inject.spi.CDI;

public class CdiInstanceProvider implements InstanceProvider
{

    public Object get(Class<?> type) throws Exception
    {
        return CDI.current().select(type).get();
    }

}
