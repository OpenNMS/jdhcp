package edu.bucknell.net.JDHCP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * This class represents a DHCP Message.
 * @author Jason Goldschmidt and Nick Stone
 */

public class DHCPMessage extends Object {
    private byte op;				// Op code
    private byte htype;				// HW address Type
    private byte hlen;				// hardware address length
    private byte hops;				// Hw options
    private int  xid;				// transaction id
    private short secs;		 	// elapsed time from trying to boot
    private short flags;			// flags
    private byte[] ciaddr = new byte[4];	// client IP
    private byte[] yiaddr = new byte[4];	// your client IP
    private byte[] siaddr = new byte[4];	// Server IP
    private byte[] giaddr = new byte[4];	// relay agent IP
    private byte[] chaddr = new byte[16];      	// Client HW address
    private byte[] sname  = new byte[64];	// Optional server host name
    private byte[] file   = new byte[128];       // Boot file name
    private DHCPOptions optionsList = new DHCPOptions(); // internal representation of 
    // DHCP Options

    private int gPort;		// global port variable for object
    private InetAddress destination;		// IP format of the servername

    /**
     * Default DHCP client port
     */
    public static final int CLIENT_PORT = 68; // client port (by default)

    /**
     * Default DHCP server port
     */
    public static final int SERVER_PORT = 67; // server port (by default)

    // DHCP Message Types

    /**
     * Code for DHCPDISCOVER Message
     */
    public static final int DISCOVER = 1;

    /**
     * Code for DHCPOFFER Message
     */
    public static final int OFFER = 2;

    /**
     * Code for DHCPREQUEST Message
     */
    public static final int REQUEST = 3;

    /**
     * Code for DHCPDECLINE Message
     */
    public static final int DECLINE = 4;

    /**
     * Code for DHCPACK Message
     */
    public static final int ACK = 5;

    /**
     * Code for DHCPNAK Message
     */
    public static final int NAK = 6;

    /**
     * Code for DHCPRELEASE Message
     */
    public static final int RELEASE = 7;

    /**
     * Code for DHCPINFORM Message
     */
    public static final int INFORM = 8;

    private static InetAddress sBROADCAST = null;
    static {
        try {
            sBROADCAST = InetAddress.getByName("255.255.255.255"); //NOSONAR
        } catch (final UnknownHostException e) {
            throw new IllegalStateException(e);
        }
    }

    /** Creates empty DHCPMessage object,
     * initializes the object, sets the host to the broadcast address,
     * the local subnet, binds to the default server port. */
    public DHCPMessage() {
        destination = sBROADCAST;
        gPort = SERVER_PORT;

    }

    /** Copy constructor 
     * creates DHCPMessage from inMessage
     */
    public DHCPMessage(final DHCPMessage inMessage) {
        destination = sBROADCAST;
        gPort = SERVER_PORT;
        setMessageValues(inMessage);
        optionsList.internalize(inMessage.getOptions()); 

    }

    /** Copy constructor
     * creates DHCPMessage from inMessage and sets server and port
     */

    public DHCPMessage(final DHCPMessage inMessage, final InetAddress inServername, final int inPort) {
        destination = inServername;
        gPort = inPort;

        setMessageValues(inMessage);
        optionsList.internalize(inMessage.getOptions()); 
    }

    public DHCPMessage(final DHCPMessage inMessage, final InetAddress inServername) {
        destination = inServername;	 
        gPort = SERVER_PORT;

        setMessageValues(inMessage);
        optionsList.internalize(inMessage.getOptions()); 
    }


    /** Creates empty DHCPMessage object,
     * initializes the object, sets the host to a specified host name,
     * and binds to a specified port.
     * @param inServername  the host name
     * @param inPort  the port number
     */

    public DHCPMessage(final InetAddress inServername, final int inPort) {
        destination = inServername;
        gPort = inPort;
    }

    /** Creates empty DHCPMessage object,
     * initializes the object, sets the host to a specified host name,
     * and binds to the default port.
     * @param inServername  the host name
     */

    public DHCPMessage(final InetAddress inServername) {
        destination = inServername;
        gPort = SERVER_PORT;
    }

    /** Creates empty DHCPMessage object,
     * initializes the object, sets the host to the broadcast address,
     * and binds to a specified port.
     * @param inPort  the port number
     */

    public DHCPMessage(final int inPort) {
        destination = sBROADCAST;
        gPort = inPort;
    }

    /** Creates empty DHCPMessage object,
     * initializes the object with a specified byte array containing
     * DHCP message information, sets the host to default host name, the
     * local subnet, and bind to the default server port.
     * @param ibuff[]  the byte array to initialize DHCPMessage object
     * @throws IOException 
     */

    public DHCPMessage(final byte[] ibuf) throws IOException {
        internalize(ibuf);

        destination = sBROADCAST;
        gPort = SERVER_PORT;

    }


    /** Creates empty DHCPMessage object,
     * initializes the object with a specified byte array containing
     * DHCP message information, sets the host to specified host name,
     * and binds to the specified port.
     * @param ibuff[]  the byte array to initialize DHCPMessage object
     * @param inServername  the hostname
     * @param inPort  the port number
     * @throws IOException 
     */

    public DHCPMessage(final byte[] ibuf, final InetAddress inServername, final int inPort) throws IOException {
        internalize(ibuf);

        destination = inServername;
        gPort = inPort;

    }

    /** Creates empty DHCPMessage object,
     * initializes the object with a specified byte array containing
     * DHCP message information, sets the host to broadcast address,
     * and binds to the specified port.
     * @param ibuff[]  the byte array to initialize DHCPMessage object
     * @param inPort  the port number
     * @throws IOException 
     */

    public DHCPMessage(final byte[] ibuf, final int inPort) throws IOException {
        internalize(ibuf);

        destination = sBROADCAST;
        gPort = inPort;

    }

    /** Creates empty DHCPMessage object,
     * initializes the object with a specified byte array containing
     * DHCP message information, sets the host to specified host name,
     * and binds to the specified port.
     * @param ibuff[]  the byte array to initialize DHCPMessage object
     * @param inServername  the hostname
     * @throws IOException 
     */

    public DHCPMessage(final byte[] ibuf, final InetAddress inServername) throws IOException {
        internalize(ibuf);

        destination = inServername;
        gPort = SERVER_PORT;

    }

    // ********add port/server options for all constructors************
    // plus add constructor that takes DHCPMessage object parameter and
    // sets IP and port from input param. can we say pain in my arse!
    public DHCPMessage(final DataInputStream inStream) throws IOException {
        readInputStream(inStream);
    }

    /** Converts a DHCPMessage object to a byte array.
     * @return a byte array with information from DHCPMessage object.
     * @throws IOException 
     */

    // Purpose: convert a DHCPMessage object to a byte array.
    // Precondition: a "well-formed" DHCPMessage object
    // Postcondition: a byte array representation of that object is returned

    public synchronized byte[] externalize() throws IOException {
        ByteArrayOutputStream outBStream = null;
        DataOutputStream outStream = null;

        try {
            outBStream = new ByteArrayOutputStream();
            outStream = new DataOutputStream(outBStream);

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
            final byte[] options = optionsList.externalize();
            outStream.write(options, 0, 312);
        } finally {
            closeQuietly(outStream);
            closeQuietly(outBStream);
        }

        // extract the byte array from the Stream
        return outBStream.toByteArray();
    }

    /** Convert a specified byte array containing a DHCP message into a
     * DHCPMessage object.
     * @return a DHCPMessage object with information from byte array.
     * @param  ibuff  byte array to convert to a DHCPMessage object
     * @throws IOException 
     */

    // Precondition: a byte array containing a DHCPMessage object.
    // Postcondition: the contents on the byte array are stored into
    // the data members of the DHCPMessage object.

    public synchronized DHCPMessage internalize(final byte[] ibuff) throws IOException {
        ByteArrayInputStream inBStream = null;
        DataInputStream inStream = null;

        try {
            inBStream = new ByteArrayInputStream(ibuff, 0, ibuff.length);
            inStream = new DataInputStream(inBStream);

            readInputStream(inStream);
        } finally {
            closeQuietly(inStream);
            closeQuietly(inBStream);
        }

        return this;
    }

    /**************************************************************/
    /* set* methods for changing DHCPMessage datamembers.         */
    /**************************************************************/

    /** Set message Op code / message type.
     * @param inOP  message Op code / message type
     */
    public void setOp(final byte inOp) {
        op = inOp;
    }

    /** Set hardware address type.
     * @param inHtype hardware address type
     */
    public void setHtype(final byte inHtype) {
        htype = inHtype;
    }

    /** Set hardware address length.
     * @param inHlen  hardware address length
     */
    public void setHlen(final byte inHlen) {
        hlen = inHlen;
    }

    /** Set hops field.
     * @param inHops hops field
     */
    public void  setHops(final byte inHops) {
        hops = inHops;
    }

    /** Set transaction ID.
     * @param inXid  transactionID
     */
    public void setXid(final int inXid) {
        xid = inXid;
    }

    /** Set seconds elapsed since client began address acquisition or
     * renewal process.
     * @param inSecs seconds elapsed since client began address acquisition
     * or renewal process
     */
    public void setSecs(final short inSecs) {
        secs = inSecs;
    }

    /** Set flags field.
     * @param inFlags flags field
     */
    public void  setFlags(final short inFlags) {
        flags = inFlags;
    }

    /** Set client IP address.
     * @param inCiaddr client IP address
     */
    public void  setCiaddr(final byte[] inCiaddr) {
        ciaddr = inCiaddr;
    }

    /** Set 'your' (client) IP address.
     * @param inYiaddr 'your' (client) IP address
     */
    public void setYiaddr(final byte[] inYiaddr) {
        yiaddr = inYiaddr;
    }

    /** Set address of next server to use in bootstrap.
     * @param inSiaddr address of next server to use in bootstrap
     */
    public void  setSiaddr(final byte[] inSiaddr) {
        siaddr = inSiaddr;
    }

    /** Set relay agent IP address.
     * @param inGiaddr relay agent IP address
     */
    public void setGiaddr(final byte[] inGiaddr) {
        giaddr = inGiaddr;
    }

    /** Set client hardware address.
     * @param inChiaddr client hardware address
     */
    public void setChaddr(final byte[] inChaddr) {
        chaddr = inChaddr;
    }

    /** Set optional server host name.
     * @param inSname server host name
     */
    public void setSname(final byte[] inSname) {
        sname = inSname;
    }

    /** Set boot file name.
     * @param inFile boot file name
     */
    public void setFile(final byte[] inFile) {
        file = inFile;
    }

    /** Set message destination port.
     * @param inPortNum port on message destination host
     */

    public void  setPort(final int inPortNum) {
        gPort = inPortNum;
    }

    /** Set message destination IP
     * @param inHost string representation of message destination IP or 
     * hostname
     * @deprecated use {{@link #setDestination(InetAddress)} instead
     * @throws UnknownHostException 
     */
    @Deprecated
    public void  setDestinationHost(final String inHost) throws UnknownHostException {
        destination = InetAddress.getByName(inHost);
    }
    /** Set message destination
     * @param inHost the message destination
     */
    public void  setDestination(final InetAddress addr) {
        destination = addr;
    }

    /**************************************************************
     * get* accessor functions return value of private data members*
     **************************************************************/

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

    /** Get client hardware address.*/
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
     *@return a byte array containing options 
     */
    public byte[] getOptions() {
        return optionsList.externalize();
    }

    /** Get message destination port
     * @return an integer representation of the message destination port 
     */
    public int getPort() {
        return gPort;
    }

    /** Get message destination hostname
     * @return a string representing the hostname of the message 
     * destination server 
     * @deprecated use {{@link #getDestination()} instead
     */
    @Deprecated
    public String getDestinationAddress() {
        return destination.getHostAddress();
    }

    /** Get message destination
     * @return the destination
     */
    public InetAddress getDestination() {
        return destination;
    }


    /** Sets DHCP options in DHCPMessage. If option already exists then remove
     * old option and insert a new one.
     * @param inOptNum  option number
     * @param inOptionData option data
     */

    // Precondition: an option number, the length of the input data and the
    // the data to go into the option.
    // Postcondition: Parameters are placed into the options field and the
    // pointer to the last index is incremented to the end.

    public void setOption(final int inOptNum, final byte[] inOptionData) {
        optionsList.setOption((byte) inOptNum, inOptionData);
    }

    /** Returns specified DHCP option that matches the input code. Null is
     *  returned if option is not set.
     * @param inOptNum  option number
     */

    public byte[] getOption(final int inOptNum) {
        return optionsList.getOption((byte) inOptNum);
    }

    /** Removes the specified DHCP option that matches the input code. 
     * @param inOptNum  option number
     */

    public void removeOption(final int inOptNum) {
        optionsList.removeOption((byte)inOptNum);
    }

    /** Report whether or not the input option is set
     * @param inOptNum  option number
     * @deprecated
     */
    @Deprecated
    public boolean IsOptSet(final int inOptNum) {
        return isOptSet(inOptNum);
    }

    /** Report whether or not the input option is set
     * @param inOptNum  option number
     */
    public boolean isOptSet(final int inOptNum) {
        return optionsList.contains((byte)inOptNum);
    }

    public void printMessage() throws IOException {
        final byte[] data = externalize();
        for(int i = 0; i < 100; i++) {
            System.out.print(data[i]);
            if ( ((i % 25) == 0)  && (i != 0)) {
                System.out.print("\n");
            } else {
                System.out.print(" ");
            }
        }
        System.out.print("\n");
        optionsList.printList();
    }

    private void setMessageValues(final DHCPMessage inMessage) {
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
    }

    private void readInputStream(final DataInputStream inStream) throws IOException {
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
        final byte[] options = new byte[312];
        inStream.readFully(options, 0, 312);
        optionsList.internalize(options);
    }

    private static void closeQuietly(final OutputStream stream) {
        if (stream == null) return;
        try {
            stream.close();
        } catch (final IOException e) {
            //NOSONAR
        }
    }

    private static void closeQuietly(final InputStream stream) {
        if (stream == null) return;
        try {
            stream.close();
        } catch (final IOException e) {
            //NOSONAR
        }
    }
}
