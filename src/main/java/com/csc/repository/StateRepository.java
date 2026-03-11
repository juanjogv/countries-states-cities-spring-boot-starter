package com.csc.repository;

import com.csc.model.State;
import com.csc.util.ResultSetUtils;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class StateRepository {

    private final JdbcTemplate jdbcTemplate;

    public StateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<State> rowMapper = (rs, rowNum) -> {
        State state = new State();
        state.setId(ResultSetUtils.getObject(rs, "id", Long.class));
        state.setName(ResultSetUtils.getString(rs, "name"));
        state.setCountryId(ResultSetUtils.getObject(rs, "country_id", Long.class));
        state.setCountryCode(ResultSetUtils.getString(rs, "country_code"));
        state.setFipsCode(ResultSetUtils.getString(rs, "fips_code"));
        state.setIso2(ResultSetUtils.getString(rs, "iso2"));
        state.setIso31662(ResultSetUtils.getString(rs, "iso3166_2"));
        state.setType(ResultSetUtils.getString(rs, "type"));
        state.setLevel(ResultSetUtils.getObject(rs, "level", Integer.class));
        state.setParentId(ResultSetUtils.getObject(rs, "parent_id", Long.class));
        state.setNativeName(ResultSetUtils.getString(rs, "native"));
        state.setLatitude(ResultSetUtils.getBigDecimal(rs, "latitude"));
        state.setLongitude(ResultSetUtils.getBigDecimal(rs, "longitude"));
        state.setTimezone(ResultSetUtils.getString(rs, "timezone"));
        state.setWikiDataId(ResultSetUtils.getString(rs, "wikiDataId"));
        state.setPopulation(ResultSetUtils.getString(rs, "population"));
        return state;
    };

    private static final String FIND_BY_COUNTRY_CODE_AND_ISO2_QUERY = """
            SELECT id,
                   name,
                   country_id,
                   country_code,
                   fips_code,
                   iso2,
                   iso3166_2,
                   type,
                   level,
                   parent_id,
                   native,
                   latitude,
                   longitude,
                   timezone,
                   translations,
                   wikiDataId,
                   population
            FROM states
            WHERE country_code = ? AND iso2 = ?
            LIMIT 1
            """;

    public Optional<State> findByCountryCodeAndIso2(String countryCode, String iso2) {
        List<State> results = jdbcTemplate.query(FIND_BY_COUNTRY_CODE_AND_ISO2_QUERY, rowMapper, countryCode, iso2);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    private static final String FIND_BY_COUNTRY_CODE_QUERY = """
            SELECT id,
                   name,
                   country_id,
                   country_code,
                   fips_code,
                   iso2,
                   iso3166_2,
                   type,
                   level,
                   parent_id,
                   native,
                   latitude,
                   longitude,
                   timezone,
                   translations,
                   wikiDataId,
                   population
            FROM states
            WHERE country_code = ?
            """;

    public List<State> findByCountryCode(String countryCode) {
        return jdbcTemplate.query(FIND_BY_COUNTRY_CODE_QUERY, rowMapper, countryCode);
    }

    // ── Dynamic field selection ──────────────────────────────────────────────

    private static final Map<String, String> FIELD_TO_COLUMN = Map.ofEntries(
            Map.entry("id", "id"),
            Map.entry("name", "name"),
            Map.entry("countryId", "country_id"),
            Map.entry("countryCode", "country_code"),
            Map.entry("fipsCode", "fips_code"),
            Map.entry("iso2", "iso2"),
            Map.entry("iso31662", "iso3166_2"),
            Map.entry("type", "type"),
            Map.entry("level", "level"),
            Map.entry("parentId", "parent_id"),
            Map.entry("nativeName", "native"),
            Map.entry("latitude", "latitude"),
            Map.entry("longitude", "longitude"),
            Map.entry("timezone", "timezone"),
            Map.entry("wikiDataId", "wikiDataId"),
            Map.entry("population", "population"));

    private static final Set<String> DEFAULT_FIELDS =
            Set.of("id", "name", "countryId", "countryCode", "iso2", "type", "latitude", "longitude");

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

    private final RowMapper<State> dynamicRowMapper = (rs, rowNum) -> {
        Set<String> cols = columnNames(rs);
        State state = new State();
        if (cols.contains("id")) state.setId(ResultSetUtils.getObject(rs, "id", Long.class));
        if (cols.contains("name")) state.setName(ResultSetUtils.getString(rs, "name"));
        if (cols.contains("country_id")) state.setCountryId(ResultSetUtils.getObject(rs, "country_id", Long.class));
        if (cols.contains("country_code")) state.setCountryCode(ResultSetUtils.getString(rs, "country_code"));
        if (cols.contains("fips_code")) state.setFipsCode(ResultSetUtils.getString(rs, "fips_code"));
        if (cols.contains("iso2")) state.setIso2(ResultSetUtils.getString(rs, "iso2"));
        if (cols.contains("iso3166_2")) state.setIso31662(ResultSetUtils.getString(rs, "iso3166_2"));
        if (cols.contains("type")) state.setType(ResultSetUtils.getString(rs, "type"));
        if (cols.contains("level")) state.setLevel(ResultSetUtils.getObject(rs, "level", Integer.class));
        if (cols.contains("parent_id")) state.setParentId(ResultSetUtils.getObject(rs, "parent_id", Long.class));
        if (cols.contains("native")) state.setNativeName(ResultSetUtils.getString(rs, "native"));
        if (cols.contains("latitude")) state.setLatitude(ResultSetUtils.getBigDecimal(rs, "latitude"));
        if (cols.contains("longitude")) state.setLongitude(ResultSetUtils.getBigDecimal(rs, "longitude"));
        if (cols.contains("timezone")) state.setTimezone(ResultSetUtils.getString(rs, "timezone"));
        if (cols.contains("wikidataid")) state.setWikiDataId(ResultSetUtils.getString(rs, "wikiDataId"));
        if (cols.contains("population")) state.setPopulation(ResultSetUtils.getString(rs, "population"));
        return state;
    };

    public List<State> findByCountryCode(String countryCode, List<String> fields) {
        Set<String> resolved = resolveFields(fields);
        String sql = "SELECT " + buildSelectClause(resolved) + " FROM states WHERE country_code = ?";
        return jdbcTemplate.query(sql, dynamicRowMapper, countryCode);
    }

    public Optional<State> findByCountryCodeAndIso2(String countryCode, String iso2, List<String> fields) {
        Set<String> resolved = resolveFields(fields);
        String sql =
                "SELECT " + buildSelectClause(resolved) + " FROM states WHERE country_code = ? AND iso2 = ? LIMIT 1";
        List<State> results = jdbcTemplate.query(sql, dynamicRowMapper, countryCode, iso2);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}
