package demo.networkcheck.task;

import android.os.AsyncTask;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import demo.networkcheck.MainActivity;
import demo.networkcheck.constants.NetWorkConstant;

/**
 * Created by anping on 15-6-10.
 */
public class TraceRouteTask extends AsyncTask {
    public EditText textView;
    private static final String PING_TEMPLATE = "ping  -W %d -c 1 -t %d  %s";
    List<String>  ips  = new ArrayList<>();
    public TraceRouteTask(EditText textView, List<String> ips) {
        super();
        this.textView = textView;
        this.ips=ips;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        final StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("====traceRoute=======").append("\n");
        for (String ip : ips) {
            stringBuffer.append(ip).append("开始").append("\n");
            int i = 1;
            while (true) {
                String result = doPing2(ip, i);
                stringBuffer.append(result);
                if (!result.toLowerCase().contains("from")) {
                    break;
                }
                i++;
            }
            stringBuffer.append(ip).append("结束").append("\n");

        }
        return stringBuffer;
    }

    @Override
    protected void onPostExecute(Object o) {
        StringBuffer sb = new StringBuffer();
        sb.append(textView.getText());
        if (o == null) {
            sb.append("\n");
            sb.append("所有ip trace 失败").append("\n");
            textView.setText(sb);
        } else {
            StringBuffer stringBuffer = (StringBuffer) o;
            if (stringBuffer.length() <= 0) {
                sb.append("\n");
                sb.append("所有ip trace 失败").append("\n");
                textView.setText(sb);
            } else {
                sb.append(stringBuffer);

                textView.setText(sb);
            }

        }
        synchronized (MessageStoreTask.object) {
            MainActivity.num++;
        }
        MessageStoreTask.store(textView.getText().toString());
    }

    public static String doPing2(String address, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader reader = null;
        Process process = null;
        try {
            String pingCmd = String.format(PING_TEMPLATE, NetWorkConstant.PintTimeOut,i, address);
            process = Runtime.getRuntime().exec(pingCmd);
            reader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                stringBuffer.append(line).append("\n");
                line = reader.readLine();
            }
            process.waitFor();
        } catch (IOException e) {
            stringBuffer.append("\n").append(address).append("\n").append(e).append("\n");
        } catch (Exception e) {
            stringBuffer.append("\n").append(address).append("\n").append(e).append("\n");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                // ignore;
            }
            if (process != null) {
                process.destroy();
            }
        }
        return stringBuffer.toString();
    }
}
