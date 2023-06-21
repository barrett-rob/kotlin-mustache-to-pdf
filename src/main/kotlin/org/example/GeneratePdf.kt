package org.example

import com.fasterxml.jackson.module.kotlin.*
import com.github.mustachejava.DefaultMustacheFactory
import org.xhtmlrenderer.pdf.ITextRenderer
import java.awt.Desktop
import java.io.FileOutputStream
import java.io.StringWriter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.nio.file.FileSystems
import org.w3c.tidy.Tidy
import java.nio.file.Files

fun main(args: Array<String>) {
    GeneratePdf().run(args.toList())
}

class GeneratePdf {

    fun run(args: List<String>) {
        val inputDataStream = javaClass.getResourceAsStream("/inputData.json")
        val inputData = jacksonObjectMapper().readValue<Map<String, Any>>(inputDataStream!!)

        val mustacheFactory = DefaultMustacheFactory()
        val mustache = mustacheFactory.compile("template.html")

        println("Generating html from template")
        val stringWriter = StringWriter()
        mustache.execute(stringWriter, inputData)
        val generatedHtml = stringWriter.toString()
        //val xHtml = convertToXhtml(generatedHtml)


        println("Generated html: ${generatedHtml.length} chars")
        println(generatedHtml)

        println("Generating pdf from html")
        val renderer = ITextRenderer()
        val baseUrl: String = FileSystems
                .getDefault()
                .getPath("src", "main", "resources")
                .toUri()
                .toURL()
                .toString()
        renderer.setDocumentFromString(generatedHtml,baseUrl)
        renderer.layout()


        // And finally, we create the PDF:
        val outputStream: OutputStream = FileOutputStream("test.pdf")
        renderer.createPDF(outputStream)
        outputStream.close()

//        val pdf = Files.createTempFile(null, ".pdf").toFile()
//        FileOutputStream(pdf).use {
//            renderer.createPDF(it)
//        }

//        println("Generating pdf: ${pdf.absoluteFile}")
//        Desktop.getDesktop().open(pdf)

    }

//  private fun convertToXhtml(html: String): String {
//    val tidy = Tidy()
//    tidy.inputEncoding = "UTF-8"
//    tidy.outputEncoding = "UTF-8"
//    tidy.xhtml = true
//
//    val inputStream = ByteArrayInputStream(html.toByteArray(charset("UTF-8")))
//    val outputStream = ByteArrayOutputStream()
//    val dom = tidy.parseDOM(inputStream, outputStream)
//    return outputStream.toString("UTF-8")
//}

}
