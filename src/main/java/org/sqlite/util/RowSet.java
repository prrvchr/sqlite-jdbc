package org.sqlite.util;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import javax.sql.rowset.spi.SyncFactory;
import javax.sql.rowset.spi.SyncProvider;


public class RowSet
    implements ResultSet {

    protected CachedRowSet mRowset;
    private boolean mClosed = false;
 
    // The constructor method:
    public RowSet(java.sql.ResultSet result)
        throws SQLException {
        CachedRowSet rowset = getCachedRowSet();
        rowset.populate(result);
        rowset.setReadOnly(true);
        mRowset = rowset;
    }

    private static void initializeRowSetProvider() throws java.sql.SQLException {
        // XXX: If we want to be able to access the RIOptimisticProvider implementation
        // XXX: then it is necessary to unregister RIOptimisticProvider
        List<String> providers = new ArrayList<>();
        Enumeration<SyncProvider> it = SyncFactory.getRegisteredProviders();
        while (it.hasMoreElements()) {
            providers.add(it.nextElement().getProviderID());
        }
        for (String provider : providers) {
            SyncFactory.unregisterProvider(provider);
        }
    }

    private static CachedRowSet getCachedRowSet() throws java.sql.SQLException {
        initializeRowSetProvider();
        // XXX: Now it has been unregistered the RIOptimisticProvider can be used...
        return RowSetProvider.newFactory().createCachedRowSet();
    }


    @Override
    public void insertRow()
        throws SQLException {
        mRowset.insertRow();
    }

    @Override
    public void cancelRowUpdates()
        throws SQLException {
        mRowset.cancelRowUpdates();
    }

    @Override
    public void moveToInsertRow()
        throws SQLException {
        mRowset.moveToInsertRow();
    }

    @Override
    public void moveToCurrentRow()
        throws SQLException {
        mRowset.moveToCurrentRow();
    }

    @Override
    public int getConcurrency()
        throws SQLException {
        return mRowset.getConcurrency();
    }

    @Override
    public int getType()
        throws SQLException {
        return mRowset.getType();
    }

    @Override
    public Statement getStatement()
        throws SQLException {
        return mRowset.getStatement();
    }

    @Override
    public SQLWarning getWarnings()
        throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings()
        throws SQLException {
        mRowset.clearWarnings();
    }

    @Override
    public String getCursorName()
        throws SQLException {
        return mRowset.getCursorName();
    }

    @Override
    public ResultSetMetaData getMetaData()
        throws SQLException {
        return mRowset.getMetaData();
    }

    @Override
    public int findColumn(String label)
        throws SQLException {
        return mRowset.findColumn(label);
    }

    @Override
    public void setFetchDirection(int direction)
        throws SQLException {
        mRowset.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection()
        throws SQLException {
        return mRowset.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows)
        throws SQLException {
        mRowset.setFetchSize(rows);
    }

    @Override
    public int getFetchSize()
        throws SQLException {
        return mRowset.getFetchSize();
    }

    @Override
    public int getHoldability()
        throws SQLException {
        return mRowset.getHoldability();
    }

    @Override
    public boolean isClosed()
        throws SQLException {
        return mClosed;
    }

    @Override
    public boolean rowUpdated()
        throws SQLException {
        return mRowset.rowUpdated();
    }

    @Override
    public boolean rowInserted()
        throws SQLException {
        return mRowset.rowInserted();
    }

    @Override
    public boolean rowDeleted()
        throws SQLException {
        return mRowset.rowDeleted();
    }

    @Override
    public void updateRow()
        throws SQLException {
        mRowset.updateRow();
    }

    @Override
    public void deleteRow()
        throws SQLException {
        mRowset.deleteRow();
    }

    @Override
    public void refreshRow()
        throws SQLException {
        mRowset.refreshRow();
    }


    // XXX: java.sql.ResultSet mover
    @Override
    public boolean next()
        throws SQLException {
        return mRowset.next();
    }

    @Override
    public boolean previous()
        throws SQLException {
        boolean previous = false;
        if (mRowset.size() > 0) {
            previous = mRowset.previous();
        }
        return previous;
    }

    @Override
    public boolean isBeforeFirst()
        throws SQLException {
        return mRowset.isBeforeFirst();
    }

    @Override
    public boolean isAfterLast()
        throws SQLException {
        return mRowset.isAfterLast();
    }

    @Override
    public boolean isFirst()
        throws SQLException {
        return mRowset.isFirst();
    }

    @Override
    public boolean isLast()
        throws SQLException {
        return mRowset.isLast();
    }

    @Override
    public void beforeFirst()
        throws SQLException {
        mRowset.beforeFirst();
    }

    @Override
    public void afterLast()
        throws SQLException {
        mRowset.afterLast();
    }

    @Override
    public boolean first()
        throws SQLException {
        return mRowset.first();
    }

    @Override
    public boolean last()
        throws SQLException {
        return mRowset.last();
    }

    @Override
    public int getRow()
        throws SQLException {
        return mRowset.getRow();
    }

    @Override
    public boolean absolute(int row)
        throws SQLException {
        return mRowset.absolute(row);
    }

    @Override
    public boolean relative(int row)
        throws SQLException {
        return mRowset.relative(row);
    }

    @Override
    public <T> T unwrap(Class<T> iface)
        throws SQLException {
        return mRowset.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface)
        throws SQLException {
        return mRowset.isWrapperFor(iface);
    }

    @Override
    public void close()
        throws SQLException {
        mRowset.close();
        mClosed = true;
    }


    // XXX: java.sql.ResultSet getter by index
    @Override
    public boolean wasNull()
        throws SQLException {
        return mRowset.wasNull();
    }

    @Override
    public String getString(int index)
        throws SQLException {
        return mRowset.getString(index);
    }

    @Override
    public boolean getBoolean(int index)
        throws SQLException {
        return mRowset.getBoolean(index);
    }

    @Override
    public byte getByte(int index)
        throws SQLException {
        return mRowset.getByte(index);
    }

    @Override
    public short getShort(int index)
        throws SQLException {
        return mRowset.getShort(index);
    }

    @Override
    public int getInt(int index)
        throws SQLException {
        return mRowset.getInt(index);
    }

    @Override
    public long getLong(int index)
        throws SQLException {
        return mRowset.getLong(index);
    }

    @Override
    public float getFloat(int index)
        throws SQLException {
        return mRowset.getFloat(index);
    }

    @Override
    public double getDouble(int index)
        throws SQLException {
        return mRowset.getDouble(index);
    }

    @Override
    public BigDecimal getBigDecimal(int index, int scale)
        throws SQLException {
        return mRowset.getBigDecimal(index);
    }

    @Override
    public byte[] getBytes(int index)
        throws SQLException {
        return mRowset.getBytes(index);
    }

    @Override
    public Date getDate(int index)
        throws SQLException {
        return mRowset.getDate(index);
    }

    @Override
    public Time getTime(int index)
        throws SQLException {
        return mRowset.getTime(index);
    }

    @Override
    public Timestamp getTimestamp(int index)
        throws SQLException {
        return mRowset.getTimestamp(index);
    }

    @Override
    public InputStream getAsciiStream(int index)
        throws SQLException {
        return mRowset.getAsciiStream(index);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InputStream getUnicodeStream(int index)
        throws SQLException {
        return mRowset.getUnicodeStream(index);
    }

    @Override
    public InputStream getBinaryStream(int index)
        throws SQLException {
        return mRowset.getBinaryStream(index);
    }

    @Override
    public Object getObject(int index)
        throws SQLException {
        return mRowset.getObject(index);
    }

    @Override
    public Reader getCharacterStream(int index)
        throws SQLException {
        return mRowset.getCharacterStream(index);
    }

    @Override
    public BigDecimal getBigDecimal(int index)
        throws SQLException {
        return mRowset.getBigDecimal(index);
    }

    @Override
    public Object getObject(int index, Map<String, Class<?>> map)
        throws SQLException {
        return mRowset.getObject(index, map);
    }

    @Override
    public Ref getRef(int index)
        throws SQLException {
        return mRowset.getRef(index);
    }

    @Override
    public Blob getBlob(int index)
        throws SQLException {
        return mRowset.getBlob(index);
    }

    @Override
    public Clob getClob(int index)
        throws SQLException {
        return mRowset.getClob(index);
    }

    @Override
    public Array getArray(int index)
        throws SQLException {
        return mRowset.getArray(index);
    }

    @Override
    public Date getDate(int index, Calendar cal)
        throws SQLException {
        return mRowset.getDate(index);
    }

    @Override
    public Time getTime(int index, Calendar cal)
        throws SQLException {
        return mRowset.getTime(index);
    }

    @Override
    public Timestamp getTimestamp(int index, Calendar cal)
        throws SQLException {
        return mRowset.getTimestamp(index);
    }

    @Override
    public URL getURL(int index)
        throws SQLException {
        return mRowset.getURL(index);
    }

    @Override
    public RowId getRowId(int index)
        throws SQLException {
        return mRowset.getRowId(index);
    }

    @Override
    public NClob getNClob(int index)
        throws SQLException {
        return mRowset.getNClob(index);
    }

    @Override
    public SQLXML getSQLXML(int index)
        throws SQLException {
        return mRowset.getSQLXML(index);
    }

    @Override
    public String getNString(int index)
        throws SQLException {
        return mRowset.getNString(index);
    }

    @Override
    public Reader getNCharacterStream(int index)
        throws SQLException {
        return mRowset.getNCharacterStream(index);
    }

    @Override
    public <T> T getObject(int index, Class<T> type)
        throws SQLException {
        return mRowset.getObject(index, type);
    }

    // XXX: java.sql.ResultSet getter by label
    @Override
    public String getString(String label)
        throws SQLException {
        return mRowset.getString(label);
    }

    @Override
    public boolean getBoolean(String label)
        throws SQLException {
        return mRowset.getBoolean(label);
    }

    @Override
    public byte getByte(String label)
        throws SQLException {
        return mRowset.getByte(label);
    }

    @Override
    public short getShort(String label)
        throws SQLException {
        return mRowset.getShort(label);
    }

    @Override
    public int getInt(String label)
        throws SQLException {
        return mRowset.getInt(label);
    }

    @Override
    public long getLong(String label)
        throws SQLException {
        return mRowset.getLong(label);
    }

    @Override
    public float getFloat(String label)
        throws SQLException {
        return mRowset.getFloat(label);
    }

    @Override
    public double getDouble(String label)
        throws SQLException {
        return mRowset.getDouble(label);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BigDecimal getBigDecimal(String label, int scale)
        throws SQLException {
        return mRowset.getBigDecimal(label, scale);
    }

    @Override
    public byte[] getBytes(String label)
        throws SQLException {
        return mRowset.getBytes(label);
    }

    @Override
    public Date getDate(String label)
        throws SQLException {
        return mRowset.getDate(label);
    }

    @Override
    public Time getTime(String label)
        throws SQLException {
        return mRowset.getTime(label);
    }

    @Override
    public Timestamp getTimestamp(String label)
        throws SQLException {
        return mRowset.getTimestamp(label);
    }

    @Override
    public InputStream getAsciiStream(String label)
        throws SQLException {
        return mRowset.getAsciiStream(label);
    }

    @SuppressWarnings("deprecation")
    @Override
    public InputStream getUnicodeStream(String label)
        throws SQLException {
        return mRowset.getUnicodeStream(label);
    }

    @Override
    public InputStream getBinaryStream(String label)
        throws SQLException {
        return mRowset.getBinaryStream(label);
    }

    @Override
    public Object getObject(String label)
        throws SQLException {
        return mRowset.getObject(label);
    }

    @Override
    public Reader getCharacterStream(String label)
        throws SQLException {
        return mRowset.getCharacterStream(label);
    }

    @Override
    public BigDecimal getBigDecimal(String label)
        throws SQLException {
        return mRowset.getBigDecimal(label);
    }

    @Override
    public Object getObject(String label, Map<String, Class<?>> map)
        throws SQLException {
        return mRowset.getObject(label, map);
    }

    @Override
    public Ref getRef(String label)
        throws SQLException {
        return mRowset.getRef(label);
    }

    @Override
    public Blob getBlob(String label)
        throws SQLException {
        return mRowset.getBlob(label);
    }

    @Override
    public Clob getClob(String label)
        throws SQLException {
        return mRowset.getClob(label);
    }

    @Override
    public Array getArray(String label)
        throws SQLException {
        return mRowset.getArray(label);
    }

    @Override
    public Date getDate(String label, Calendar cal)
        throws SQLException {
        return mRowset.getDate(label, cal);
    }

    @Override
    public Time getTime(String label, Calendar cal)
        throws SQLException {
        return mRowset.getTime(label, cal);
    }

    @Override
    public Timestamp getTimestamp(String label, Calendar cal)
        throws SQLException {
        return mRowset.getTimestamp(label, cal);
    }

    @Override
    public URL getURL(String label)
        throws SQLException {
        return mRowset.getURL(label);
    }

    @Override
    public RowId getRowId(String label)
        throws SQLException {
        return mRowset.getRowId(label);
    }

    @Override
    public NClob getNClob(String label)
        throws SQLException {
        return mRowset.getNClob(label);
    }

    @Override
    public SQLXML getSQLXML(String label)
        throws SQLException {
        return mRowset.getSQLXML(label);
    }

    @Override
    public String getNString(String label)
        throws SQLException {
        return mRowset.getNString(label);
    }

    @Override
    public Reader getNCharacterStream(String label)
        throws SQLException {
        return mRowset.getNCharacterStream(label);
    }

    @Override
    public <T> T getObject(String label, Class<T> type)
        throws SQLException {
        return mRowset.getObject(label, type);
    }


    // XXX: java.sql.ResultSet updater by index
    @Override
    public void updateNull(int index)
        throws SQLException {
        mRowset.updateNull(index);
    }

    @Override
    public void updateBoolean(int index, boolean value)
        throws SQLException {
        mRowset.updateBoolean(index, value);
    }

    @Override
    public void updateByte(int index, byte value)
        throws SQLException {
        mRowset.updateByte(index, value);
    }

    @Override
    public void updateShort(int index, short value)
        throws SQLException {
        mRowset.updateShort(index, value);
    }

    @Override
    public void updateInt(int index, int value)
        throws SQLException {
        mRowset.updateInt(index, value);
    }

    @Override
    public void updateLong(int index, long value)
        throws SQLException {
        mRowset.updateLong(index, value);
    }

    @Override
    public void updateFloat(int index, float value)
        throws SQLException {
        mRowset.updateFloat(index, value);
    }

    @Override
    public void updateDouble(int index, double value)
        throws SQLException {
        mRowset.updateDouble(index, value);
    }

    @Override
    public void updateBigDecimal(int index, BigDecimal value)
        throws SQLException {
        mRowset.updateBigDecimal(index, value);
    }

    @Override
    public void updateString(int index, String value)
        throws SQLException {
        mRowset.updateString(index, value);
    }

    @Override
    public void updateBytes(int index, byte[] value)
        throws SQLException {
        mRowset.updateBytes(index, value);
    }

    @Override
    public void updateDate(int index, Date value)
        throws SQLException {
        mRowset.updateDate(index, value);
    }

    @Override
    public void updateTime(int index, Time value)
        throws SQLException {
        mRowset.updateTime(index, value);
    }

    @Override
    public void updateTimestamp(int index, Timestamp value)
        throws SQLException {
        mRowset.updateTimestamp(index, value);
    }

    @Override
    public void updateAsciiStream(int index, InputStream value, int length)
        throws SQLException {
        mRowset.updateAsciiStream(index, value, length);
    }

    @Override
    public void updateBinaryStream(int index, InputStream value, int length)
        throws SQLException {
        mRowset.updateBinaryStream(index, value, length);
    }

    @Override
    public void updateCharacterStream(int index, Reader value, int length)
        throws SQLException {
        mRowset.updateCharacterStream(index, value, length);
    }

    @Override
    public void updateObject(int index, Object value, int length)
        throws SQLException {
        mRowset.updateObject(index, value, length);
    }

    @Override
    public void updateObject(int index, Object value)
        throws SQLException {
        mRowset.updateObject(index, value);
    }

    @Override
    public void updateRef(int index, Ref value)
        throws SQLException {
        mRowset.updateRef(index, value);
    }

    @Override
    public void updateBlob(int index, Blob value)
        throws SQLException {
        mRowset.updateBlob(index, value);
    }

    @Override
    public void updateClob(int index, Clob value)
        throws SQLException {
        mRowset.updateClob(index, value);
    }

    @Override
    public void updateArray(int index, Array value)
        throws SQLException {
        mRowset.updateArray(index, value);
    }

    @Override
    public void updateNCharacterStream(int index, Reader value, long length)
        throws SQLException {
        mRowset.updateNCharacterStream(index, value, length);
    }

    @Override
    public void updateAsciiStream(int index, InputStream value, long length)
        throws SQLException {
        mRowset.updateAsciiStream(index, value, length);
    }

    @Override
    public void updateBinaryStream(int index, InputStream value, long length)
        throws SQLException {
        mRowset.updateBinaryStream(index, value, length);
    }

    @Override
    public void updateCharacterStream(int index, Reader value, long length)
        throws SQLException {
        mRowset.updateCharacterStream(index, value, length);
    }

    @Override
    public void updateBlob(int index, InputStream value, long length)
        throws SQLException  {
        mRowset.updateBlob(index, value, length);
    }

    @Override
    public void updateClob(int index, Reader value, long length)
        throws SQLException {
        mRowset.updateClob(index, value, length);
    }

    @Override
    public void updateNClob(int index, Reader value, long length)
        throws SQLException {
        mRowset.updateNClob(index, value, length);
    }

    @Override
    public void updateNCharacterStream(int index, Reader value)
        throws SQLException {
        mRowset.updateNCharacterStream(index, value);
    }

    @Override
    public void updateAsciiStream(int index, InputStream value)
        throws SQLException {
        mRowset.updateAsciiStream(index, value);
    }

    @Override
    public void updateBinaryStream(int index, InputStream value)
        throws SQLException {
        mRowset.updateBinaryStream(index, value);
    }

    @Override
    public void updateCharacterStream(int index, Reader value)
        throws SQLException {
        mRowset.updateCharacterStream(index, value);
    }

    @Override
    public void updateBlob(int index, InputStream value)
        throws SQLException {
        mRowset.updateBlob(index, value);
    }

    @Override
    public void updateClob(int index, Reader value)
        throws SQLException {
        mRowset.updateClob(index, value);
    }

    @Override
    public void updateNClob(int index, Reader value)
        throws SQLException {
        mRowset.updateNClob(index, value);
    }

    @Override
    public void updateSQLXML(int index, SQLXML value)
        throws SQLException {
        mRowset.updateSQLXML(index, value);
    }

    @Override
    public void updateNString(int index, String value)
        throws SQLException {
        mRowset.updateNString(index, value);
    }

    @Override
    public void updateNClob(int index, NClob value)
        throws SQLException {
        mRowset.updateNClob(index, value);
    }

    @Override
    public void updateRowId(int index, RowId value)
        throws SQLException {
        mRowset.updateRowId(index, value);
    }

    // XXX: java.sql.ResultSet updater by label
    @Override
    public void updateNull(String label)
        throws SQLException {
        mRowset.updateNull(label);
    }

    @Override
    public void updateBoolean(String label, boolean value)
        throws SQLException {
        mRowset.updateBoolean(label ,value);
    }

    @Override
    public void updateByte(String label, byte value)
        throws SQLException {
        mRowset.updateByte(label, value);
    }

    @Override
    public void updateShort(String label, short value)
        throws SQLException {
        mRowset.updateShort(label, value);
    }

    @Override
    public void updateInt(String label, int value)
        throws SQLException {
        mRowset.updateInt(label, value);
    }

    @Override
    public void updateLong(String label, long value)
        throws SQLException {
        mRowset.updateLong(label, value);
    }

    @Override
    public void updateFloat(String label, float value)
        throws SQLException {
        mRowset.updateFloat(label, value);
    }

    @Override
    public void updateDouble(String label, double value)
        throws SQLException {
        mRowset.updateDouble(label, value);
    }

    @Override
    public void updateBigDecimal(String label, BigDecimal value)
        throws SQLException {
        mRowset.updateBigDecimal(label, value);
    }

    @Override
    public void updateString(String label, String value)
        throws SQLException {
        mRowset.updateString(label, value);
    }

    @Override
    public void updateBytes(String label, byte[] value)
        throws SQLException {
        mRowset.updateBytes(label, value);
    }

    @Override
    public void updateDate(String label, Date value)
        throws SQLException {
        mRowset.updateDate(label, value);
    }

    @Override
    public void updateTime(String label, Time value)
        throws SQLException {
        mRowset.updateTime(label, value);
    }

    @Override
    public void updateTimestamp(String label, Timestamp value)
        throws SQLException {
        mRowset.updateTimestamp(label, value);
    }

    @Override
    public void updateAsciiStream(String label, InputStream value, int length)
        throws SQLException {
        mRowset.updateAsciiStream(label, value, length);
    }

    @Override
    public void updateBinaryStream(String label, InputStream value, int length)
        throws SQLException {
        mRowset.updateBinaryStream(label, value, length);
    }

    @Override
    public void updateCharacterStream(String label, Reader value, int length)
        throws SQLException {
        mRowset.updateCharacterStream(label, value, length);
    }

    @Override
    public void updateObject(String label, Object value, int length)
        throws SQLException {
        mRowset.updateObject(label, value, length);
    }

    @Override
    public void updateObject(String label, Object value)
        throws SQLException {
        mRowset.updateObject(label, value);
    }

    @Override
    public void updateRef(String label, Ref value)
        throws SQLException {
        mRowset.updateRef(label, value);
    }

    @Override
    public void updateBlob(String label, Blob value)
        throws SQLException {
        mRowset.updateBlob(label, value);
    }

    @Override
    public void updateClob(String label, Clob value)
        throws SQLException {
        mRowset.updateClob(label, value);
    }

    @Override
    public void updateArray(String label, Array value)
        throws SQLException {
        mRowset.updateArray(label, value);
    }

    @Override
    public void updateNCharacterStream(String label, Reader value, long length)
        throws SQLException {
        mRowset.updateNCharacterStream(label, value, length);
    }

    @Override
    public void updateAsciiStream(String label, InputStream value, long length)
        throws SQLException {
        mRowset.updateAsciiStream(label, value, length);
    }

    @Override
    public void updateBinaryStream(String label, InputStream value, long length)
        throws SQLException {
        mRowset.updateBinaryStream(label, value, length);
    }

    @Override
    public void updateCharacterStream(String label, Reader value, long length)
        throws SQLException {
        mRowset.updateCharacterStream(label, value, length);
    }

    @Override
    public void updateBlob(String label, InputStream value, long length)
        throws SQLException {
        mRowset.updateBlob(label, value, length);
    }

    @Override
    public void updateClob(String label, Reader value, long length)
        throws SQLException {
        mRowset.updateClob(label, value, length);
    }

    @Override
    public void updateNClob(String label, Reader value, long length)
        throws SQLException {
        mRowset.updateNClob(label, value, length);
    }

    @Override
    public void updateNCharacterStream(String label, Reader value)
        throws SQLException {
        mRowset.updateNCharacterStream(label, value);
    }

    @Override
    public void updateAsciiStream(String label, InputStream value)
        throws SQLException {
        mRowset.updateAsciiStream(label, value);
    }

    @Override
    public void updateBinaryStream(String label, InputStream value)
        throws SQLException {
        mRowset.updateBinaryStream(label, value);
    }

    @Override
    public void updateCharacterStream(String label, Reader value)
        throws SQLException {
        mRowset.updateCharacterStream(label, value);
    }

    @Override
    public void updateBlob(String label, InputStream value)
        throws SQLException {
        mRowset.updateBlob(label, value);
    }

    @Override
    public void updateClob(String label, Reader value)
        throws SQLException {
        mRowset.updateClob(label, value);
    }

    @Override
    public void updateNClob(String label, Reader value)
        throws SQLException {
        mRowset.updateNClob(label, value);
    }

    @Override
    public void updateSQLXML(String label, SQLXML value)
        throws SQLException {
        mRowset.updateSQLXML(label, value);
    }

    @Override
    public void updateNString(String label, String value)
        throws SQLException {
        mRowset.updateNString(label, value);
    }

    @Override
    public void updateNClob(String label, NClob value)
        throws SQLException {
        mRowset.updateNClob(label, value);
    }

    @Override
    public void updateRowId(String label, RowId value)
        throws SQLException {
        mRowset.updateRowId(label, value);
    }

}
