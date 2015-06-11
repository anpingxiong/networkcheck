package demo.networkcheck.task;

import android.os.AsyncTask;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import demo.networkcheck.MainActivity;
import demo.networkcheck.utils.IpUtils;

/**
 * Created by anping on 15-6-10.
 */
public class PingTask extends AsyncTask {
    public EditText textView;
    private static final String PING_TEMPLATE = "ping -W 500 -i 0.2 -c 2 %s";

    public PingTask(EditText textView) {
        super();
        this.textView = textView;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        final StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("====ping=======").append("\n");
        for (String ip : IpUtils.getIps()) {
            stringBuffer.append(ip).append("开始").append("\n");
            stringBuffer.append(doPing2(ip));
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
            sb.append("所有ip ping 失败").append("\n");
            textView.setText(sb);
        } else {
            StringBuffer stringBuffer = (StringBuffer) o;
            if (stringBuffer.length() <= 0) {
                sb.append("\n");
                sb.append("所有ip ping 失败").append("\n");
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

    public static String doPing2(String address) {
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader reader = null;
        Process process = null;
        try {
            String pingCmd = String.format(PING_TEMPLATE, address);
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
