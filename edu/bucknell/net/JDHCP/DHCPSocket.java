/*DHCPSocket class, part of DHCP API. 
  allows for a Datagram Socket implementation for DHCP to use for 
  sending and receiving DHCP Messages
  written by Jason Goldschmidt 4/21/98 
*/

// 	$Id: DHCPSocket.java,v 1.1 1999/02/02 02:21:58 jgoldsch Exp $	
package JDHCP;

import java.net.*;
import java.io.*;
import JDHCP.DHCPMessage;
/**
 * This class represents a DHCP Message. 
 * @author Jason Goldschmidt 
 * @version 1.12  1998/05/28 03:37:24
 * @since    JDK1.0
 */


public class DHCPSocket extends DatagramSocket  {

    DatagramSocket gSocket;
    int PACKET_SIZE = 8192;		// smaller????
    int defaultSOTIME_OUT = 3000;

    /** 
     * Constructor for creating DHCPSocket on a specific port on the local
     * machine. 
     * @param inPort the port for the application to bind.
     */

    public DHCPSocket (int inPort) throws SocketException {
	gSocket = new DatagramSocket(inPort);
	gSocket.setSoTimeout(defaultSOTIME_OUT); // set default timeout option
    }
    
    /**
     * Sets the socket timeout variable
     * @param inTimeout integer value in miliseconds for socket timeout
     */ 
    
    public synchronized void setSoTimeout(int inTimeout) 
	 throws SocketException {
	     gSocket.setSoTimeout(inTimeout);
    }
    
    /** 
     * Returns value of SO_TIMEOUT variable
     * @return integer value of SO_TIMEOUT variable in miliseconds
     */

    public synchronized int getSoTimeout() throws SocketException {
	return gSocket.getSoTimeout();

    }
    
    /**
     * Sends a DHCPMessage object to a predifined host.
     * @param inMessage well-formed DHCPMessage to be sent to a server
     */
       
    public synchronized void send(DHCPMessage inMessage)
	 throws IOException {
	     
	     DatagramPacket outgoing = inMessage.formSend();
	     gSocket.send(outgoing);
    }

    /** 
     * Receives a datagram packet containing a DHCP Message into
     * a DHCPMessage object.
     * @return true if message is received, false if timeout occurs.  
     * @param outMessage DHCPMessage object to receive new message into
     */
    
    public synchronized boolean receive(DHCPMessage outMessage) 
    {
	try {
	DatagramPacket incoming = 
	    new DatagramPacket(new byte[PACKET_SIZE], 
			       PACKET_SIZE);
	gSocket.receive(incoming);
		  
	      
	outMessage.ByteToObject(incoming.getData());
	}
	catch (IOException e) {
	    System.err.println(e);      // comment for no SO_TIMEOUT debugging
	    return false;
        }  // end catch    
	return true;
    }
    
    
    
}

    




