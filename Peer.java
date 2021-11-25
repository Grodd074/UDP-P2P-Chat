import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class Peer {
    private DatagramSocket clientDgSocket = new DatagramSocket();
    private byte[] msgBuf = new byte[256];
    private Server server;
    private PeerInfo myPeerInfo;

    // Nome da maquina -> (Nome, InetAdress, Port)
    private Map<String, PeerInfo> knownPeers = new HashMap<>();

    public Peer(PeerInfo p) throws SocketException {
        this.myPeerInfo = new PeerInfo(p);
        // Abre servidor a escuta na port
        Thread st = new Thread(new Server(p.getPort()));
        st.start();
    }

    public void addToPeerList (PeerInfo p) {
        this.knownPeers.put(p.getName(), p);
    }

    public Set<String> getPeerNameList() {
        return this.knownPeers.keySet();
    }

    public void sendMsgToPeer (String destPeerName, String msgToSend) throws IOException {
        PeerInfo destPeer = knownPeers.get(destPeerName);
        msgBuf = msgToSend.getBytes();
        DatagramPacket dp = new DatagramPacket(msgBuf, msgBuf.length, destPeer.getIa(), destPeer.getPort());
        clientDgSocket.send(dp);
    }

    public void sendMsgToAll (String msgToSend) throws IOException {
        for (PeerInfo pInfo : knownPeers.values()) {
            msgBuf = msgToSend.getBytes();
            DatagramPacket dp = new DatagramPacket(msgBuf, msgBuf.length, pInfo.getIa(), pInfo.getPort());
            clientDgSocket.send(dp);
        }
    }

    public static void main (String[] args) throws IOException {
        // args[0] -> Nome da maquina
        // args[1] -> Ip da maquina
        // args[2] -> Port
        InetAddress ia = InetAddress.getByName(args[1]);
        int port = Integer.parseInt(args[2]);

        PeerInfo pinfo = new PeerInfo(args[0], ia, port);
        Peer p = new Peer(pinfo);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean running = true;
        while (running) {
            String cmd = reader.readLine();

            if (cmd.equals("add")) {
                System.out.println("Adicionar peer a lista de peers conheçidos: <Nome_Maquina Ip Port>");
                String pinfoStr = reader.readLine();
                PeerInfo otherpinfo = new PeerInfo();
                StringTokenizer tokenizer = new StringTokenizer(pinfoStr);
                otherpinfo.setName(tokenizer.nextToken());
                otherpinfo.setIa(InetAddress.getByName(tokenizer.nextToken()));
                otherpinfo.setPort(Integer.parseInt(tokenizer.nextToken()));
                p.addToPeerList(otherpinfo);
            } else if (cmd.equals("msg2peer")) {
                System.out.println("destPeerName msg");
                String str = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(str);
                String destPeerName = tokenizer.nextToken();

                StringBuilder sb = new StringBuilder();
                while (tokenizer.hasMoreTokens()) {
                    sb.append(tokenizer.nextToken());
                    sb.append(" ");
                }

                String msg = sb.toString();
                p.sendMsgToPeer(destPeerName, msg);
            } else if (cmd.equals("msg2all")) {
                System.out.println("<msg>");
                String str = reader.readLine();
                p.sendMsgToAll(str);
            } else if (cmd.equals("list")) {
                for (String n : p.getPeerNameList()) {
                    System.out.println(n);
                }
            } else if (cmd.equals("quit")) {
                running = false;
            } else {
                System.out.println("unknown cmd: " + cmd);
            }
        }
    }
}
