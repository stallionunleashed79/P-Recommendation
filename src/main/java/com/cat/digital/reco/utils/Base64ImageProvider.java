package com.cat.digital.reco.utils;

import java.io.IOException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgWMF;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;

public class Base64ImageProvider extends AbstractImageProvider {

  @Override
  public Image retrieve(String src) {
    int pos = src.indexOf("base64,");
    try {
      if (src.startsWith("data") && pos > 0) {
        byte[] img = Base64.decode(src.substring(pos + 7));
        return src.startsWith("data:image/wmf") ? new ImgWMF(img) : Image.getInstance(img);
      } else {
        return Image.getInstance(src);
      }
    } catch (BadElementException ex) {
      return null;
    } catch (IOException ex) {
      return null;
    }
  }

  @Override
  public String getImageRootPath() {
    return null;
  }
}
