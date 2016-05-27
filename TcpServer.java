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


/**
 * This class works in conjunction with TcpClient.java and TcpPayload.java
 *
 * This server test class opens a socket on localhost and waits for a client
 * to connect. When a client connects, this server serializes an instance of
 * TcpPayload and sends it to the client.
 */

public class TcpServer
{
    public final static int COMM_PORT = 9999;  // socket port for client comms

    private ServerSocket serverSocket;
    private InetSocketAddress inboundAddr;
    private TcpPayload payload;
     private TcpPayloadExploit foobar;

    /** Default constructor. */
    public TcpServer()
    {
        this.payload = new TcpPayload();
        initServerSocket();
        try
        {
            while (true)
            {
                // listen for and accept a client connection to serverSocket
                Socket sock = this.serverSocket.accept();

                InputStream iStream = sock.getInputStream();
                ObjectInputStream oiStream = new ObjectInputStream(iStream);

                TcpPayload rec = new TcpPayload();
                rec= (TcpPayload) oiStream.readObject();

                OutputStream oStream = sock.getOutputStream();
                ObjectOutputStream ooStream = new ObjectOutputStream(oStream);
                ooStream.writeObject(rec);  // send serilized payload
                ooStream.close();
                Thread.sleep(1000);
            }
        }
        catch (SecurityException se)
        {
            System.err.println("Unable to get host address due to security.");
            System.err.println(se.toString());
            System.exit(1);
        }
        catch (IOException ioe)
        {
            System.err.println("Unable to read data from an open socket.");
            System.err.println(ioe.toString());
            System.exit(1);
        }
        catch (InterruptedException ie) { }  // Thread sleep interrupted
        catch(ClassNotFoundException cne)
        {
            System.out.println("Wanted class TcpPayload, but got class " + cne);
        }
        finally
        {
            try
            {
                this.serverSocket.close();
            }
            catch (IOException ioe)
            {
                System.err.println("Unable to close an open socket.");
                System.err.println(ioe.toString());
                System.exit(1);
            }
        }
    }

    /** Initialize a server socket for communicating with the client. */
    private void initServerSocket()
    {
        this.inboundAddr = new InetSocketAddress(COMM_PORT);
        try
        {
            this.serverSocket = new java.net.ServerSocket(COMM_PORT);
            assert this.serverSocket.isBound();
            if (this.serverSocket.isBound())
            {
                System.out.println("SERVER inbound data port " +
                    this.serverSocket.getLocalPort() +
                    " is ready and waiting for client to connect...");
            }
        }
        catch (SocketException se)
        {
            System.err.println("Unable to create socket.");
            System.err.println(se.toString());
            System.exit(1);
        }
        catch (IOException ioe)
        {
            System.err.println("Unable to read data from an open socket.");
            System.err.println(ioe.toString());
            System.exit(1);
        }
    }

    /**
     * Run this class as an application.
     */
    public static void main(String[] args)
    {
        TcpServer tcpServer = new TcpServer();
    }
}