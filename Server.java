import java.net.*;
import java.io.*;

public class Server implements Runnable {
	private DatagramSocket serverDgSocket;
    private byte[] msgBuf = new byte[256];
    private int port;

    public Server(int p) throws SocketException {
        this.serverDgSocket = new DatagramSocket(p);
    }

    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket dp = new DatagramPacket(msgBuf, msgBuf.length);
                serverDgSocket.receive(dp);

                InetAddress senderIa = dp.getAddress();
                int senderPort = dp.getPort();

                String msgFromClient = new String(dp.getData(), 0, dp.getLength());
                System.out.println("Message -> " + msgFromClient);

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
