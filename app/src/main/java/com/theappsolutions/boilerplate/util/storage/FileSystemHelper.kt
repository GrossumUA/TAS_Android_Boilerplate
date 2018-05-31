package com.theappsolutions.boilerplate.util.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

import com.annimon.stream.Optional
import com.theappsolutions.boilerplate.injection.ApplicationContext
import com.theappsolutions.boilerplate.util.data.RandomUtils

import org.javatuples.Pair

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.ArrayList

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import okio.Okio
import retrofit2.Response
import timber.log.Timber

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 *
 * Utils for working with files
 */
class FileSystemHelper(val context: Context) {

    fun deleteFile(path: String?): Boolean {
        if (path == null) {
            return false
        }
        val file = File(path)
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }

    fun listAssetFiles(path: String): Observable<List<String>> {
        return Observable.create { emitter ->
            val pathList = ArrayList<String>()
            listAssetFiles(path, pathList)
            emitter.onNext(pathList)
            emitter.onComplete()
        }
    }

    private fun listAssetFiles(path: String, fileList: MutableList<String>): Boolean {
        val list: Array<String>
        try {
            list = context.assets.list(path)
            if (list.size > 0) {
                // This is a folder
                for (file in list) {
                    if (!listAssetFiles("$path/$file", fileList)) {
                        return false
                    } else {
                        fileList.add("$path/$file")
                    }
                }
            }
        } catch (e: IOException) {
            return false
        }

        return true
    }

    fun copyAssetsToStorage(fileList: List<Pair<String, String>>): Observable<List<String>> {
        return Observable.create { emitter ->
            val files = ArrayList<String>()
            for (filenames in fileList) {
                Timber.i(filenames.toString())
                var inputStream: InputStream? = null
                var outputStream: OutputStream? = null
                try {
                    inputStream = context.assets.open(filenames.value0)
                    val outFile = buildPath(buildFileName(filenames.value1))
                    outputStream = FileOutputStream(outFile)
                    inputStream?.let { copyFile(it, outputStream) }
                    files.add(outFile.absolutePath)
                } catch (e: IOException) {

                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close()
                        } catch (e: IOException) {

                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close()
                        } catch (e: IOException) {

                        }

                    }
                }
            }
            emitter.onNext(files)
            emitter.onComplete()
        }
    }

    @Throws(IOException::class)
    private fun copyFile(inputStream: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        val read: Int = inputStream.read(buffer)
        while (read != -1) {
            out.write(buffer, 0, read)
        }
    }


    fun getBitmapFromAsset(context: Context, assetPath: String): Observable<Bitmap> {
        return Observable.fromCallable {
            val ims = context.assets.open(assetPath)
            BitmapFactory.decodeStream(ims)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun saveFileToDisk(
            response: Response<ResponseBody>, filename: String): Observable<Optional<File>> {
        return Observable.create { emitter ->
            try {
                var destinationFile = Optional.empty<File>()
                if (response.isSuccessful) {
                    destinationFile = Optional.of(buildPath(buildFileName(filename)))

                    val bufferedSink = Okio.buffer(Okio.sink(destinationFile.get()))
                    bufferedSink.writeAll(response.body()!!.source())
                    bufferedSink.close()

                }
                emitter.onNext(destinationFile)
                emitter.onComplete()
            } catch (ex: IOException) {
                emitter.onError(ex)
            }
        }
    }

    fun saveBitmapToDisk(bmp: Bitmap, filename: String): Observable<Optional<File>> {
        Timber.i(filename)
        return Observable.create { emitter ->
            var out: FileOutputStream? = null
            var destinationFile = Optional.empty<File>()
            try {
                destinationFile = Optional.of(buildPath(buildFileName(filename)))
                out = FileOutputStream(destinationFile.get().absolutePath)
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.close()
                emitter.onNext(Optional.of(File(filename)))
                emitter.onComplete()
            } catch (e: Exception) {
                try {
                    if (out != null) {
                        out.close()
                    }
                } catch (ee: Exception) {
                    emitter.onError(ee)
                }

                emitter.onError(e)
            }
        }
    }

    private fun buildFileName(name: String?): String {
        return "${name ?: RandomUtils().nextString()}.jpg"
    }

    private fun buildPath(filename: String): File {
        val sub = context.filesDir.path
        val pathF = "$sub/images/"
        File(pathF).mkdirs()
        return File(pathF + filename)
    }

    companion object {
        fun getAssetsFilePath(fileName: String): Uri {
            return Uri.parse("file:///android_asset/$fileName")
        }
    }
}
