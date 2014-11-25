package org.mrgeo.column;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.mrgeo.hdfs.utils.HadoopFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * 
 */
public class ColumnsIncludeFileWriter
{
  private static final Logger log = LoggerFactory.getLogger(ColumnsIncludeFileWriter.class);
  
  /**
   * 
   * 
   * @param fields
   * @param output
   * @throws IOException 
   */
  public static long write(final List<String> fields, final Path output) throws IOException
  {
    FSDataOutputStream fdos = HadoopFileUtils.getFileSystem().create(output);
    BufferedWriter writer = 
      new BufferedWriter(
        new OutputStreamWriter(fdos, "UTF-8"));
    long ctr = 0;
    for (String field : fields)
    {
      log.debug("Writing field: " + field);
      if (ctr > 0)
      {
        writer.write(",");
      }
      writer.write(field);
      ctr++;
    }
    writer.write("\n");
    writer.flush();
    writer.close();
    
    fdos.close();
    
    log.info("Wrote " + String.valueOf(ctr) + " fields to columns include file.");
    return ctr;
  }
}
