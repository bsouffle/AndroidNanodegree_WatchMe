package com.soufflet.mobile.watchme.dao;


import java.util.Date;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

import static com.soufflet.mobile.watchme.dao.InternalMoviesTableStructure.COLUMN_DESCRIPTION;
import static com.soufflet.mobile.watchme.dao.InternalMoviesTableStructure.COLUMN_ID;
import static com.soufflet.mobile.watchme.dao.InternalMoviesTableStructure.COLUMN_POSTER;
import static com.soufflet.mobile.watchme.dao.InternalMoviesTableStructure.COLUMN_RELEASE_DATE;
import static com.soufflet.mobile.watchme.dao.InternalMoviesTableStructure.COLUMN_TITLE;
import static com.soufflet.mobile.watchme.dao.InternalMoviesTableStructure.COLUMN_VOTE_AVERAGE;

@SimpleSQLTable(
        table = "FavoriteMovies",
        provider = "InternalMoviesProvider")
public class InternalMovie {

    @SimpleSQLColumn(value = COLUMN_ID, primary = true)
    public long movie_id;

    @SimpleSQLColumn(COLUMN_TITLE)
    public String title;

    @SimpleSQLColumn(COLUMN_DESCRIPTION)
    public String description;

    @SimpleSQLColumn(COLUMN_VOTE_AVERAGE)
    public double vote_average;

    @SimpleSQLColumn(COLUMN_RELEASE_DATE)
    public Date release_date;

    @SimpleSQLColumn(COLUMN_POSTER)
    public String poster;
}
