/*
 * Copyright (c) 2007 David Crawshaw <david@zentus.com>
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package org.sqlite.core;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteConnectionConfig;
import org.sqlite.jdbc3.JDBC3Connection;
import org.sqlite.jdbc4.JDBC4ResultSet;
import org.sqlite.util.QueryUtils;
import org.sqlite.util.RowSet;

public abstract class CoreStatement implements Codes {
    public final SQLiteConnection conn;
    protected final CoreResultSet rs;

    public SafeStmtPtr pointer;
    protected String sql = null;

    protected int batchPos;
    protected Object[] batch = null;
    protected boolean resultsWaiting = false;

    private ResultSet generatedKeysRs = null;

    protected CoreStatement(SQLiteConnection c) {
        conn = c;
        rs = new JDBC4ResultSet(this);
    }

    public DB getDatabase() {
        return conn.getDatabase();
    }

    public SQLiteConnectionConfig getConnectionConfig() {
        return conn.getConnectionConfig();
    }

    /** @throws SQLException If the database is not opened. */
    protected final void checkOpen() throws SQLException {
        if (pointer.isClosed()) throw new SQLException("statement is not executing");
    }

    /**
     * @return True if the database is opened; false otherwise.
     * @throws SQLException
     */
    boolean isOpen() throws SQLException {
        return !pointer.isClosed();
    }

    /**
     * Calls sqlite3_step() and sets up results. Expects a clean stmt.
     *
     * @return True if the ResultSet has at least one row; false otherwise.
     * @throws SQLException If the given SQL statement is null or no database is open.
     */
    protected boolean exec() throws SQLException {
        if (sql == null) throw new SQLException("SQLiteJDBC internal error: sql==null");
        if (rs.isOpen()) throw new SQLException("SQLite JDBC internal error: rs.isOpen() on exec.");

        if (this.conn instanceof JDBC3Connection) {
            ((JDBC3Connection) this.conn).tryEnforceTransactionMode();
        }

        boolean success = false;
        boolean rc = false;
        try {
            rc = conn.getDatabase().execute(this, null);
            success = true;
        } finally {
            notifyFirstStatementExecuted();
            resultsWaiting = rc;
            if (!success) {
                this.pointer.close();
            }
        }

        return pointer.safeRunInt(DB::column_count) != 0;
    }

    /**
     * Executes SQL statement and throws SQLExceptions if the given SQL statement is null or no
     * database is open.
     *
     * @param sql SQL statement.
     * @return True if the ResultSet has at least one row; false otherwise.
     * @throws SQLException If the given SQL statement is null or no database is open.
     */
    protected boolean exec(String sql) throws SQLException {
        if (sql == null) throw new SQLException("SQLiteJDBC internal error: sql==null");
        if (rs.isOpen()) throw new SQLException("SQLite JDBC internal error: rs.isOpen() on exec.");

        if (this.conn instanceof JDBC3Connection) {
            ((JDBC3Connection) this.conn).tryEnforceTransactionMode();
        }

        boolean rc = false;
        boolean success = false;
        try {
            rc = conn.getDatabase().execute(sql, conn.getAutoCommit());
            success = true;
        } finally {
            notifyFirstStatementExecuted();
            resultsWaiting = rc;
            if (!success && pointer != null) {
                pointer.close();
            }
        }

        return pointer.safeRunInt(DB::column_count) != 0;
    }

    protected void internalClose() throws SQLException {
        if (this.pointer != null && !this.pointer.isClosed()) {
            if (conn.isClosed()) throw DB.newSQLException(SQLITE_ERROR, "Connection is closed");

            rs.close();

            batch = null;
            batchPos = 0;
            int resp = this.pointer.close();

            if (resp != SQLITE_OK && resp != SQLITE_MISUSE) conn.getDatabase().throwex(resp);
        }
    }

    protected void notifyFirstStatementExecuted() {
        conn.setFirstStatementExecuted(true);
    }

    public abstract ResultSet executeQuery(String sql, boolean closeStmt) throws SQLException;

    protected void checkIndex(int index) throws SQLException {
        if (batch == null) {
            throw new SQLException("No parameter has been set yet");
        }
        if (index < 1 || index > batch.length) {
            throw new SQLException("Parameter index is invalid");
        }
    }

    protected void clearGeneratedRs() throws SQLException {
        if (generatedKeysRs != null && !generatedKeysRs.isClosed()) {
            generatedKeysRs.close();
        }
        generatedKeysRs = null;
    }

    /**
     * This implementation add a SQLite's RETURNING clause to SQL command starting with
     * INSERT or REPLACE this will allow to retrieve multiple rows ID when inserting multiple rows.
     *
     * @see java.sql.Statement#getGeneratedKeys()
     */
    protected void updateGeneratedKeys(ResultSet result) throws SQLException {
        System.out.println("CoreStatement.updateGeneratedKeys() 1");
        clearGeneratedRs();
        if (QueryUtils.isInsertQuery(sql, "*")) {
            generatedKeysRs = new RowSet(result);
        }
    }

    /**
     * This implementation add a SQLite's RETURNING clause to SQL command starting with INSERT or REPLACE.
     * this will allow to retrieve multiple rows ID when inserting multiple rows.
     *
     * @see java.sql.Statement#getGeneratedKeys()
     */
    public ResultSet getGeneratedKeys() throws SQLException {
        // getGeneratedKeys is required to return an EmptyResult set if the statement
        // did not generate any keys. Thus, if the generateKeysResultSet is NULL, spin
        // up a new result set without any contents by issuing a query with a false where condition
        if (generatedKeysRs == null) {
            generatedKeysRs = conn.createStatement().executeQuery("SELECT 1 WHERE 1 = 2;");
        }
        return generatedKeysRs;
    }
}
