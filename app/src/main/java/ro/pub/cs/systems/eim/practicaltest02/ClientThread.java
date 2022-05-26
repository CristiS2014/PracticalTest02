package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientThread extends Thread{

    private String address;
    private int port;
    private String data;
    private TextView resultTextView;

    private Socket socket;

    public ClientThread(String address, int port, String city, TextView resultTextView) {
        this.address = address;
        this.port = port;
        this.data = city;
        this.resultTextView = resultTextView;
    }
    @Override
    public void run() {
        try {
            System.out.println(address);
            System.out.println(port);
            address = "10.0.2.15"; // hard-coded so I don't need to input local host every time
            socket = new Socket(address, port);
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(data);
            printWriter.flush();
            System.out.println("Test");
            String res;

            StringBuilder finalRes = new StringBuilder();
            while ((res = bufferedReader.readLine()) != null) {
                finalRes.append(res);
            }

            resultTextView.post(new Runnable() {
                @Override
                public void run() {
                    resultTextView.setText(finalRes);
                }
            });
        } catch (Exception e) {
            Log.e(Constants.TAG, "ERROR CLIENT THREAD");
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());

                }
            }
        }
    }

}
