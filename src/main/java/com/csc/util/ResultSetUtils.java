package com.csc.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Null-safe helpers for reading values from a {@link ResultSet} inside a RowMapper.
 * <p>
 * Every method calls {@link ResultSet#getObject(String)} first. If the result is
 * {@code null} (SQL NULL), it returns {@code null}. Otherwise it delegates to the
 * appropriate native getter so the JDBC driver handles the type conversion.
 * Both column-name and column-index overloads are provided for every type.
 */
public final class ResultSetUtils {

    private ResultSetUtils() {}

    // ── String ───────────────────────────────────────────────────────────────

    public static String getString(ResultSet rs, String column) throws SQLException {
        return rs.getObject(column) != null ? rs.getString(column) : null;
    }

    public static String getString(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex) != null ? rs.getString(columnIndex) : null;
    }

    // ── Boolean ──────────────────────────────────────────────────────────────

    public static boolean getBoolean(ResultSet rs, String column) throws SQLException {
        return rs.getBoolean(column);
    }

    public static boolean getBoolean(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBoolean(columnIndex);
    }

    // ── Byte ─────────────────────────────────────────────────────────────────

    public static byte getByte(ResultSet rs, String column) throws SQLException {
        return rs.getByte(column);
    }

    public static byte getByte(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getByte(columnIndex);
    }

    // ── Short ────────────────────────────────────────────────────────────────

    public static short getShort(ResultSet rs, String column) throws SQLException {
        return rs.getShort(column);
    }

    public static short getShort(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getShort(columnIndex);
    }

    // ── Integer ──────────────────────────────────────────────────────────────

    public static int getInt(ResultSet rs, String column) throws SQLException {
        return rs.getInt(column);
    }

    public static int getInt(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getInt(columnIndex);
    }

    // ── Long ─────────────────────────────────────────────────────────────────

    public static long getLong(ResultSet rs, String column) throws SQLException {
        return rs.getLong(column);
    }

    public static long getLong(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getLong(columnIndex);
    }

    // ── Float ────────────────────────────────────────────────────────────────

    public static float getFloat(ResultSet rs, String column) throws SQLException {
        return rs.getFloat(column);
    }

    public static float getFloat(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getFloat(columnIndex);
    }

    // ── Double ───────────────────────────────────────────────────────────────

    public static double getDouble(ResultSet rs, String column) throws SQLException {
        return rs.getDouble(column);
    }

    public static double getDouble(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getDouble(columnIndex);
    }

    // ── BigDecimal ───────────────────────────────────────────────────────────

    public static BigDecimal getBigDecimal(ResultSet rs, String column) throws SQLException {
        return rs.getObject(column) != null ? rs.getBigDecimal(column) : null;
    }

    public static BigDecimal getBigDecimal(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex) != null ? rs.getBigDecimal(columnIndex) : null;
    }

    // ── byte[] ───────────────────────────────────────────────────────────────

    public static byte[] getBytes(ResultSet rs, String column) throws SQLException {
        return rs.getBytes(column);
    }

    public static byte[] getBytes(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBytes(columnIndex);
    }

    // ── java.sql.Date ────────────────────────────────────────────────────────

    public static Date getDate(ResultSet rs, String column) throws SQLException {
        return rs.getObject(column) != null ? rs.getDate(column) : null;
    }

    public static Date getDate(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex) != null ? rs.getDate(columnIndex) : null;
    }

    // ── java.sql.Time ────────────────────────────────────────────────────────

    public static Time getTime(ResultSet rs, String column) throws SQLException {
        return rs.getObject(column) != null ? rs.getTime(column) : null;
    }

    public static Time getTime(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex) != null ? rs.getTime(columnIndex) : null;
    }

    // ── java.sql.Timestamp ───────────────────────────────────────────────────

    public static Timestamp getTimestamp(ResultSet rs, String column) throws SQLException {
        return rs.getObject(column) != null ? rs.getTimestamp(column) : null;
    }

    public static Timestamp getTimestamp(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex) != null ? rs.getTimestamp(columnIndex) : null;
    }

    // ── LocalDate ────────────────────────────────────────────────────────────

    public static LocalDate getLocalDate(ResultSet rs, String column) throws SQLException {
        return rs.getObject(column) != null ? rs.getDate(column).toLocalDate() : null;
    }

    public static LocalDate getLocalDate(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex) != null ? rs.getDate(columnIndex).toLocalDate() : null;
    }

    // ── LocalTime ────────────────────────────────────────────────────────────

    public static LocalTime getLocalTime(ResultSet rs, String column) throws SQLException {
        return rs.getObject(column) != null ? rs.getTime(column).toLocalTime() : null;
    }

    public static LocalTime getLocalTime(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex) != null ? rs.getTime(columnIndex).toLocalTime() : null;
    }

    // ── LocalDateTime ────────────────────────────────────────────────────────

    public static LocalDateTime getLocalDateTime(ResultSet rs, String column) throws SQLException {
        return rs.getObject(column) != null ? rs.getTimestamp(column).toLocalDateTime() : null;
    }

    public static LocalDateTime getLocalDateTime(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex) != null ? rs.getTimestamp(columnIndex).toLocalDateTime() : null;
    }

    // ── Object ───────────────────────────────────────────────────────────────

    public static Object getObject(ResultSet rs, String column) throws SQLException {
        return rs.getObject(column);
    }

    public static Object getObject(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex);
    }

    public static <T> T getObject(ResultSet rs, String column, Class<T> type) throws SQLException {
        return rs.getObject(column) != null ? rs.getObject(column, type) : null;
    }

    public static <T> T getObject(ResultSet rs, int columnIndex, Class<T> type) throws SQLException {
        return rs.getObject(columnIndex) != null ? rs.getObject(columnIndex, type) : null;
    }
}
