package ovh.adiantek.android.minecraftserverstats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MyServer {
    /**
     * Name of server in list
     */
    public String name;
    /**
     * null - automatically detect
     */
    public Integer protocolVersion;
    /**
     * hostname or IP, e.g. localhost or 127.0.0.1
     */
    public String serverAddress;
    /**
     * default is 25565
     */
    public int serverPort = 25565;

    private void _load(DataInputStream dataInputStream) throws IOException {
        int version = dataInputStream.readInt();
        if (version == 0) {
            name = dataInputStream.readUTF();
            if (dataInputStream.readBoolean()) {
                protocolVersion = dataInputStream.readInt();
            }
            serverAddress = dataInputStream.readUTF();
            serverPort = dataInputStream.readInt();
        } else {
            throw new IOException("Unknown version");
        }
    }

    public static MyServer load(DataInputStream dataInputStream) throws IOException {
        MyServer myServer = new MyServer();
        byte[] buf = new byte[dataInputStream.readInt()];
        dataInputStream.readFully(buf);
        myServer._load(new DataInputStream(new ByteArrayInputStream(buf)));
        return myServer;
    }

    private void _save(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(0); //version code
        dataOutputStream.writeUTF(name);
        if (protocolVersion == null)
            dataOutputStream.writeBoolean(false);
        else {
            dataOutputStream.writeBoolean(true);
            dataOutputStream.writeInt(protocolVersion);
        }
        dataOutputStream.writeUTF(serverAddress);
        dataOutputStream.writeInt(serverPort);
    }

    public void save(OutputStream outputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        _save(new DataOutputStream(byteArrayOutputStream));
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeInt(byteArrayOutputStream.size());
        byteArrayOutputStream.writeTo(outputStream);
    }
}
