package com.csc.repository;

import com.csc.model.Subregion;
import com.csc.util.ResultSetUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class SubregionRepository {

    private final JdbcTemplate jdbcTemplate;

    public SubregionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Subregion> rowMapper = (rs, rowNum) -> {
        Subregion subregion = new Subregion();
        subregion.setId(ResultSetUtils.getObject(rs, "id", Long.class));
        subregion.setName(ResultSetUtils.getString(rs, "name"));
        subregion.setTranslations(ResultSetUtils.getString(rs, "translations"));
        subregion.setRegionId(ResultSetUtils.getObject(rs, "region_id", Long.class));
        subregion.setWikiDataId(ResultSetUtils.getString(rs, "wikiDataId"));
        return subregion;
    };
}
