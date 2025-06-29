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
    public static boolean isInsertQuery(String sql, String... cols) {
        boolean is = false;
        if (sql != null && cols.length > 0) {
            String query = sql.trim().toUpperCase();
            is = query.startsWith("INSERT ") || query.startsWith("REPLACE ");
        }
        return is;
    }

    public static String addReturningClause(String sql, String[] keys) {
        String separator = ";";
        String clause = " RETURNING ";
        StringBuilder buffer = new StringBuilder();
        int position = sql.length();
        int index = sql.indexOf(separator);
        if (index != -1) {
            position = index;
        }
        if (sql.toUpperCase().indexOf(clause) != -1) {
            buffer.append(sql);
        } else {
            buffer.append(sql.substring(0, position));
            buffer.append(clause);
            buffer.append(String.join(",", keys));
            buffer.append(sql.substring(position));
        }
        return buffer.toString();
    }
}
