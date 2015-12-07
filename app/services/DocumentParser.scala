package services

import java.io.InputStream

import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.{ParseContext, AutoDetectParser, Parser}
import org.apache.tika.parser.pdf.PDFParser
import org.apache.tika.sax.BodyContentHandler

object DocumentType extends Enumeration {
  type DocumentType = Value
  val PDF, DOC, DOCX, Unknown = Value
}

case class DocumentMeta(property: String, value: String)
case class ParsedDocument(content: String, documentMetaData: Seq[DocumentMeta])

import services.DocumentType.DocumentType
class DocumentParser {

  def parseDocument(documentStream: InputStream, documentType: DocumentType): ParsedDocument = {

    documentType match {
      case DocumentType.PDF => parsePDF(documentStream, new PDFParser())
      case _ => throw new NotImplementedError(s"Document Type $documentType is not supported")
    }
  }

  def parsePDF(documentStream: InputStream, tikaParser: Parser): ParsedDocument = {
    val bodyContentHandler = new BodyContentHandler()
    val metaData = new Metadata()
    val parseContext = new ParseContext()
    tikaParser.parse(documentStream, bodyContentHandler, metaData, parseContext)

    ParsedDocument(bodyContentHandler.toString, metaData.names().map(name => new DocumentMeta(name, metaData.get(name))))
  }

}
