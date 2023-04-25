package com.cat.digital.reco.service.impl;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.cat.digital.reco.common.Constants;
import com.cat.digital.reco.domain.responses.RecommendationDetailsResponse;
import com.cat.digital.reco.utils.Base64ImageProvider;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PDFGeneratorServiceImpl implements PDFGeneratorBuilder {


  String templatePath;
  final ResourceLoader resourceLoader;

  @Autowired
  public PDFGeneratorServiceImpl(ResourceLoader resourceLoader, @Value("${template.path:classpath:template/}") final String path) {
    this.resourceLoader = resourceLoader;
    this.templatePath = path;
  }

  public String setupXSLTemplate(String templateName) {
    StringBuilder buildPath = new StringBuilder(templatePath);
    buildPath.append(templateName).append(".").append(Constants.XST_EXTENSION);
    return buildPath.toString();
  }

  public String createXML(RecommendationDetailsResponse recommendationDetailsResponse, String templateName)
      throws IOException {
    XmlMapper xmlMapper = new XmlMapper();
    return xmlMapper.writeValueAsString(recommendationDetailsResponse);
  }

  public byte[] createHtml(String xmlContent, String xslPath) throws IOException, TransformerException {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Writer writer = new OutputStreamWriter(baos);

    StringReader xmlContentReader = new StringReader(xmlContent);
    StreamSource xml = new StreamSource(xmlContentReader);
    StreamSource xsl = new StreamSource(resourceLoader.getResource(xslPath).getInputStream());

    // Create a TransformerFactory object
    TransformerFactory factory = TransformerFactory.newInstance();
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, StringUtils.EMPTY); // Compliant
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, StringUtils.EMPTY); // Compliant

    // Get the incoming XSLT file
    Transformer transformer = factory.newTransformer(xsl);

    // Get the XML file and apply the XSLT transformation to convert to HTML.
    transformer.transform(xml, new StreamResult(writer));
    writer.flush();
    writer.close();
    return baos.toByteArray();
  }

  //@Override
  public ByteArrayInputStream convertHtmlToPdf(byte[] html) throws IOException, DocumentException {
    // step 1 get Document object
    Document document = new Document(PageSize.A4);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    // step 2 get Pdf write instance
    PdfWriter writer = PdfWriter.getInstance(document, outputStream);
    writer.setPageEvent(new PageEventListener());
    // step 3 open the document
    document.open();
    // step 4 setup proccesor,css and html pipeline context

    final TagProcessorFactory tagProcessorFactory = Tags.getHtmlTagProcessorFactory();

    final CssFilesImpl cssFiles = new CssFilesImpl();
    cssFiles.add(XMLWorkerHelper.getInstance().getDefaultCSS());

    // CSS
    final StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);

    // HTML context
    final HtmlPipelineContext htmlContext = new HtmlPipelineContext(new CssAppliersImpl(new XMLWorkerFontProvider()));
    htmlContext.setAcceptUnknown(true).autoBookmark(true).setTagFactory(tagProcessorFactory);
    htmlContext.setImageProvider(new Base64ImageProvider());

    // HTML Pipeline
    PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
    final HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, pdf);
    CssResolverPipeline cssResolverPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);

    // XML Worker
    final XMLWorker worker = new XMLWorker(cssResolverPipeline, true);
    final Charset charset = StandardCharsets.UTF_8;
    final XMLParser xmlParser = new XMLParser(true, worker, charset);
    final InputStream is = new ByteArrayInputStream(html);

    xmlParser.parse(is, charset);

    is.close();

    // step close the document
    document.close();
    return new ByteArrayInputStream(outputStream.toByteArray());
  }
}

class PageEventListener extends PdfPageEventHelper {

  @Override
  public void onEndPage(PdfWriter writer, Document document) {
    PdfPTable footer = new PdfPTable(1);

    try {
      // set defaults
      footer.setWidths(new int[]{28});
      footer.setTotalWidth(527);
      footer.setLockedWidth(true);
      footer.getDefaultCell().setFixedHeight(40);
      footer.getDefaultCell().setBorder(Rectangle.NO_BORDER);

      // add current page count
      footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
      footer.addCell(new Phrase(String.format("Page %d", writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)));

      // write page
      PdfContentByte canvas = writer.getDirectContent();
      canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
      footer.writeSelectedRows(0, -1, 34, 50, canvas);
      canvas.endMarkedContentSequence();
    } catch (DocumentException de) {
      throw new ExceptionConverter(de);
    }
  }
}
