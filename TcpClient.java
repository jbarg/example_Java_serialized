import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketAddress;


/**
 * TcpClient.java
 *
 * This class works in conjunction with TcpServer.java and TcpPayload.java
  *
 * This client test class connects to server class TcpServer, and in response,
* it receives a serialized an instance of TcpPayload.
 */

public class TcpClient
{
    public final static String SERVER_HOSTNAME = "127.0.0.1";
    public final static int COMM_PORT = 8080;  // socket port for client comms

    private Socket socket;
    private TcpPayload payload;

    /** Default constructor. */
    public TcpClient()
    {
        try
        {
            this.payload = new TcpPayload();
            this.socket = new Socket();
            SocketAddress sockaddr = new InetSocketAddress(SERVER_HOSTNAME, COMM_PORT);
            socket.connect(sockaddr, 10000);
            OutputStream oStream = socket.getOutputStream();
            ObjectOutputStream ooStream = new ObjectOutputStream(oStream);
            ooStream.writeObject(this.payload);  // send serilized payload


            InputStream iStream = this.socket.getInputStream();
            ObjectInputStream oiStream = new ObjectInputStream(iStream);

            this.payload = (TcpPayload) oiStream.readObject();
        }
        catch (UnknownHostException uhe)
        {
            System.out.println("Don't know about host: " + SERVER_HOSTNAME);
            System.exit(1);
        }
        catch (IOException ioe)
        {
            System.out.println("Couldn't get I/O for the connection to: " +
                SERVER_HOSTNAME + ":" + COMM_PORT);
            System.exit(1);
        }
        catch(ClassNotFoundException cne)
        {
            System.out.println("Wanted class TcpPayload, but got class " + cne);
        }
        System.out.println("Received payload:");
        System.out.println(this.payload.toString());
    }

    /**
     * Run this class as an application.
     */
    public static void main(String[] args)
    {
        TcpClient tcpclient = new TcpClient();
    }
}