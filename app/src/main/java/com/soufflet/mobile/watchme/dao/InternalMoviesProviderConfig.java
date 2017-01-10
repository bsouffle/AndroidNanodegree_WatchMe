package com.soufflet.mobile.watchme.dao;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

@SimpleSQLConfig(
        name = "InternalMoviesProvider",
        authority = "com.soufflet.mobile.watchme",
        database = "watchme.db",
        version = 1)
public class InternalMoviesProviderConfig implements ProviderConfig {

    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }

}
