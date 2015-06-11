package demo.networkcheck.task;

import android.os.AsyncTask;
import android.widget.EditText;

import java.net.InetAddress;
import java.net.UnknownHostException;

import demo.networkcheck.MainActivity;

/**
 * Created by anping on 15-6-11.
 */
public class DigTask extends AsyncTask {

    public EditText textView;

    public DigTask(EditText textView) {
        super();
        this.textView = textView;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("=====ip dig=====").append("\n");
        try {//解析域名或者ip
            InetAddress[] addresses = InetAddress.getAllByName("www.baidu.com");
            if (addresses == null || addresses.length <= 0) {
                stringBuffer.append("解析结果为空").append("\n");
            } else {
                for (InetAddress address : addresses) {
                    stringBuffer.append(address.getHostAddress()).append("\n");
                }
            }
            return stringBuffer;
        } catch (UnknownHostException e) {
            stringBuffer.append("\n").append(e).append("\n");
        }
        return stringBuffer;
    }

    @Override
    protected void onPostExecute(Object o) {
        StringBuffer sb = new StringBuffer();
        sb.append(textView.getText());

        if (o == null) {
            sb.append("\n");
            sb.append("所有ip dig 失败").append("\n");
            textView.setText(sb);
        } else {
            StringBuffer stringBuffer = (StringBuffer) o;
            if (stringBuffer.length() <= 0) {
                sb.append("\n");
                sb.append("所有ip dig 失败").append("\n");
                textView.setText(sb);
            } else {
                sb.append(stringBuffer).append("\n");
                textView.setText(sb);
            }
        }
        synchronized (MessageStoreTask.object) {
            MainActivity.num++;
        }
        MessageStoreTask.store(textView.getText().toString());
    }
}
