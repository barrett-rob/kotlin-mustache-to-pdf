package org.example

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.w3c.tidy.Tidy
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.*
import java.nio.file.FileSystems

fun main(args: Array<String>) {
    GeneratePdf().run(args.toList())
}

class GeneratePdf {

    private val OUTPUT_FILE = "test.pdf"
    private val UTF_8 = "UTF-8"

    fun run(args: List<String>) {
        val inputDataStream = javaClass.getResourceAsStream("/inputData1.json")
        val inputData = jacksonObjectMapper().readValue<Map<String, Any>>(inputDataStream!!)

        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.prefix = "/"
        templateResolver.suffix = ".html"
        templateResolver.templateMode = TemplateMode.HTML
        templateResolver.characterEncoding = UTF_8
        val templateEngine = TemplateEngine()
        templateEngine.setTemplateResolver(templateResolver)

        val context = Context()
        context.setVariable("data", inputData)

        val renderedHtmlContent = templateEngine.process("template", context)
        println(renderedHtmlContent)
        val xHtml: String = convertToXhtml(renderedHtmlContent)

//
//        val mustacheFactory = DefaultMustacheFactory()
//        val mustache = mustacheFactory.compile("template.html")
//
//        println("Generating html from template")
//        val stringWriter = StringWriter()
//        mustache.execute(stringWriter, inputData)
//        val generatedHtml = stringWriter.toString()
//        val xHtml = convertToXhtml(generatedHtml)
//
//
//        println("Generated html: ${generatedHtml.length} chars")
//        println(generatedHtml)

        println("Generating pdf from html")
        val renderer = ITextRenderer()
        val baseUrl: String = FileSystems
                .getDefault()
                .getPath("src", "main", "resources")
                .toUri()
                .toURL()
                .toString()
        renderer.setDocumentFromString(xHtml,baseUrl)
        renderer.layout()


        // And finally, we create the PDF:
        val outputStream: OutputStream = FileOutputStream(OUTPUT_FILE)
        renderer.createPDF(outputStream)
        outputStream.close()

//        val pdf = Files.createTempFile(null, ".pdf").toFile()
//        FileOutputStream(pdf).use {
//            renderer.createPDF(it)
//        }

//        println("Generating pdf: ${pdf.absoluteFile}")
//        Desktop.getDesktop().open(pdf)

    }

  private fun convertToXhtml(html: String): String {
    val tidy = Tidy()
    tidy.inputEncoding = "UTF-8"
    tidy.outputEncoding = "UTF-8"
    tidy.xhtml = true

    val inputStream = ByteArrayInputStream(html.toByteArray(charset("UTF-8")))
    val outputStream = ByteArrayOutputStream()
    tidy.parseDOM(inputStream, outputStream)
    return outputStream.toString("UTF-8")
}

}
