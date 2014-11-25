package org.mrgeo.pdf;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface PdfCurve
{
  public static final String CSV_FORMAT = "csv";
  public static final String JSON_FORMAT = "json";

  /**
   * Returns the likelihood of the specified value on the curve. This function may
   * interpolate the likelihood, depending on the implementation.
   * 
   * @param rfdValue
   * @return
   */
  public double getLikelihood(double rfdValue);

  /**
   * Export the PDF to the specified stream in the specified format. Classes
   * that implement this method should be sure to flush the buffer before
   * returning. otherwise, all output is not guaranteed to be written.
   * 
   * @param os The output stream to write to.
   * @param format The format for the output - use CSV_FORMAT or JSON_FORMAT
   * for example.
   * @throws IOException
   */
  public void export(OutputStream os, String format) throws IOException;
  
  /**
   * Return the points of the PDF curve where the x coordinate is the RFD value and
   * the y coordinate is the likelihood at that RFD value.
   * @return
   * @throws IOException
   */
  public List<Point2D.Double> getPoints() throws IOException;
  
  /**
   * Calculates the area under this PDF curve as well as the area under otherCurve
   * and subtracts the otherCurve area from this curve area and returns the result.
   * 
   * @param otherCurve
   * @return
   */
  public double diff(PdfCurve otherCurve);

  /**
   * Computes the area under the PDF curve.
   * @return
   */
  public double area();
}
