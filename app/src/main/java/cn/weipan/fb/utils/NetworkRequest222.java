package cn.weipan.fb.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkRequest222 extends Thread implements Runnable {
    private String mUrl = "182.254.219.119";
    private int serverPort = 52435;
//        private String mUrl = "182.254.146.121";
//    private int serverPort = 2000;
    private String mSendStr = null;

    public NetworkRequest222(String mSendStr) {
        this.mUrl = mUrl;
        this.serverPort = serverPort;
        this.mSendStr = mSendStr;
    }

    public void run() {
        getSocketRegister();
    }

    private String getSocketRegister() {
        String result = null;
        try {

//            Socket socket = new Socket();
//            SocketAddress socketAddress = new InetSocketAddress(mUrl, serverPort);
//            socket.connect(socketAddress, 20000);
            Socket socket = new Socket(mUrl, serverPort);
//            socket.setSoTimeout(20000);
            // 向服务端程序发送数据
            OutputStream ops = socket.getOutputStream();
            OutputStreamWriter opsw = new OutputStreamWriter(ops);
            BufferedWriter bw = new BufferedWriter(opsw);
            bw.write(mSendStr);
            bw.flush();
            // 从服务端程序接收数据
            InputStream ips = socket.getInputStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int len = -1;
            while ((len = ips.read(data)) != -1) {
                outStream.write(data, 0, len);
                break;
            }
            data = null;
            result = new String(outStream.toByteArray(), "GBK");
            socket.close();
            ops.close();
            opsw.close();
            bw.close();
            ips.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mListener != null) {
            mListener.onResult22(result);
        }
        return result;
    }

    private ReponseListener mListener = null;

    public void setListener(ReponseListener listener) {
        mListener = listener;
    }


    public interface ReponseListener {
        void onResult22(String result);
    }
}
