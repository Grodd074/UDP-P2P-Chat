import java.net.InetAddress;
import java.net.UnknownHostException;

public class PeerInfo {
    private String name;
    private InetAddress ia;
    private int port;

    public PeerInfo () {
        this.name = "";
        this.ia = null;
        this.port = -1;
    }

    public PeerInfo(String name, InetAddress ia, int port) {
        this.name = name;
        this.ia = ia;
        this.port = port;
    }

    public PeerInfo(PeerInfo p) {
        this(p.getName(), p.getIa(), p.getPort());
    }

    public String getName() {
        return name;
    }

    public InetAddress getIa() {
        return ia;
    }

    public int getPort() {
        return port;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIa(InetAddress ia) {
        this.ia = ia;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
