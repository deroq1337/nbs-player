package com.github.deroq1337.nbs.bukkit.data.loader;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class NbsInputStream extends DataInputStream {

    /**
     * Creates a DataInputStream that uses the specified
     * underlying InputStream.
     *
     * @param in the specified input stream
     */
    public NbsInputStream(@NotNull InputStream in) {
        super(in);
    }

    public short readNbsShort() throws IOException {
        int byte1 = readUnsignedByte();
        int byte2 = readUnsignedByte();
        return (short) (byte1 + (byte2 << 8));
    }

    public int readNbsInt() throws IOException {
        int byte1 = readUnsignedByte();
        int byte2 = readUnsignedByte();
        int byte3 = readUnsignedByte();
        int byte4 = readUnsignedByte();
        return (byte1 + (byte2 << 8) + (byte3 << 16) + (byte4 << 24));
    }

    public @NotNull String readString() throws IOException {
        int length = readNbsInt();
        StringBuilder sb = new StringBuilder(length);

        while (length > 0) {
            char c = (char) readByte();
            if (c == (char) 0x0D) {
                c = ' ';
            }
            sb.append(c);
            length--;
        }
        return sb.toString();
    }
}
