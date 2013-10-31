package com.gopivotal.mr;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Model of the BUSY_STOCK table:
 *
 * CREATE TABLE BUSY_STOCK
 *    (
 *        QUOTESYMBOL VARCHAR(250),
 *        COUNT INT
 *    )
 *
 * This class is the glue that bridges the output of a reducer to an actual
 * SqlFire table.
 */
public class BusyStockModel {

  private String quoteSymbol;
  private int count;
  //long timestamp;

  public BusyStockModel(String quoteSymbol, int count) {
    this.quoteSymbol = quoteSymbol;
    this.count = count;
  //  this.timestamp = System.currentTimeMillis();
  }

  public void setQuoteSymbol(int idx, PreparedStatement ps) throws SQLException {
    ps.setString(idx, quoteSymbol);
  }

  public void setCount(int idx, PreparedStatement ps) throws SQLException {
    ps.setInt(idx, count);
  }
}