package com.example.simplybook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.service.autofill.OnClickAction
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.simplybook.ml.Model

import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class Image : AppCompatActivity() {
    lateinit var select_image_button: Button
    lateinit var link_button: Button
    lateinit var make_prediction: Button
    lateinit var img_view: ImageView
    lateinit var text_view: TextView
    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        select_image_button = findViewById(R.id.button)
        make_prediction = findViewById(R.id.button2)
        link_button = findViewById(R.id.button3)
        img_view = findViewById(R.id.imageView)
        text_view = findViewById(R.id.textView)

        val actionBar = supportActionBar
        actionBar!!.title = "Image"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val labels = application.assets.open("labels.txt").bufferedReader().use { it.readText() }.split("\n")

        select_image_button.setOnClickListener(View.OnClickListener {
            Log.d("mssg", "button pressed")
            var intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(intent, 100)
        })

        link_button.setOnClickListener(View.OnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://sharishkumar192.github.io/Simply-Book/Website/src/index.html"))
            startActivity(i)
        })


        make_prediction.setOnClickListener(View.OnClickListener {
            var resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val model = Model.newInstance(this)

            var tbuffer = TensorImage.fromBitmap(resized)
            var byteBuffer = tbuffer.buffer

// Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
            inputFeature0.loadBuffer(byteBuffer)


// Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer


                var max = getMax(outputFeature0.floatArray)

                text_view.setText(labels[max])

            link_button.setOnClickListener(View.OnClickListener {
                if (max == 0) {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://sharishkumar192.github.io/Simply-Book/Website/src/pages/Std%20VIII.html"))
                    startActivity(i)
                } else if (max == 1) {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://sharishkumar192.github.io/Simply-Book/Website/src/pages/Std%20XII.html"))
                    startActivity(i)
                } else if (max == 2) {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://sharishkumar192.github.io/Simply-Book/Website/src/pages/Std%20XII.html"))
                    startActivity(i)
                } else if (max == 3) {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://sharishkumar192.github.io/Simply-Book/Website/src/pages/Std%20XI.html"))
                    startActivity(i)
                } else if (max == 4) {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://sharishkumar192.github.io/Simply-Book/Website/src/pages/Std%20IX.html"))
                    startActivity(i)
                } else if (max == 5) {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://sharishkumar192.github.io/Simply-Book/Website/src/pages/Std%20X.html"))
                    startActivity(i)
                } else {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://sharishkumar192.github.io/Simply-Book/Website/src/index.html"))
                    startActivity(i)
                }
            })
// Releases model resources if no longer used.
            model.close()
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        img_view.setImageURI(data?.data)

        var uri: Uri? = data?.data
        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
    }


    fun getMax(arr: FloatArray): Int {
        var ind = 0;
        var min = 0.0f;


        for (i in 0..5) {
            if (arr[i] > min) {
                min = arr[i]
                ind = i;
            }
        }

        return ind
    }

}