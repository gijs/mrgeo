package org.mrgeo.mapalgebra;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.mrgeo.format.FeatureInputFormatFactory;
import org.mrgeo.hdfs.utils.HadoopFileUtils;
import org.mrgeo.utils.HadoopUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class VectorFileMapOpLoader implements ResourceMapOpLoader
{
  private static String basePath = null;

  public static void setVectorBasePath(String path)
  {
    basePath = path;
  }

  @Override
  public MapOp loadMapOpFromResource(String resourceName,
      final Properties providerProperties) throws IOException
  {
    // This code is HDFS-specific for now because we do not have a vector
    // data provider implemented for file-based vector data yet. Once that
    // is done, this code can be re-factored to use it.
    Path resourcePath = resolveNameToPath(resourceName);
    if (resourcePath != null)
    {
      if (FeatureInputFormatFactory.getInstance().isRecognized(resourcePath))
      {
        FileSystem fs = HadoopFileUtils.getFileSystem(resourcePath);
        if (fs.exists(resourcePath))
        {
          VectorReaderMapOp vrn = new VectorReaderMapOp(resourceName);
          return vrn;
        }
      }
    }
    return null;
  }

  // This functionality will eventually be inside of an HDFS data provider for
  // vector data...
  private static Path resolveNameToPath(final String input) throws IOException
  {
    // It could be either HDFS or local file system
    File f = new File(input);
    if (f.exists())
    {
      try
      {
        return new Path(new URI("file://" + input));
      }
      catch (URISyntaxException e)
      {
        // The URI is invalid, so let's continue to try to open it in HDFS
      }
    }
    Path p = new Path(input);

    FileSystem fs = HadoopFileUtils.getFileSystem(p);
    if (fs.exists(p))
    {
      return p;
    }

    Path basePath = new Path(getVectorBasePath());
    p = new Path(basePath, input);
    fs = HadoopFileUtils.getFileSystem(p);
    if (fs.exists(p))
    {
      return p;
    }

    return null;
  }

  private static String getVectorBasePath()
  {
    if (basePath == null)
    {
      basePath = HadoopUtils.getDefaultVectorBaseDirectory();
    }
    return basePath;
  }
}
