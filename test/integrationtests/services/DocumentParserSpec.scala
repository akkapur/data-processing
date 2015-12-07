package integrationtests.services

import java.io.ByteArrayInputStream

import org.scalatest.FunSpec
import services.{DocumentType, DocumentParser}

class DocumentParserSpec extends FunSpec {

  describe("A PDF document when streamed") {

    val pdfStream = getClass.getResourceAsStream("/pdf-sample.pdf")
    val documentParser = new DocumentParser()

    it("should be parsed for it's content and meta data") {
      val parsedDocument = documentParser.parseDocument(pdfStream, DocumentType.PDF)
      println(parsedDocument.content)
    }
  }

  describe("A PDF document when read as an array of bytes") {

    val pdfStream = getClass.getResourceAsStream("/pdf-sample.pdf")
    val documentParser = new DocumentParser()

    it("should be parsed for it's content and meta data") {
      val documentByteArray = Stream.continually(pdfStream.read()).takeWhile(_ != -1).map(_.toByte).toArray
      val byteArrayStream = new ByteArrayInputStream(documentByteArray)
      val parsedDocument = documentParser.parseDocument(byteArrayStream, DocumentType.PDF)
      println(parsedDocument.content)
    }
  }


}
