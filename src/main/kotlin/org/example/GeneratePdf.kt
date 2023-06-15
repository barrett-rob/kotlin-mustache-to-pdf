package org.example

import com.fasterxml.jackson.module.kotlin.*
import com.github.mustachejava.DefaultMustacheFactory
import org.xhtmlrenderer.pdf.ITextRenderer
import java.awt.Desktop
import java.io.FileOutputStream
import java.io.StringWriter
import java.nio.file.Files

fun main(args: Array<String>) {
    GeneratePdf().run(args.toList())
}

class GeneratePdf {

    fun run(args: List<String>) {
        val inputDataStream = javaClass.getResourceAsStream("/inputData.json")
        val inputData = jacksonObjectMapper().readValue<Map<String, Any>>(inputDataStream!!)

        val mustacheFactory = DefaultMustacheFactory()
        val mustache = mustacheFactory.compile("template.mustache")

        println("Generating html from template")
        val stringWriter = StringWriter()
        mustache.execute(stringWriter, inputData)
        val generatedHtml = stringWriter.toString()

        println("Generated html: ${generatedHtml.length} chars")
        println(generatedHtml)

        println("Generating pdf from html")
        val renderer = ITextRenderer()
        renderer.setDocumentFromString(generatedHtml)
        renderer.layout()

        val pdf = Files.createTempFile(null, ".pdf").toFile()
        FileOutputStream(pdf).use {
            renderer.createPDF(it)
        }

        println("Generating pdf: ${pdf.absoluteFile}")
        Desktop.getDesktop().open(pdf)

    }

}
