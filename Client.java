import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
	private DatagramSocket ds;
	private InetAddress ia;
	private byte[] bf = new byte[256];

	public Client(DatagramSocket ds, InetAddress ia) {
		this.ds = ds;
		this.ia = ia;
	}

	public void sendThenReceive() {
		Scanner sc = new Scanner(System.in);

		while (true) {
			try {
				String msgToSend = sc.nextLine();
				bf = msgToSend.getBytes();
				DatagramPacket dp = new DatagramPacket(bf, bf.length, ia, 1234);
				ds.send(dp);
				ds.receive(dp);
				String msgFromServer = new String(dp.getData(), 0, dp.getLength());
				System.out.println("The server says you said: " + msgFromServer);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public static void main (String[] args) throws SocketException, UnknownHostException {
		DatagramSocket ds = new DatagramSocket();
		InetAddress ia = InetAddress.getByName("localhost");
		Client client = new Client(ds, ia);
		System.out.println("Send datagram packets to a server.");
		client.sendThenReceive();
	}

}