package demo.networkcheck.task;

import android.os.AsyncTask;
import android.widget.EditText;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;

import demo.networkcheck.MainActivity;
import demo.networkcheck.constants.NetWorkConstant;
import demo.networkcheck.utils.IpUtils;

/**
 * Created by anping on 15-6-10.
 */
public class TalentTask extends AsyncTask {
    public EditText textView;

    public TalentTask(EditText textView) {
        super();
        this.textView = textView;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("==========telnet=========").append("\n");
        for (String ip : IpUtils.getIps()) {

            for (int port : IpUtils.getPort()) {
                TelnetClient tc = new TelnetClient();
                try {
                    tc.setConnectTimeout(NetWorkConstant.telnetTimeOut);
                    tc.connect(ip, port);
                    boolean result = tc.isConnected();
                    boolean avalibale = tc.isAvailable();
                    if (result) {
                        stringBuffer.append(ip + ":" + port).append("telent成功").append("\n");
                    }
                    tc.disconnect();
                } catch (IOException e) {
                    stringBuffer.append("\n").append(ip + ":" + port).append("\n").append(e).append("\n");
                }
            }

        }

        return stringBuffer;

    }

    @Override
    protected void onPostExecute(Object o) {
        StringBuffer sb = new StringBuffer();
        sb.append(textView.getText());
        if (o == null) {
            sb.append("\n");
            sb.append("所有ip telent 失败").append("\n");
            textView.setText(sb);
        } else {
            StringBuffer stringBuffer = (StringBuffer) o;
            if (stringBuffer.length() <= 0) {
                sb.append("\n");
                sb.append("所有ip telent 失败").append("\n");
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
}
