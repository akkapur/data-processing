package controllers

import java.io.{FileInputStream, InputStream, ByteArrayInputStream, File}

import play.api._
import play.api.mvc.BodyParsers.parse
import play.api.mvc._
import services.{DocumentType, DocumentParser}

class GuavaDocument extends Controller {

  def create() = Action(parse.multipartFormData) { request =>
    request.body.file("userDoc").map { userDoc =>
      //val filename = userDoc.filename
      //val contentType = userDoc.contentType
      val localFile = userDoc.ref.moveTo(new File("/tmp/userDoc")) //TODO: The directory should be custom for each project

      val localFileStream = new FileInputStream(localFile)
      val documentParser = new DocumentParser
      documentParser.parseDocument(localFileStream, DocumentType.PDF)
      localFileStream.close()

      Ok("File processed")
    }.getOrElse {
      Redirect(routes.Application.index).flashing(
        "error" -> "Missing file"
      )
    }
  }

}
