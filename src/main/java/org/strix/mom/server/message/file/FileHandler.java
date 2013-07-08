package org.strix.mom.server.message.file;

import org.strix.mom.server.communication.impl.UdpServer;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: SSC1
 * Date: 7/5/13
 * Time: 4:10 PM
 */
public class FileHandler {
    private HashMap<String,FileEvent> fileEventHashMap = new HashMap<String, FileEvent>();
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
            if (fileEvent.isLast()) {
                InetAddress IPAddress = packet.getAddress();
                int port = packet.getPort();
                String reply = "Thank you for the message";
                byte[] replyBytea = reply.getBytes();
                DatagramPacket replyPacket =
                        new DatagramPacket(replyBytea, replyBytea.length, IPAddress, port);
                evt.getUdpServer().send(replyPacket);
            }
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
            byte[] totalFileData = new byte[(int) fileEvent.getFileSize()];
            if (dstFile.exists()) {
                dstFile.delete();
            }
            fileEvent.setData(totalFileData);
            fileEventHashMap.put(outputFile,fileEvent);
        }
        try {
            /*if (!dstFile.exists()) {
                dstFile.createNewFile();
            }*/
            FileEvent cachedFileEvent = fileEventHashMap.get(outputFile);
            cachedFileEvent.setMessageCount(cachedFileEvent.getMessageCount() + 1);
            System.out.println("+++++++++++" + fileEvent.getStart() + ":::::::::::::::" + fileEvent.getEnd() + "cachedFileEvent.getMessageCount():::::::" + cachedFileEvent.getMessageCount());
            byte[] totalFileDate = cachedFileEvent.getData();
            /*for (int i = (int) fileEvent.getStart(); i < fileEvent.getEnd(); i++) {
                // System.out.println("i: "+i +"   i-(int) fileEvent.getStart():   "+(i-(int) fileEvent.getStart()));
                //System.out.print((char)fileEvent.getFileData()[i-(int) fileEvent.getStart()]);
                totalFileDate[i] = fileEvent.getFileData()[i-(int) fileEvent.getStart()];
            }*/
            System.arraycopy(fileEvent.getFileData(),0,totalFileDate,(int) fileEvent.getStart(), (int) (fileEvent.getEnd()-fileEvent.getStart()));
            if(fileEvent.isLast()){
                FileOutputStream fileOuputStream =
                        new FileOutputStream(dstFile);
                fileOuputStream.write(totalFileDate);
                fileOuputStream.close();
                fileEventHashMap.remove(outputFile);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getOutputLocation() {
        return outputLocation;
    }

    public void setOutputLocation(String outputLocation) {
        this.outputLocation = outputLocation;
    }

    public String getDirectoryListing() {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTable = new StringBuilder();
        List<String> files = doDir(outputLocation);

        

        for(String file : files){
            if (file.endsWith((".mp4"))) {
                String videoHtml= "<video width=\"320\" height=\"240\" controls>\n" +
                        "  <source src=\"file:///"+outputLocation+"/"+file+"\" type=\"video/mp4\">\n" +
                        "Your browser does not support the video tag.\n" +
                        "</video>";
                sb.append("  <tr>\n" +
                        "    <td>"+videoHtml+"</td>\n" +
                        "  </tr>\n");

            }else if (file.endsWith((".mp3"))) {
                String audioHtml= "<audio width=\"320\" height=\"240\" controls>\n" +
                        "  <source src=\"file:///"+outputLocation+"/"+file+"\" type=\"audio/mp3\">\n" +
                        "Your browser does not support the video tag.\n" +
                        "</audio>";
                sb.append("  <tr>\n" +
                        "    <td>"+audioHtml+"</td>\n" +
                        "  </tr>\n");

            } else{
                String linkHtml =  "<a href=\"file:///"+outputLocation+"/"+file+"\">"+file+"</a>";
                sb.append("  <tr>\n" +
                        "    <td>"+linkHtml+"</td>\n" +
                        "  </tr>\n");
            }
        }

        sbTable.append("<table border=\"0\">\n" + sb.toString()+"</table>");
        return sbTable.toString();
    }

    private List<String> doDir(String directory) {
        List<String> list = new ArrayList<String>();
        File dir = new File(directory);
        for (File file : dir.listFiles()) {
//            if (file.getName().endsWith((".txt"))) {
                list.add(file.getName());
//            }
        }
        return list;
    }
}
