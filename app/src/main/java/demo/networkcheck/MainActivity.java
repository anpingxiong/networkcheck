package demo.networkcheck;


import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import demo.networkcheck.task.DigTask;
import demo.networkcheck.utils.IpUtils;
import demo.networkcheck.task.MessageStoreTask;
import demo.networkcheck.task.PingTask;
import demo.networkcheck.task.TalentTask;
import demo.networkcheck.task.TraceRouteTask;

public class MainActivity extends ActionBarActivity {
    public static final  int TaskNum  = 5;//有7个任务执行
     public static volatile  int num   = 0;
    private EditText  iptextView;
    private ProgressBar  progressBar;
    private TextView  textView;
    private Button  button;
    private static ExecutorService sExecutor =  Executors.newFixedThreadPool(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iptextView  = (EditText)findViewById(R.id.editText);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        textView = (TextView)findViewById(R.id.textView);
        button  = (Button)findViewById(R.id.button);
        button.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), "network.txt");

                Uri uri = Uri.fromFile(file);
                Intent mRequestFileIntent = new Intent(Intent.ACTION_SEND);
                mRequestFileIntent.putExtra(Intent.EXTRA_STREAM, uri);
                mRequestFileIntent.setType("text/plain");
                startActivity(Intent.createChooser(mRequestFileIntent, "Select"));

            }
        });

        MessageStoreTask.init(textView, button, progressBar);
        DigTask digUtils  = new DigTask(iptextView);
        digUtils.executeOnExecutor(sExecutor);

        TalentTask talentUtils =  new TalentTask(iptextView);
        talentUtils.executeOnExecutor(sExecutor);
        PingTask pingUtils  = new PingTask(iptextView);
        pingUtils.executeOnExecutor(sExecutor);

        TraceRouteTask traceRouteUtils1  = new TraceRouteTask(iptextView, IpUtils.getIps1());
        traceRouteUtils1.executeOnExecutor(sExecutor);

        TraceRouteTask traceRouteUtils2  = new TraceRouteTask(iptextView, IpUtils.getIps2());
        traceRouteUtils2.executeOnExecutor(sExecutor);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
