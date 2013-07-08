package temp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.strix.mom.server.message.file.FileEvent;

import java.io.*;
import java.net.*;

public class FileSender {
    private DatagramSocket socket = null;
    private FileEvent event = null;
    private String sourceFilePath = null;//"G:\\Strix\\MyjWebSocketJavaClient\\WebSocketServer\\testData\\in\\1.avi";
    private String destinationPath = "C:/Downloads/udp/";
    private String hostName = "localHost";
    private int port;

    public FileSender() {

    }

    public void createConnection() {
        try {
            socket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(hostName);
            byte[] incomingData = new byte[1024];
            event = getFileEvent();
            byte[] fileData = event.getFileData();
            System.out.println("FileSender.createConnection"+fileData.length);
            event.setFileData(null);
            int noPacketsSend = 0;
            for (int i = 0; i < event.getFileSize(); ) {
                byte[] buffer = new byte[1024];
                event.setStart(i);
                event.setBufferSize(buffer.length);
                if(i+buffer.length>event.getFileSize()){
                    System.arraycopy(fileData,i,buffer,0,fileData.length-i);
                    event.setLast(true);
                    event.setEnd(fileData.length);
                }else{
                    System.arraycopy(fileData,i,buffer,0,buffer.length);
                    event.setEnd(i+buffer.length);
                    if(i+buffer.length==event.getFileSize()){
                        event.setLast(true);
                    }
                }
                i=i+buffer.length;
                event.setFileData(buffer);
                System.out.println("$$$$$$$$$$$$$$$$$"+event.getStart()+"from to"+event.getEnd());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(outputStream);
                os.writeObject(event);
                byte[] data = outputStream.toByteArray();
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, port);
                socket.send(sendPacket);
                noPacketsSend++;
                Thread.sleep(10);
            }
            Thread.sleep(50000);
            System.out.println("File sent from client with "+noPacketsSend);
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            socket.receive(incomingPacket);
            String response = new String(incomingPacket.getData());
            System.out.println("Response from server:" + response);

            System.exit(0);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public FileEvent getFileEvent() {
        FileEvent fileEvent = new FileEvent();
        String fileName = sourceFilePath.substring(sourceFilePath.lastIndexOf("/") + 1, sourceFilePath.length());
        String path = sourceFilePath.substring(0, sourceFilePath.lastIndexOf("/") + 1);
        fileEvent.setDestinationDirectory(destinationPath);
        fileEvent.setFilename(fileName);
        fileEvent.setSourceDirectory(sourceFilePath);
        File file = new File(sourceFilePath);
        if (file.isFile()) {
            try {
                fileEvent.setFilename(file.getName());
                DataInputStream diStream = new DataInputStream(new FileInputStream(file));
                long len = (int) file.length();
                byte[] fileBytes = new byte[(int) len];
                int read = 0;
                int numRead = 0;
                while (read < fileBytes.length && (numRead = diStream.read(fileBytes, read,
                        fileBytes.length - read)) >= 0) {
                    read = read + numRead;
                }
                fileEvent.setFileSize(len);
                fileEvent.setFileData(fileBytes);
                fileEvent.setStatus("Success");
            } catch (Exception e) {
                e.printStackTrace();
                fileEvent.setStatus("Error");
            }
        } else {
            System.out.println("path specified is not pointing to a file");
            fileEvent.setStatus("Error");
        }
        return fileEvent;
    }


    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring-processor.xml"});
        FileSender fileSender = (FileSender) context.getBean("fileSender");
        fileSender.createConnection();
    }

    public void setSourceFilePath(String sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
    }

    public String getSourceFilePath() {
        return sourceFilePath;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}