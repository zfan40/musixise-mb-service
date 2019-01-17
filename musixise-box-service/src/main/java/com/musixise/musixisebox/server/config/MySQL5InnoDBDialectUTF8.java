package com.musixise.musixisebox.server.config;

import org.hibernate.dialect.MySQL5InnoDBDialect;

public class MySQL5InnoDBDialectUTF8 extends MySQL5InnoDBDialect {

    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    }
}
