package edu.bucknell.net.JDHCP;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents a linked list of options for a DHCP message.
 * Its purpose is to ease option handling such as add, remove, or change.
 * @author Jason Goldschmidt 
 */
public class DHCPOptions {

    /**
     *This inner class represent an entry in the Option Table
     */

    private static class DHCPOptionsEntry {
        protected byte code;
        protected byte length;
        protected byte[] content;

        public DHCPOptionsEntry(final byte entryCode, final byte entryLength, final byte[] entryContent) {
            code = entryCode;
            length = entryLength;
            content = entryContent;
        }
    }

    private Map<Byte, DHCPOptionsEntry> optionsTable = new ConcurrentHashMap<Byte,DHCPOptionsEntry>();

    /**
     * Removes option with specified bytecode
     * @param entryCode The code of option to be removed
     */

    public void removeOption(final byte entryCode) {
        optionsTable.remove(entryCode);
    }


    /*
     * Returns true if option code is set in list; false otherwise
     * @param entryCode The node's option code
     * @return true if option is set, otherwise false
     */
    public boolean contains(final byte entryCode) {
        return optionsTable.containsKey(entryCode);
    }

    /**
     * Determines if list is empty
     * @return true if there are no options set, otherwise false
     */
    public boolean isEmpty() {
        return optionsTable.isEmpty();
    }

    /**
     * Fetches value of option by its option code
     * @param entryCode The node's option code
     * @return byte array containing the value of option entryCode.
     *         null is returned if option is not set.
     */
    public byte[] getOption(final byte entryCode) {
        if (this.contains(entryCode)) {
            return optionsTable.get(entryCode).content;
        }
        return null; //NOSONAR
    }

    /**
     * Changes an existing option to new value
     * @param entryCode The node's option code
     * @param value[] Content of node option
     */
    public void setOption (final byte entryCode, final byte[] value) {
        optionsTable.put(entryCode, new DHCPOptionsEntry(entryCode, (byte)value.length, value));
    }

    /**
     * Returns the option value of a specified option code in a byte array
     * @param length Length of option content
     * @param position Location in array of option node
     * @param options[] The byte array of options
     * @return byte array containing the value for the option
     */
    private byte[] getArrayOption(final int length, final int position, final byte[] options) {
        final byte[] value = new byte[length];
        for (int i = 0; i < length; i++) {
            value[i] = options[position + i];
        }
        return value;
    }


    /**
     * Converts an options byte array to a linked list
     * @param optionsArray[] The byte array representation of the options list
     */
    public void internalize(final byte[] optionsArray) {
        int pos = 4;		// ignore vendor magic cookie
        byte code;
        byte length;
        byte[] value;

        while (optionsArray[pos] != (byte) 255) { // until end option
            code = optionsArray[pos++];
            length = optionsArray[pos++];
            value = getArrayOption(length, pos, optionsArray);
            setOption(code, value);
            pos += length;	// increment position pointer
        }
    }

    /**
     * Converts a linked options list to a byte array
     * @return array representation of optionsTable
     */
    public byte[] externalize() {
        final byte[] options = new byte[312];

        options[0] = (byte) 99;    // insert vendor magic cookie
        options[1] = (byte) 130;
        options[2] = (byte) 83;
        options[3] = (byte) 99;

        int position = 4;
        for (final Iterator<DHCPOptionsEntry> e = optionsTable.values().iterator(); e.hasNext(); ) {
            final DHCPOptionsEntry entry = e.next();
            options[position++] = entry.code;
            options[position++] = entry.length;
            for(int i = 0; i < entry.length; ++i) {
                options[position++] = entry.content[i];
            }
        }
        options[position] = (byte) 255;  // insert end option
        return options;
    }

    /**
     *  Prints the options linked list: For testing only.
     */
    public void printList() {
        System.out.println(optionsTable.toString());
    }
}
