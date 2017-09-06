package org.opennms.jdhcp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * This class represents a Socket for sending DHCP Messages
 * @author Jason Goldschmidt 
 * @see java.net.DatagramSocket
 */


public class DHCPSocket extends DatagramSocket  {
    protected static int sPACKETSIZE = 1500; // default MTU for ethernet
    private static final int DEFAULT_SO_TIMEOUT = 3000; // 3 second socket timeout

    /** 
     * Constructor for creating DHCPSocket on a specific port on the local machine. 
     * @param inPort the port for the application to bind.
     */

    public DHCPSocket (final int inPort) throws SocketException {
        super(inPort);
        setSoTimeout(DEFAULT_SO_TIMEOUT);
    }

    /**
     * Sets the Maximum Transfer Unit for the UDP DHCP Packets to be set.
     * Default is 1500, MTU for Ethernet
     * @param inSize integer representing desired MTU
     */

    public static void setMTU(final int inSize) {
        sPACKETSIZE = inSize;
    }

    /**
     * Returns the set MTU for this socket
     * @return the Maximum Transfer Unit set for this socket
     */

    public static int getMTU() {
        return sPACKETSIZE;
    }

    /**
     * Sends a DHCPMessage object to a predefined host.
     * @param inMessage well-formed DHCPMessage to be sent to a server
     */

    public synchronized void send(final DHCPMessage inMessage) throws IOException {
        final byte[] data = inMessage.externalize();
        final InetAddress dest = inMessage.getDestination();

        final DatagramPacket outgoing = new DatagramPacket(data, data.length, dest, inMessage.getPort());
        send(outgoing); // send outgoing message
    }

    /** 
     * Receives a datagram packet containing a DHCP Message into
     * a DHCPMessage object.
     * @return true if message is received, false if timeout occurs.  
     * @param outMessage DHCPMessage object to receive new message into
     */

    public synchronized boolean receive(final DHCPMessage outMessage) {
        try {
            final DatagramPacket incoming = new DatagramPacket(new byte[sPACKETSIZE], sPACKETSIZE);
            receive(incoming); // block on receive for SO_TIMEOUT

            outMessage.internalize(incoming.getData());
        } catch (final Exception e) {
            return false;
        }
        return true;
    }

}
