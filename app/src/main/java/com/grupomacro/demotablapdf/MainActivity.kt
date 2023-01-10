package com.grupomacro.demotablapdf

import android.Manifest
import android.graphics.Bitmap
import com.itextpdf.kernel.colors.ColorConstants
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.grupomacro.demotablapdf.databinding.ActivityMainBinding
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.*
import com.permissionx.guolindev.PermissionX
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            btnPdf.setOnClickListener {
                pedirPermisos()
            }
        }
    }

    private fun generarPdf(){
        val usuario1 = Usuario("Nelson","Ek",31)
        val usuario2 = Usuario("Nelson","Ek",31)
        val usuario3 = Usuario("Nelson","Ek",31)
        val usuario4 = Usuario("Nelson","Ek",31)

        val dataTable = mutableListOf<Usuario>()
        dataTable.add(usuario1)
        dataTable.add(usuario2)
        dataTable.add(usuario3)
        dataTable.add(usuario4)

        try {
            val path = Environment.getExternalStorageDirectory().absolutePath.plus("/EjemploPdf")
            val dir = File(path)
            if (!dir.exists())
                dir.mkdir()

            val file = File(dir,"Demo.pdf")
            val fileOutputStream = FileOutputStream(file)

            val writer = PdfWriter(file)
            val pdfDocument = PdfDocument(writer)
            val document = Document(pdfDocument)

            //creando una imagen
            //val imageFile = "./src/main/drawable/logo_macropay.png"
            //val data = ImageDataFactory.create(imageFile)
            //val image = Image(data)

            //creando un parrafo
            val paragraph = Paragraph("Ejemplo de tabla")
            document.add(paragraph)

            //creando una imagen
            val dataImage = createImage()
            val imageData = ImageDataFactory.create(dataImage)
            val image = Image(imageData)
            document.add(image)

            //creando una tabla
            val columnWidth = floatArrayOf(100f,100f,100f)
            val table = Table(columnWidth)
            table.addCell("Name")
            table.addCell("Apellido")
            table.addCell("Edad")

            var active = true

            for (item in dataTable){

                val colorFinal = when(active){
                    true -> ColorConstants.CYAN
                    else -> ColorConstants.WHITE
                }

                active = !active

                var cellNombre = Cell()
                cellNombre.setBackgroundColor(colorFinal)
                val textNombre = Paragraph(item.nombre)
                cellNombre.add(textNombre)
                cellNombre.setBorder(Border.NO_BORDER)
                table.addCell(cellNombre)

                var cellApellido = Cell()
                val textApellido = Paragraph(item.apellido)
                cellApellido.setBackgroundColor(colorFinal)
                cellApellido.add(textApellido)
                cellApellido.setBorder(Border.NO_BORDER)
                table.addCell(cellApellido)

                var cellEdad = Cell()
                val textEdad = Paragraph(item.edad.toString())
                cellEdad.setBackgroundColor(colorFinal)
                cellEdad.add(textEdad)
                cellEdad.setBorder(Border.NO_BORDER)
                table.addCell(cellEdad)

            }
            document.add(table)

            document.close()



        }catch (ex:Exception){
            Toast.makeText(this, ex.message!!, Toast.LENGTH_SHORT).show()
        }


    }

    private fun createImage():ByteArray {
        val d = AppCompatResources.getDrawable(this,R.drawable.logo_macropay)
        val bitmap = (d as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
        return stream.toByteArray()
    }

    private fun pedirPermisos(){
        PermissionX.init(this)
            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    generarPdf()
                } else {
                    Toast.makeText(this, "Permisos negados: $deniedList", Toast.LENGTH_LONG).show()
                }
            }
    }



}