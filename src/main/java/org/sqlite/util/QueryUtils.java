package org.sqlite.util;

import java.util.List;
import java.util.stream.Collectors;

public class QueryUtils {
    /**
     * Build a SQLite query using the VALUES clause to return arbitrary values.
     *
     * @param columns list of column names
     * @param valuesList values to return as rows
     * @return SQL query as string
     */
    public static String valuesQuery(List<String> columns, List<List<Object>> valuesList) {
        valuesList.forEach(
                (list) -> {
                    if (list.size() != columns.size())
                        throw new IllegalArgumentException(
                                "values and columns must have the same size");
                });
        return "with cte("
                + String.join(",", columns)
                + ") as (values "
                + valuesList.stream()
                        .map(
                                (values) ->
                                        "("
                                                + values.stream()
                                                        .map(
                                                                (o -> {
                                                                    if (o instanceof String)
                                                                        return "'" + o + "'";
                                                                    if (o == null) return "null";
                                                                    return o.toString();
                                                                }))
                                                        .collect(Collectors.joining(","))
                                                + ")")
                        .collect(Collectors.joining(","))
                + ") select * from cte";
    }

    // pattern for matching insert statements of the general format starting with INSERT or REPLACE.
    // CTEs used prior to the insert or replace keyword are not be permitted.
    public static boolean isInsertQuery(String sql) {
        if (sql != null && (sql.trim().toUpperCase().startsWith("INSERT ") ||
                            sql.trim().toUpperCase().startsWith("REPLACE "))) {
            return true;
        }
        return false;
    }

    public static String addReturningClause(String sql, String keys) {
        String separator = ";";
        String clause = "RETURNING";
        StringBuilder buffer = new StringBuilder();
        int count = sql.length() - sql.replace(separator, "").length();
        if (count == 1 && !sql.split(separator)[1].trim().isEmpty()) {
            count = 2;
        }
        if (sql.toUpperCase().indexOf(clause) != -1 || count > 1) {
            buffer.append(sql);
        }
        else {
            int index = count == 1 ? sql.indexOf(separator) : sql.length();
            buffer.append(sql.substring(0, index));
            buffer.append(" ");
            buffer.append(clause);
            buffer.append(" ");
            buffer.append(keys);
            buffer.append(sql.substring(index));
        }
        return buffer.toString();
    }


}
