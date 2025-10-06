package com.example.pagos.util
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.util.EnumMap
import kotlin.jvm.java

object QrGenerator {

    fun generateQrBitmap(
        content: String,
        size: Int = 512,
        foregroundColor: Int = Color.BLACK,
        backgroundColor: Int = Color.WHITE
    ): Bitmap? {
        return try {
            val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hints[EncodeHintType.MARGIN] = 1
            hints[EncodeHintType.CHARACTER_SET] = "UTF-8"

            val bitMatrix: BitMatrix = MultiFormatWriter().encode(
                content,
                BarcodeFormat.QR_CODE,
                size,
                size,
                hints
            )

            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
            for (x in 0 until size) {
                for (y in 0 until size) {
                    bitmap.setPixel(
                        x,
                        y,
                        if (bitMatrix[x, y]) foregroundColor else backgroundColor
                    )
                }
            }
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun generateQrBitmapWithLogo(
        content: String,
        logo: Bitmap? = null,
        size: Int = 512
    ): Bitmap? {
        val baseQr = generateQrBitmap(content, size) ?: return null

        logo?.let {
            // Redimensionar logo para que sea 1/5 del tamaño del QR
            val logoSize = size / 5
            val scaledLogo = Bitmap.createScaledBitmap(it, logoSize, logoSize, true)

            // Crear un bitmap mutable para dibujar
            val result = baseQr.copy(Bitmap.Config.ARGB_8888, true)

            // Calcular posición para centrar el logo
            val left = (size - logoSize) / 2
            val top = (size - logoSize) / 2

            // Dibujar el logo en el centro del QR
            val canvas = android.graphics.Canvas(result)
            canvas.drawBitmap(scaledLogo, left.toFloat(), top.toFloat(), null)

            return result
        }

        return baseQr
    }
}