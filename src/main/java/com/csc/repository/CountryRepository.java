package com.csc.repository;

import com.csc.model.Country;
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

public class CountryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CountryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Country> rowMapper = (rs, rowNum) -> {
        Country country = new Country();
        country.setId(ResultSetUtils.getObject(rs, "id", Long.class));
        country.setName(ResultSetUtils.getString(rs, "name"));
        country.setIso3(ResultSetUtils.getString(rs, "iso3"));
        country.setNumericCode(ResultSetUtils.getString(rs, "numeric_code"));
        country.setIso2(ResultSetUtils.getString(rs, "iso2"));
        country.setPhoneCode(ResultSetUtils.getString(rs, "phonecode"));
        country.setCapital(ResultSetUtils.getString(rs, "capital"));
        country.setCurrency(ResultSetUtils.getString(rs, "currency"));
        country.setCurrencyName(ResultSetUtils.getString(rs, "currency_name"));
        country.setCurrencySymbol(ResultSetUtils.getString(rs, "currency_symbol"));
        country.setTld(ResultSetUtils.getString(rs, "tld"));
        country.setNativeName(ResultSetUtils.getString(rs, "native"));
        country.setPopulation(ResultSetUtils.getObject(rs, "population", Long.class));
        country.setGdp(ResultSetUtils.getObject(rs, "gdp", Long.class));
        country.setRegion(ResultSetUtils.getString(rs, "region"));
        country.setRegionId(ResultSetUtils.getObject(rs, "region_id", Long.class));
        country.setSubregion(ResultSetUtils.getString(rs, "subregion"));
        country.setSubregionId(ResultSetUtils.getObject(rs, "subregion_id", Long.class));
        country.setNationality(ResultSetUtils.getString(rs, "nationality"));
        country.setAreaSqKm(ResultSetUtils.getObject(rs, "area_sq_km", Double.class));
        country.setPostalCodeFormat(ResultSetUtils.getString(rs, "postal_code_format"));
        country.setPostalCodeRegex(ResultSetUtils.getString(rs, "postal_code_regex"));
        country.setTimezones(ResultSetUtils.getString(rs, "timezones"));
        country.setLatitude(ResultSetUtils.getBigDecimal(rs, "latitude"));
        country.setLongitude(ResultSetUtils.getBigDecimal(rs, "longitude"));
        country.setEmoji(ResultSetUtils.getString(rs, "emoji"));
        country.setEmojiU(ResultSetUtils.getString(rs, "emojiU"));
        country.setWikiDataId(ResultSetUtils.getString(rs, "wikiDataId"));
        return country;
    };

    private static final String FIND_ALL_QUERY = """
            SELECT id,
                   name,
                   iso3,
                   numeric_code,
                   iso2,
                   phonecode,
                   capital,
                   currency,
                   currency_name,
                   currency_symbol,
                   tld,
                   native,
                   population,
                   gdp,
                   region,
                   region_id,
                   subregion,
                   subregion_id,
                   nationality,
                   area_sq_km,
                   postal_code_format,
                   postal_code_regex,
                   timezones,
                   translations,
                   latitude,
                   longitude,
                   emoji,
                   emojiU,
                   wikiDataId
            FROM countries
            ORDER BY name
            """;

    public List<Country> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, rowMapper);
    }

    private static final String FIND_BY_ISO2_QUERY = """
            SELECT id,
                   name,
                   iso3,
                   numeric_code,
                   iso2,
                   phonecode,
                   capital,
                   currency,
                   currency_name,
                   currency_symbol,
                   tld,
                   native,
                   population,
                   gdp,
                   region,
                   region_id,
                   subregion,
                   subregion_id,
                   nationality,
                   area_sq_km,
                   postal_code_format,
                   postal_code_regex,
                   timezones,
                   translations,
                   latitude,
                   longitude,
                   emoji,
                   emojiU,
                   wikiDataId
            FROM countries
            WHERE iso2 = ?
            LIMIT 1
            """;

    public Optional<Country> findByIso2(String iso2) {
        List<Country> results = jdbcTemplate.query(FIND_BY_ISO2_QUERY, rowMapper, iso2);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    // ── Dynamic field selection ──────────────────────────────────────────────

    private static final Map<String, String> FIELD_TO_COLUMN = Map.ofEntries(
            Map.entry("id", "id"),
            Map.entry("name", "name"),
            Map.entry("iso3", "iso3"),
            Map.entry("numericCode", "numeric_code"),
            Map.entry("iso2", "iso2"),
            Map.entry("phoneCode", "phonecode"),
            Map.entry("capital", "capital"),
            Map.entry("currency", "currency"),
            Map.entry("currencyName", "currency_name"),
            Map.entry("currencySymbol", "currency_symbol"),
            Map.entry("tld", "tld"),
            Map.entry("nativeName", "native"),
            Map.entry("population", "population"),
            Map.entry("gdp", "gdp"),
            Map.entry("region", "region"),
            Map.entry("regionId", "region_id"),
            Map.entry("subregion", "subregion"),
            Map.entry("subregionId", "subregion_id"),
            Map.entry("nationality", "nationality"),
            Map.entry("areaSqKm", "area_sq_km"),
            Map.entry("postalCodeFormat", "postal_code_format"),
            Map.entry("postalCodeRegex", "postal_code_regex"),
            Map.entry("timezones", "timezones"),
            Map.entry("latitude", "latitude"),
            Map.entry("longitude", "longitude"),
            Map.entry("emoji", "emoji"),
            Map.entry("emojiU", "emojiU"),
            Map.entry("wikiDataId", "wikiDataId"));

    private static final Set<String> DEFAULT_FIELDS =
            Set.of("id", "name", "iso2", "iso3", "phoneCode", "capital", "currency", "nativeName", "emoji");

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

    private final RowMapper<Country> dynamicRowMapper = (rs, rowNum) -> {
        Set<String> cols = columnNames(rs);
        Country country = new Country();
        if (cols.contains("id")) country.setId(ResultSetUtils.getObject(rs, "id", Long.class));
        if (cols.contains("name")) country.setName(ResultSetUtils.getString(rs, "name"));
        if (cols.contains("iso3")) country.setIso3(ResultSetUtils.getString(rs, "iso3"));
        if (cols.contains("numeric_code")) country.setNumericCode(ResultSetUtils.getString(rs, "numeric_code"));
        if (cols.contains("iso2")) country.setIso2(ResultSetUtils.getString(rs, "iso2"));
        if (cols.contains("phonecode")) country.setPhoneCode(ResultSetUtils.getString(rs, "phonecode"));
        if (cols.contains("capital")) country.setCapital(ResultSetUtils.getString(rs, "capital"));
        if (cols.contains("currency")) country.setCurrency(ResultSetUtils.getString(rs, "currency"));
        if (cols.contains("currency_name")) country.setCurrencyName(ResultSetUtils.getString(rs, "currency_name"));
        if (cols.contains("currency_symbol"))
            country.setCurrencySymbol(ResultSetUtils.getString(rs, "currency_symbol"));
        if (cols.contains("tld")) country.setTld(ResultSetUtils.getString(rs, "tld"));
        if (cols.contains("native")) country.setNativeName(ResultSetUtils.getString(rs, "native"));
        if (cols.contains("population")) country.setPopulation(ResultSetUtils.getObject(rs, "population", Long.class));
        if (cols.contains("gdp")) country.setGdp(ResultSetUtils.getObject(rs, "gdp", Long.class));
        if (cols.contains("region")) country.setRegion(ResultSetUtils.getString(rs, "region"));
        if (cols.contains("region_id")) country.setRegionId(ResultSetUtils.getObject(rs, "region_id", Long.class));
        if (cols.contains("subregion")) country.setSubregion(ResultSetUtils.getString(rs, "subregion"));
        if (cols.contains("subregion_id"))
            country.setSubregionId(ResultSetUtils.getObject(rs, "subregion_id", Long.class));
        if (cols.contains("nationality")) country.setNationality(ResultSetUtils.getString(rs, "nationality"));
        if (cols.contains("area_sq_km")) country.setAreaSqKm(ResultSetUtils.getObject(rs, "area_sq_km", Double.class));
        if (cols.contains("postal_code_format"))
            country.setPostalCodeFormat(ResultSetUtils.getString(rs, "postal_code_format"));
        if (cols.contains("postal_code_regex"))
            country.setPostalCodeRegex(ResultSetUtils.getString(rs, "postal_code_regex"));
        if (cols.contains("timezones")) country.setTimezones(ResultSetUtils.getString(rs, "timezones"));
        if (cols.contains("latitude")) country.setLatitude(ResultSetUtils.getBigDecimal(rs, "latitude"));
        if (cols.contains("longitude")) country.setLongitude(ResultSetUtils.getBigDecimal(rs, "longitude"));
        if (cols.contains("emoji")) country.setEmoji(ResultSetUtils.getString(rs, "emoji"));
        if (cols.contains("emojiu")) country.setEmojiU(ResultSetUtils.getString(rs, "emojiU"));
        if (cols.contains("wikidataid")) country.setWikiDataId(ResultSetUtils.getString(rs, "wikiDataId"));
        return country;
    };

    public List<Country> findAll(List<String> fields) {
        Set<String> resolved = resolveFields(fields);
        String sql = "SELECT " + buildSelectClause(resolved) + " FROM countries ORDER BY name";
        return jdbcTemplate.query(sql, dynamicRowMapper);
    }

    public Optional<Country> findByIso2(String iso2, List<String> fields) {
        Set<String> resolved = resolveFields(fields);
        String sql = "SELECT " + buildSelectClause(resolved) + " FROM countries WHERE iso2 = ? LIMIT 1";
        List<Country> results = jdbcTemplate.query(sql, dynamicRowMapper, iso2);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}
