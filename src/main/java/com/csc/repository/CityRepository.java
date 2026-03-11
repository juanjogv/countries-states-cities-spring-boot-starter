package com.csc.repository;

import com.csc.model.City;
import com.csc.util.ResultSetUtils;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class CityRepository {

    private final JdbcTemplate jdbcTemplate;

    public CityRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<City> rowMapper = (rs, rowNum) -> {
        City city = new City();
        city.setId(ResultSetUtils.getObject(rs, "id", Long.class));
        city.setName(ResultSetUtils.getString(rs, "name"));
        city.setStateId(ResultSetUtils.getObject(rs, "state_id", Long.class));
        city.setStateCode(ResultSetUtils.getString(rs, "state_code"));
        city.setCountryId(ResultSetUtils.getObject(rs, "country_id", Long.class));
        city.setCountryCode(ResultSetUtils.getString(rs, "country_code"));
        city.setType(ResultSetUtils.getString(rs, "type"));
        city.setLevel(ResultSetUtils.getObject(rs, "level", Integer.class));
        city.setParentId(ResultSetUtils.getObject(rs, "parent_id", Long.class));
        city.setLatitude(ResultSetUtils.getBigDecimal(rs, "latitude"));
        city.setLongitude(ResultSetUtils.getBigDecimal(rs, "longitude"));
        city.setNativeName(ResultSetUtils.getString(rs, "native"));
        city.setPopulation(ResultSetUtils.getObject(rs, "population", Long.class));
        city.setTimezone(ResultSetUtils.getString(rs, "timezone"));
        city.setTranslations(ResultSetUtils.getString(rs, "translations"));
        city.setWikiDataId(ResultSetUtils.getString(rs, "wikiDataId"));
        return city;
    };

    private static final String FIND_BY_COUNTRY_CODE_AND_STATE_CODE_QUERY = """
            SELECT id,
                   name,
                   state_id,
                   state_code,
                   country_id,
                   country_code,
                   type,
                   level,
                   parent_id,
                   latitude,
                   longitude,
                   native,
                   population,
                   timezone,
                   translations,
                   wikiDataId
            FROM cities
            WHERE country_code = ? AND state_code = ?
            """;

    public List<City> findByCountryCodeAndStateCode(String countryCode, String stateCode) {
        return jdbcTemplate.query(FIND_BY_COUNTRY_CODE_AND_STATE_CODE_QUERY, rowMapper, countryCode, stateCode);
    }

    private static final String FIND_BY_COUNTRY_CODE = """
            SELECT id,
                   name,
                   state_id,
                   state_code,
                   country_id,
                   country_code,
                   type,
                   level,
                   parent_id,
                   latitude,
                   longitude,
                   native,
                   population,
                   timezone,
                   translations,
                   wikiDataId
            FROM cities
            WHERE country_code = ?
            """;

    public List<City> findByCountryCode(String countryCode) {
        return jdbcTemplate.query(FIND_BY_COUNTRY_CODE, rowMapper, countryCode);
    }

    // ── Dynamic field selection ──────────────────────────────────────────────

    private static final Map<String, String> FIELD_TO_COLUMN = Map.ofEntries(
            Map.entry("id", "id"),
            Map.entry("name", "name"),
            Map.entry("stateId", "state_id"),
            Map.entry("stateCode", "state_code"),
            Map.entry("countryId", "country_id"),
            Map.entry("countryCode", "country_code"),
            Map.entry("type", "type"),
            Map.entry("level", "level"),
            Map.entry("parentId", "parent_id"),
            Map.entry("latitude", "latitude"),
            Map.entry("longitude", "longitude"),
            Map.entry("nativeName", "native"),
            Map.entry("population", "population"),
            Map.entry("timezone", "timezone"),
            Map.entry("translations", "translations"),
            Map.entry("wikiDataId", "wikiDataId"));

    private static final Set<String> DEFAULT_FIELDS = Set.of("id", "name");

    private Set<String> resolveFields(List<String> requested) {
        if (requested == null || requested.isEmpty()) return DEFAULT_FIELDS;
        Set<String> valid = new HashSet<>(requested);
        valid.retainAll(FIELD_TO_COLUMN.keySet());
        return valid.isEmpty() ? DEFAULT_FIELDS : valid;
    }

    private String buildSelectClause(Set<String> fields) {
        return fields.stream().map(FIELD_TO_COLUMN::get).collect(Collectors.joining(", "));
    }

    private static Set<String> columnNames(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        Set<String> names = new HashSet<>(meta.getColumnCount());
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            names.add(meta.getColumnName(i).toLowerCase());
        }
        return names;
    }

    private final RowMapper<City> dynamicRowMapper = (rs, rowNum) -> {
        Set<String> cols = columnNames(rs);
        City city = new City();
        if (cols.contains("id")) city.setId(ResultSetUtils.getObject(rs, "id", Long.class));
        if (cols.contains("name")) city.setName(ResultSetUtils.getString(rs, "name"));
        if (cols.contains("state_id")) city.setStateId(ResultSetUtils.getObject(rs, "state_id", Long.class));
        if (cols.contains("state_code")) city.setStateCode(ResultSetUtils.getString(rs, "state_code"));
        if (cols.contains("country_id")) city.setCountryId(ResultSetUtils.getObject(rs, "country_id", Long.class));
        if (cols.contains("country_code")) city.setCountryCode(ResultSetUtils.getString(rs, "country_code"));
        if (cols.contains("type")) city.setType(ResultSetUtils.getString(rs, "type"));
        if (cols.contains("level")) city.setLevel(ResultSetUtils.getObject(rs, "level", Integer.class));
        if (cols.contains("parent_id")) city.setParentId(ResultSetUtils.getObject(rs, "parent_id", Long.class));
        if (cols.contains("latitude")) city.setLatitude(ResultSetUtils.getBigDecimal(rs, "latitude"));
        if (cols.contains("longitude")) city.setLongitude(ResultSetUtils.getBigDecimal(rs, "longitude"));
        if (cols.contains("native")) city.setNativeName(ResultSetUtils.getString(rs, "native"));
        if (cols.contains("population")) city.setPopulation(ResultSetUtils.getObject(rs, "population", Long.class));
        if (cols.contains("timezone")) city.setTimezone(ResultSetUtils.getString(rs, "timezone"));
        if (cols.contains("translations")) city.setTranslations(ResultSetUtils.getString(rs, "translations"));
        if (cols.contains("wikidataid")) city.setWikiDataId(ResultSetUtils.getString(rs, "wikiDataId"));
        return city;
    };

    public List<City> findByCountryCode(String countryCode, List<String> fields) {
        Set<String> resolved = resolveFields(fields);
        String sql = "SELECT " + buildSelectClause(resolved) + " FROM cities WHERE country_code = ?";
        return jdbcTemplate.query(sql, dynamicRowMapper, countryCode);
    }

    public List<City> findByCountryCodeAndStateCode(String countryCode, String stateCode, List<String> fields) {
        Set<String> resolved = resolveFields(fields);
        String sql = "SELECT " + buildSelectClause(resolved) + " FROM cities WHERE country_code = ? AND state_code = ?";
        return jdbcTemplate.query(sql, dynamicRowMapper, countryCode, stateCode);
    }
}
