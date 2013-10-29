package com.gopivotal.mr;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.vmware.sqlfire.hadoop.mapred.Key;
import com.vmware.sqlfire.hadoop.mapred.Row;
import com.vmware.sqlfire.hadoop.mapred.RowInputFormat;
import com.vmware.sqlfire.hadoop.mapred.RowOutputFormat;
import com.vmware.sqlfire.internal.engine.SqlfDataSerializable;



public class TopBusyStock extends Configured implements Tool {

  /**
   * Mapper used for first job. Produces tuples of the form:
   * 
   * MIA 1 JFK 1
   * 
   * This job is configured with a standard IntSumReducer to produce totals for
   * each airport code.
   */
  public static class SampleMapper extends MapReduceBase implements
      Mapper<Object, Row, Text, IntWritable> {

    private final static IntWritable countOne = new IntWritable(1);

    @Override
    public void map(Object key, Row row,
        OutputCollector<Text, IntWritable> output, Reporter reporter)
        throws IOException {

      String quote_symbol;

      try {
        ResultSet rs = row.getRowAsResultSet();
        quote_symbol = rs.getString("QUOTE_SYMBOL");
        System.out.println("#####Spring Travel Received QUOTE_SYMBOL: " + quote_symbol);
        output.collect(new Text(quote_symbol), countOne);
        System.out.println("#####Spring Travel Post Collect");
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public static class SampleReducer extends MapReduceBase implements
      Reducer<Text, IntWritable, Key, BusyStockModel> {

    @Override
    public void reduce(Text quoteSymbol, Iterator<IntWritable> values,
        OutputCollector<Key, BusyStockModel> output, Reporter reporter)
        throws IOException {
      int sum = 0;
      String quote_symbol = quoteSymbol.toString();
      while (values.hasNext()) {
        sum += values.next().get();
      }
      System.out.println("##### SUM for symbol: " + quote_symbol + " count: "+ sum);
      output.collect(new Key(), new BusyStockModel(quote_symbol, sum));
      System.out.println("###POST Reduce");

    }
  }

 

  public int run(String[] args) throws Exception {

    SqlfDataSerializable.initTypes();

    JobConf conf = new JobConf(getConf());
    conf.setJobName("Busy Stock Count");

    String hdfsHomeDir = "/user/liquanpei/st-tables";
    String tableName = "NANOTRADER.ORDERS";

    conf.set(RowInputFormat.HOME_DIR, hdfsHomeDir);
    conf.set(RowInputFormat.INPUT_TABLE, tableName);
    conf.setBoolean(RowInputFormat.CHECKPOINT_MODE, false);

    conf.set(RowOutputFormat.OUTPUT_URL,
        "jdbc:sqlfire://127.0.0.1:1527");
    conf.set(RowOutputFormat.OUTPUT_TABLE, "NANOTRADER.BUSY_STOCK");
    conf.setOutputFormat(RowOutputFormat.class);
    
    conf.setInputFormat(RowInputFormat.class);
    conf.setMapperClass(SampleMapper.class);
    conf.setMapOutputKeyClass(Text.class);
    conf.setMapOutputValueClass(IntWritable.class);

    conf.setReducerClass(SampleReducer.class);
    conf.setOutputKeyClass(Key.class);
    conf.setOutputValueClass(BusyStockModel.class);


    int rc = JobClient.runJob(conf).isSuccessful() ? 0 : 1;
    return rc;
  }

  public static void main(String[] args) throws Exception {
    System.out.println("SampleApp.main() invoked with " + args);
    int rc = ToolRunner.run(new TopBusyStock(), args);
    System.exit(rc);
  }
}
