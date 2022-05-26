package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicationThread extends Thread{

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }

        try {

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");
            String request = bufferedReader.readLine();
            String arr[] = request.split(" ");
            String operation = arr[0];
            String op1 = arr[1];
            String op2 = arr[2];
            int rez = 0;
            String rezStr = "";
            if (operation.equals(Constants.ADD)) {
                rez = Integer.parseInt(op1);

                try {
                    rez = Math.addExact(rez, Integer.parseInt(op2));
                    rezStr = Integer.toString(rez);
                } catch (ArithmeticException ar) {
                    rezStr = "overflow";
                }
            } else if (operation.equals(Constants.MUL)) {
                sleep(2000);
                try {
                    rez = Integer.parseInt(op1);
                    rez = Math.multiplyExact(rez, Integer.parseInt(op2));
                    rezStr = Integer.toString(rez);
                } catch (ArithmeticException ar) {
                    rezStr = "overflow";
                }
            } else {
                Log.e(Constants.TAG, "OPERATION NOT SUPPORTED");
            }

            printWriter.println(rezStr);
            printWriter.flush();
        } catch (Exception e) {
            Log.e(Constants.TAG, "Exception in comm thread");
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
    }
}
