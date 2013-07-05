package org.strix.mom.server.message.file;

import org.strix.mom.server.sever.impl.UdpServer;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.channels.FileLock;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 7/5/13
 * Time: 4:10 PM
 */
public class FileHandler {

    private String outputLocation;

    public void processFrame(UdpServer.Event evt) {
        DatagramPacket packet = evt.getUdpServer().getPacket();
        byte[] data = evt.getPacketAsBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(in);

            FileEvent fileEvent = (FileEvent) is.readObject();
            if (fileEvent.getStatus().equalsIgnoreCase("Error")) {
                System.out.println("Some issue happened while packing the data @ client side");
                System.exit(0);
            }
            createAndWriteFile(fileEvent);   // writing the file to hard disk
            InetAddress IPAddress = packet.getAddress();
            int port = packet.getPort();
            String reply = "Thank you for the message";
            byte[] replyBytea = reply.getBytes();
            DatagramPacket replyPacket =
                    new DatagramPacket(replyBytea, replyBytea.length, IPAddress, port);
            evt.getUdpServer().send(replyPacket);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private  void createAndWriteFile(FileEvent fileEvent) {
        String outputFile = outputLocation+"\\" + fileEvent.getFilename();
        if (!new File(outputLocation).exists()) {
            new File(outputLocation).mkdirs();
        }
        File dstFile = new File(outputFile);
        if(fileEvent.getStart()==0) {
            if (dstFile.exists()) {
                dstFile.delete();
            }
        }
        try {
            /*if (!dstFile.exists()) {
                dstFile.createNewFile();
            }*/
            System.out.println("+++++++++++"+fileEvent.getStart()+":::::::::::::::"+fileEvent.getEnd());
            RandomAccessFile randomAccessFile = new RandomAccessFile(dstFile, "rw");
            randomAccessFile.seek(fileEvent.getFileSize());
            randomAccessFile.write(fileEvent.getFileData(),(int)fileEvent.getStart(), (int) fileEvent.getBufferSize());
            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getOutputLocation() {
        return outputLocation;
    }

    public void setOutputLocation(String outputLocation) {
        this.outputLocation = outputLocation;
    }
}
