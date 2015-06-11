package demo.networkcheck.task;

import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import demo.networkcheck.MainActivity;

/**
 * Created by anping on 15-6-11.
 */
public class MessageStoreTask {

    public static volatile  Object object= new Object();

    public static TextView textView  = null;
    public static Button button  =null;
    public static ProgressBar progressBar=null;
    public static  void init(TextView  textView ,Button button,ProgressBar progressBar){
        MessageStoreTask.textView = textView;
        MessageStoreTask.button = button;
        MessageStoreTask.progressBar  = progressBar;
    }
    public static void store(String sb) {

        synchronized (object){
            if(MainActivity.num<MainActivity.TaskNum){
                return;
            }
        }
        progressBar.setVisibility(View.GONE);
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "network.txt");
            FileWriter fileWriter = null;
            BufferedWriter bufferedWriter = null;
            try {
                fileWriter = new FileWriter(file);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(sb.toString());
                bufferedWriter.flush();
                textView.setText("hello,数据以保存在" + file.getAbsolutePath());
                button.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                    if (bufferedWriter != null) {

                        bufferedWriter.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
