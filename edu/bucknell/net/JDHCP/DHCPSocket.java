
// 	$Id: DHCPSocket.java,v 1.2 1999/07/09 09:04:39 jgoldsch Exp $	

package edu.bucknell.eg.JDHCP;

import java.net.*;

/**
 * This class represents a Socket for sending DHCP Messages
 * @author Jason Goldschmidt 
 * @version 1.1.0  1999/07/08 03:37:24
 * @see java.net.DatagramSocket
 */


public class DHCPSocket extends DatagramSocket  {

    private int PACKET_SIZE = 1500; // default MTU for ethernet
    private int defaultSOTIME_OUT = 3000;

    /** 
     * Constructor for creating DHCPSocket on a specific port on the local
     * machine. 
     * @param inPort the port for the application to bind.
     */

    public DHCPSocket (int inPort) throws SocketException {
	super();
	setSoTimeout(defaultSOTIME_OUT); // set default timeout option
    }
    
    /**
     * Sets the Maximum Transfer Unit for the UDP DHCP Packets to be set.
     * Default is 1500, MTU for Ethernet
     * @param inSize integer representing desired MTU
     */
    
    public void setMTU(int inSize) {
	PACKET_SIZE = inSize;
    }

    /**
     * Returns the set MTU for this socket
     * @return the Maximum Transfer Unit set for this socket
     */
    
    public int getMTU() {
	return PACKET_SIZE;
    }
    
    /**
     * Sends a DHCPMessage object to a predifined host.
     * @param inMessage well-formed DHCPMessage to be sent to a server
     */
       
    public synchronized void send(DHCPMessage inMessage)
	 throws java.io.IOException {
	     byte[] data = inMessage.externalize();
	     DatagramPacket outgoing = new DatagramPacket(data,
							  PACKET_SIZE,
							  inMessage.
							  getDestinationAddress(),
							  inMessage.getPort());
	     gSocket.send(outgoing); // send outgoing message
    }

    /** 
     * Receives a datagram packet containing a DHCP Message into
     * a DHCPMessage object.
     * @return true if message is received, false if timeout occurs.  
     * @param outMessage DHCPMessage object to receive new message into
     */
    
    public synchronized boolean receive(DHCPMessage outMessage)  {
	try {
	    DatagramPacket incoming = 
		new DatagramPacket(new byte[PACKET_SIZE], 
				   PACKET_SIZE);
	    gSocket.receive(incoming); // block on receive for SO_TIMEOUT

	    outMessage.internalize(incoming.getData());
	} catch (java.io.IOException e) {
	    return false;
        }  // end catch    
	return true;
    }
    
    
    
}

    




