/** DHCPMessage Class */
/* by Jason Goldschmidt and Nick Stone 10/9/97*/

// $Id: DHCPMessage.java,v 1.3 1999/02/02 02:22:30 jgoldsch Exp $
package JDHCP;

import java.net.*;
import java.io.*;
import JDHCP.DHCPOptions;

/**
 * This class represents a DHCP Message.
 * @author Jason Goldschmidt and Nick Stone
 * @version 1.0.1 2/1/1999
 */

public final class DHCPMessage {
    private byte op;				// Op code
    private byte htype;				// HW address Type
    private byte hlen;				// hardware address length
    private byte hops;				// Hw options
    private int  xid;				// transaction id
    private short secs;		 	// elapsed time from trying to boot
    private short flags;			// flags
    private byte ciaddr[] = new byte[4];	// client IP
    private byte yiaddr[] = new byte[4];	// your client IP
    private byte siaddr[] = new byte[4];	// Server IP
    private byte giaddr[] = new byte[4];	// relay agent IP
    private byte chaddr[] = new byte[16];      	// Client HW address
    private byte sname[] = new byte[64];	// Optional server host name
    private byte file [] = new byte[128];       // Boot file name
    private byte options[] = new byte[312];     // Options
    private DHCPOptions optionsList = new DHCPOptions () ;


    private int gPort;				// global port variable for object
    int default_cPort = 68;		// client port (by default)
    int default_sPort = 67;		// server port (by default)
    int PACKET_SIZE = 8192;		// default packet size (?????)
    private InetAddress destination_IP;		// IP format of the servername



    /** Creates empty DHCPMessage object,
     * initializes the object, sets the host to the broadcast address,
     * the local subnet, binds to the default server port. */

    public DHCPMessage () {
	Initialize ();
	try {
	    destination_IP = InetAddress.getByName("255.255.255.255");
	    gPort = default_sPort;
	}
	catch (UnknownHostException e) {
            System.err.println(e);
        }  // end catch
    }

    /** Copy constructor 
     * creates DHCPMessage from inMessage
     */

    public DHCPMessage (DHCPMessage inMessage) {
        try {
            destination_IP = InetAddress.getByName("255.255.255.255");
	    gPort = default_sPort;
            op = inMessage.getOp();
    	    htype = inMessage.getHtype();
	    hlen = inMessage.getHlen();
	    hops = inMessage.getHops();
    	    xid = inMessage.getXid();
	    secs = inMessage.getSecs();
	    flags = inMessage.getFlags();
	    ciaddr = inMessage.getCiaddr();
	    yiaddr = inMessage.getYiaddr();
	    siaddr = inMessage.getSiaddr();
	    giaddr = inMessage.getGiaddr();
            chaddr = inMessage.getChaddr();
            sname = inMessage.getSname();
            file = inMessage.getFile();
            optionsList.byteArrayToList(inMessage.getOptions());
            Initialize();
        }
        catch (Exception e){
            System.err.println(e);
        }
    }

     public DHCPMessage (DHCPMessage inMessage, String inServername, int inPort) {
        try {
            destination_IP = InetAddress.getByName(inServername);
	    gPort = inPort;
	    op = inMessage.getOp();
    	    htype = inMessage.getHtype();
	    hlen = inMessage.getHlen();
	    hops = inMessage.getHops();
    	    xid = inMessage.getXid();
	    secs = inMessage.getSecs();
	    flags = inMessage.getFlags();
	    ciaddr = inMessage.getCiaddr();
	    yiaddr = inMessage.getYiaddr();
	    siaddr = inMessage.getSiaddr();
	    giaddr = inMessage.getGiaddr();
            chaddr = inMessage.getChaddr();
            sname = inMessage.getSname();
            file = inMessage.getFile();
            optionsList.byteArrayToList(inMessage.getOptions());
            Initialize();
        }
        catch (Exception e){
            System.err.println(e);
        }
    }

    public DHCPMessage (DHCPMessage inMessage, String inServername) {
        try {
            destination_IP = InetAddress.getByName(inServername);
	    gPort = default_sPort;;
            op = inMessage.getOp();
    	    htype = inMessage.getHtype();
	    hlen = inMessage.getHlen();
	    hops = inMessage.getHops();
    	    xid = inMessage.getXid();
	    secs = inMessage.getSecs();
	    flags = inMessage.getFlags();
	    ciaddr = inMessage.getCiaddr();
	    yiaddr = inMessage.getYiaddr();
	    siaddr = inMessage.getSiaddr();
	    giaddr = inMessage.getGiaddr();
            chaddr = inMessage.getChaddr();
            sname = inMessage.getSname();
            file = inMessage.getFile();
            optionsList.byteArrayToList(inMessage.getOptions());
            Initialize();
        }
        catch (Exception e){
            System.err.println(e);
        }
    }

     public DHCPMessage (DHCPMessage inMessage, int inPort) {
        try {
            destination_IP = InetAddress.getByName("255.255.255.255");
	    gPort = inPort;
            op = inMessage.getOp();
    	    htype = inMessage.getHtype();
	    hlen = inMessage.getHlen();
	    hops = inMessage.getHops();
    	    xid = inMessage.getXid();
	    secs = inMessage.getSecs();
	    flags = inMessage.getFlags();
	    ciaddr = inMessage.getCiaddr();
	    yiaddr = inMessage.getYiaddr();
	    siaddr = inMessage.getSiaddr();
	    giaddr = inMessage.getGiaddr();
            chaddr = inMessage.getChaddr();
            sname = inMessage.getSname();
            file = inMessage.getFile();
            optionsList.byteArrayToList(inMessage.getOptions());
            Initialize();
        }
        catch (Exception e){
            System.err.println(e);
        }
    }

    /** Creates empty DHCPMessage object,
     * initializes the object, sets the host to a specified host name,
     * and binds to a specified port.
     * @param inServername  the host name
     * @param inPort  the port number
     */

    public DHCPMessage (String inServername, int inPort) {
	Initialize ();
	try {
	    destination_IP = InetAddress.getByName(inServername);
	    gPort = inPort;
	}
	catch (UnknownHostException e) {
            System.err.println(e);
        }  // end catch
    }

     /** Creates empty DHCPMessage object,
     * initializes the object, sets the host to a specified host name,
     * and binds to the default port.
     * @param inServername  the host name
     */

    public DHCPMessage (String inServername) {
	Initialize ();
	try {
	    destination_IP = InetAddress.getByName(inServername);
	    gPort = default_sPort;
	}
	catch (UnknownHostException e) {
            System.err.println(e);
        }  // end catch
    }

   /** Creates empty DHCPMessage object,
     * initializes the object, sets the host to the broadcast address,
     * and binds to a specified port.
     * @param inPort  the port number
     */

    public DHCPMessage (int inPort) {
	Initialize ();
	try {
	    destination_IP = InetAddress.getByName("255.255.255.255");
	    gPort = inPort;
	}
	catch (UnknownHostException e) {
            System.err.println(e);
        }  // end catch
    }

    /** Creates empty DHCPMessage object,
     * initializes the object with a specified byte array containing
     * DHCP message information, sets the host to default host name, the
     * local subnet, and bind to the default server port.
     * @param ibuff[]  the byte array to initialize DHCPMessage object
     */

    public DHCPMessage (byte ibuf[] ) {
	ByteToObject(ibuf);
	try {
	    destination_IP = InetAddress.getByName("255.255.255.255");
	    gPort = default_sPort;
	}
	catch (UnknownHostException e) {
            System.err.println(e);
        }  // end catch
    }


    /** Creates empty DHCPMessage object,
     * initializes the object with a specified byte array containing
     * DHCP message information, sets the host to specified host name,
     * and binds to the specified port.
     * @param ibuff[]  the byte array to initialize DHCPMessage object
     * @param inServername  the hostname
     * @param inPort  the port number
     */

    public DHCPMessage (byte ibuf[], String inServername, int inPort) {
	ByteToObject(ibuf);
	try {
	    destination_IP = InetAddress.getByName(inServername);
	    gPort = inPort;
	}
	catch (UnknownHostException e) {
            System.err.println(e);
        }  // end catch
    }

    /** Creates empty DHCPMessage object,
     * initializes the object with a specified byte array containing
     * DHCP message information, sets the host to broadcast address,
     * and binds to the specified port.
     * @param ibuff[]  the byte array to initialize DHCPMessage object
     * @param inPort  the port number
     */

    public DHCPMessage (byte ibuf[], int inPort) {
	ByteToObject(ibuf);
	try {
	    destination_IP = InetAddress.getByName("255.255.255.255");
	    gPort = inPort;
	}
	catch (UnknownHostException e) {
            System.err.println(e);
        }  // end catch
    }
    /** Creates empty DHCPMessage object,
     * initializes the object with a specified byte array containing
     * DHCP message information, sets the host to specified host name,
     * and binds to the specified port.
     * @param ibuff[]  the byte array to initialize DHCPMessage object
     * @param inServername  the hostname
     */

    public DHCPMessage (byte ibuf[], String inServername) {
	ByteToObject(ibuf);
	try {
	    destination_IP = InetAddress.getByName(inServername);
	    gPort = default_sPort;
	}
	catch (UnknownHostException e) {
            System.err.println(e);
        }  // end catch
    }

    // ********add port/server options for all constructors************
    // plus add constructer that takes DHCPMessage object parameter and
    // sets IP and port from input param. can we say pain in my arse!

    public DHCPMessage (DataInputStream inStream) {
	try {
	    op = inStream.readByte();
	    htype = inStream.readByte();
	    hlen = inStream.readByte();
	    hops = inStream.readByte();
	    xid = inStream.readInt();
	    secs = inStream.readShort();
	    flags = inStream.readShort();
	    inStream.readFully(ciaddr, 0, 4);
	    inStream.readFully(yiaddr, 0, 4);
	    inStream.readFully(siaddr, 0, 4);
	    inStream.readFully(giaddr, 0, 4);
	    inStream.readFully(chaddr, 0, 16);
	    inStream.readFully(sname, 0, 64);
	    inStream.readFully(file, 0, 128);
	    inStream.readFully(options, 0, 312);
	}
	catch (IOException e) {
            System.err.println(e);
        }  // end catch

					//entry in options
    }



    /*
     * DHCPMessage::Initialize
     * initializes datamembers in the constructors
     * every empty DHCPMessage object will by default contain these params.
     * Initializes options array from linked list form
     */

    private void Initialize () {
        optionsList.listToByteArray(options);
        }



    /** Returns a datagram packet containing a DHCPMessage object.
     * @return a datagram packet containing a DHCPMessage object. */

    // Precondition: the IP addr of the desitination host and the port on
    //               that host that you are sending to.
    // Postcondition: a returned datagram packet containing host info
    //                and a byte array repsentation of a DHCPMessage object

    public synchronized DatagramPacket formSend() {
	byte data[] = ObjectToByte();
	InetAddress theIP = destination_IP;
	int thePort = gPort;
	DatagramPacket outgoing = new DatagramPacket(data,
	 					      data.length,
	 					      theIP, thePort);
	return outgoing;
    }


    /** Returns a datagram packet to receive a DHCPMessage into of size
      * <B>PACKET_SIZE</B>.
      * @return a datagram packet to receive a DHCPMessage into of size
      * <B>PACKET_SIZE</B>. */

    // DHCPMessage::receive
    // Purpose: to return a datagram packet to receive a DHCPMessage into.
    // Precondition: size of the datagram packet.
    // Postconditon: a datagram packet object of size PACKET_SIZE

    public synchronized DatagramPacket formReceive() {
	DatagramPacket incoming = new DatagramPacket(new byte[PACKET_SIZE],
						     PACKET_SIZE);
	return incoming;
    }



    /** Converts a DHCPMessage object to a byte array.
     * @return a byte array with information from DHCPMessage object.*/

    // Purpose: convert a DHCPMessage object to a byte array.
    // Precondition: a "well-formed" DHCPMessage object
    // Postconditon: a byte array representation of that object is returned

    private synchronized byte[] ObjectToByte() {
	ByteArrayOutputStream outBStream = new ByteArrayOutputStream ();
	DataOutputStream outStream = new DataOutputStream (outBStream);

	try {
	    outStream.writeByte(op);
	    outStream.writeByte(htype);
	    outStream.writeByte(hlen);
	    outStream.writeByte(hops);
	    outStream.writeInt(xid);
	    outStream.writeShort(secs);
	    outStream.writeShort(flags);
	    outStream.write(ciaddr, 0, 4);
	    outStream.write(yiaddr, 0, 4);
	    outStream.write(siaddr, 0, 4);
	    outStream.write(giaddr, 0, 4);
	    outStream.write(chaddr, 0, 16);
	    outStream.write(sname, 0, 64);
	    outStream.write(file, 0, 128);
	    outStream.write(options, 0, 312);
	}

	catch (IOException e) {
            System.err.println(e);
        }  // end catch

	// extract the byte array from the Stream
	byte data[] = outBStream.toByteArray ();

	return data;
    }



    /** Converts a specified DHCPMessage object to a byte array.
      * @return a byte array with information about specified DHCPMessage
      * object.
      * @param inDHCPMessage  DHCPMessage to convert to a byte array
      */

    // Purpose: convert a DHCPMessage object to a byte array.
    // Precondition: a "well-formed" DHCPMessage object
    // Postconditon: a byte array representation of that object is returned

    public synchronized byte[] ObjectToByte(DHCPMessage inDHCPMessage) {
	ByteArrayOutputStream outBStream = new ByteArrayOutputStream ();
	DataOutputStream outStream = new DataOutputStream (outBStream);

	try {
	    outStream.writeByte(inDHCPMessage.op);
	    outStream.writeByte(inDHCPMessage.htype);
	    outStream.writeByte(inDHCPMessage.hlen);
	    outStream.writeByte(inDHCPMessage.hops);
	    outStream.writeInt(inDHCPMessage.xid);
	    outStream.writeShort(inDHCPMessage.secs);
	    outStream.writeShort(inDHCPMessage.flags);
	    outStream.write(inDHCPMessage.ciaddr, 0, 4);
	    outStream.write(inDHCPMessage.yiaddr, 0, 4);
	    outStream.write(inDHCPMessage.siaddr, 0, 4);
	    outStream.write(inDHCPMessage.giaddr, 0, 4);
	    outStream.write(inDHCPMessage.chaddr, 0, 16);
	    outStream.write(inDHCPMessage.sname, 0, 64);
	    outStream.write(inDHCPMessage.file, 0, 128);
	    outStream.write(inDHCPMessage.options, 0, 312);
	}

	catch (IOException e) {
            System.err.println(e);
        }  // end catch


	// extract the byte array from the Stream
	byte data[] = outBStream.toByteArray ();

	return data;

    }

    /** Convert a specified byte array containing a DHCP message into a
      * DHCPMessage object.
      * @return a DHCPMessage object with information from byte array.
      * @param  ibuff  byte array to convert to a DHCPMessage object
      */

    // Precondition: a byte array containg a DHCPMessage object.
    // Postconditon: the contents on the byte array are stored into
    // the datamembers of the DHCPMessage object.

    public synchronized DHCPMessage ByteToObject(byte[] ibuff) {
	ByteArrayInputStream inBStream = new ByteArrayInputStream
	    (ibuff, 0, ibuff.length );
	DataInputStream inStream = new DataInputStream (inBStream);

	try {
	    op = inStream.readByte();
	    htype = inStream.readByte();
	    hlen = inStream.readByte();
	    hops = inStream.readByte();
	    xid = inStream.readInt();
	    secs = inStream.readShort();
	    flags = inStream.readShort();
	    inStream.readFully(ciaddr, 0, 4);
	    inStream.readFully(yiaddr, 0, 4);
	    inStream.readFully(siaddr, 0, 4);
	    inStream.readFully(giaddr, 0, 4);
	    inStream.readFully(chaddr, 0, 16);
	    inStream.readFully(sname, 0, 64);
	    inStream.readFully(file, 0, 128);
	    inStream.readFully(options, 0, 312);
	    optionsList.byteArrayToList(options);
	}
	catch (IOException e) {
            System.err.println(e);
        }  // end catch


	return this;
    }

    /**************************************************************/
    /* set* methods for changing DHCPMessage datamembers.         */
    /**************************************************************/

    /** Set message Op code / message type.
     * @param inOP  message Op code / message type
     */
    public void setOp(byte inOp) {
	op = inOp;
    }

    /** Set hardware address type.
     * @param inHtype hardware address type
     */
    public void setHtype(byte inHtype) {
	htype = inHtype;
    }

    /** Set hardware address length.
     * @param inHlen  hardware address length
     */
    public void setHlen(byte inHlen) {
	hlen = inHlen;
    }

    /** Set hops field.
     * @param inHops hops field
     */
    public void  setHops(byte inHops) {
	hops = inHops;
    }

    /** Set transaction ID.
     * @param inXid  transactionID
     */
    public void setXid(int inXid) {
	xid = inXid;
    }

    /** Set seconds elapsed since client began address acquisition or
      * renewal process.
      * @param inSecs seconds elapsed since client began address acquisition
      * or renewal process
      */
    public void setSecs(short inSecs) {
	secs = inSecs;
    }

    /** Set flags field.
     * @param inFlags flags field
     */
    public void  setFlags (short inFlags) {
	flags = inFlags;
    }

    /** Set client IP address.
     * @param inCiaddr client IP address
     */
    public void  setCiaddr (byte [] inCiaddr) {
	ciaddr = inCiaddr;
    }

    /** Set 'your' (client) IP address.
     * @param inYiaddr 'your' (client) IP address
     */
    public void setYiaddr (byte [] inYiaddr) {
	yiaddr = inYiaddr;
    }

    /** Set address of next server to use in bootstrap.
     * @param inSiaddr address of next server to use in bootstrap
     */
    public void  setSiaddr (byte [] inSiaddr) {
	siaddr = inSiaddr;
    }

    /** Set relay agent IP address.
     * @param inGiaddr relay agent IP address
     */
    public void setGiaddr (byte [] inGiaddr) {
	giaddr = inGiaddr;
    }

    /** Set client harware address.
     * @param inChiaddr client hardware address
     */
    public void setChaddr (byte [] inChaddr) {
	chaddr = inChaddr;
    }

    /** Set optional server host name.
     * @param inSname server host name
     */
    public void setSname (byte [] inSname) {
	sname = inSname;
    }

    /** Set boot file name.
     * @param inFile boot file name
     */
    public void  setFile (byte [] inFile) {
	file = inFile;
    }

     /** Set message destination port.
     * @param inPortNum port on message destination host
     */
    public void  setPort (int inPortNum) {
    	gPort = inPortNum;
    }

     /** Set message destination IP
     * @param inHost string representation of message destination IP or hostname
     */
    public void  setDestinationHost (String inHost) {
        try {
    	    destination_IP = InetAddress.getByName(inHost);
    	}
    	catch (Exception e) {
    	     System.err.println(e);
    	}
    }

    /**************************************************************/
    /* get* accesser fuctions return value of private data members*/
    /**************************************************************/
    /** Get message Op code / message type. */
    public byte getOp() {
	return op;
    }

    /** Get hardware address type.*/
    public byte getHtype() {
	return	htype;
    }

    /** Get hardware address length.*/
    public byte getHlen() {
	return	hlen ;
    }

    /** Get hops field.*/
    public byte  getHops() {
	return	hops;
    }

    /** Get transaction ID.*/
    public int getXid() {
	return xid;
    }

    /** Get seconds elapsed since client began address acquisition or
      renewal process.*/
    public short getSecs() {
	return	secs;
    }

    /** Get flags field.*/
    public short  getFlags () {
	return flags;
    }


    /** Get client IP address.*/
    public byte[]  getCiaddr () {
	return	ciaddr;
    }

    /** Get 'your' (client) IP address.*/
    public byte[] getYiaddr () {
	return	yiaddr;
    }

    /** Get address of next server to use in bootstrap.*/
    public byte[]  getSiaddr () {
	return	siaddr;
    }

    /** Get relay agent IP address.*/
    public byte[] getGiaddr () {
	return	giaddr;
    }

    /** Get client harware address.*/
    public byte[] getChaddr () {
	return	chaddr;
    }

    /** Get optional server host name.*/
    public byte[] getSname () {
	return	sname;
    }

    /** Get boot file name.*/
    public byte[]  getFile () {
	return	file;
    }

    /** Get all options.
     *@return a byte array containing options */
    public byte[] getOptions() {
	optionsList.listToByteArray(options);
	return options;
    }

    /** Get message destination port
     * @return an interger representation of the message destination port */
    public int getPort() {
            return gPort;
    }

    /** Get message destination hostname
     * @return a string representing the hostname of the message destination server */
     // broken!!
    public String getDestinationHost() {
            return destination_IP.toString();
    }


    /** Sets BootP options in DHCPMessage. If option already exists then remove
     * old option and insert a new one.
     * @param inOptNum  option number
     * @param inOptionData option data
     */

    // Precondition: an option number, the length of the input data and the
    // the data to go into the option.
    // Postconditon: Parameters are placed into the options field and the
    // pointer to the last index is incremented to the end.

    public void setOption (int inOptNum, byte[] inOptionData) {
	optionsList.setOption((byte) inOptNum, inOptionData);
	optionsList.listToByteArray(options);
    }

    public byte[] getOption (int inOptNum) {
	return optionsList.getOption((byte) inOptNum);
    }

    public void removeOption(int inOptNum) {
	optionsList.removeEntry( (byte) inOptNum);
    }

    /*
     * DHCPMessage::IsOptSet
     * Purpose: to return is a certain option is already set.
     * Precondition: a option number to lookup and a output parameter to
     * the index of it into.
     * Postcodition: if option is found, true is returned and so is the
     * index of that option in the options array. If it is not found, false
     * is returned.
     */

    public boolean IsOptSet(int inOptNum) {
	return optionsList.contains((byte) inOptNum);
    }


    /* for testing only*/
    public void printMessage() {
	byte[] data = ObjectToByte();
	for(int i = 0; i < 100; i++) {
	    System.out.print(data[i]);
	    if ( ((i % 25) == 0)  && (i != 0))
		System.out.print("\n");
	    else
		System.out.print(" ");
	}
	System.out.print("\n");
	optionsList.printList();
    }

}









