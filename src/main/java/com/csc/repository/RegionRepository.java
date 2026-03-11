package com.csc.repository;

import com.csc.model.Region;
import com.csc.util.ResultSetUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class RegionRepository {

    private final JdbcTemplate jdbcTemplate;

    public RegionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Region> rowMapper = (rs, rowNum) -> {
        Region region = new Region();
        region.setId(ResultSetUtils.getObject(rs, "id", Long.class));
        region.setName(ResultSetUtils.getString(rs, "name"));
        region.setTranslations(ResultSetUtils.getString(rs, "translations"));
        region.setWikiDataId(ResultSetUtils.getString(rs, "wikiDataId"));
        return region;
    };
}
