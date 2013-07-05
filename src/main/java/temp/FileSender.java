package temp;

import org.strix.mom.server.message.file.FileEvent;
import org.strix.mom.server.sever.impl.UdpServer;

import java.io.*;
import java.net.*;

public class FileSender {
    private DatagramSocket socket = null;
    private FileEvent event = null;
    private String sourceFilePath = "G:\\Strix\\MyjWebSocketJavaClient\\WebSocketServer\\lib\\aa.txt";
    private String destinationPath = "C:/Downloads/udp/";
    private String hostName = "localHost";


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
            for (int i = 0; i < fileData.length;i++ ) {
                byte[] buffer = new byte[1024];
                event.setStart(i);
                event.setBufferSize(buffer.length);
                if(i+buffer.length>fileData.length){

                    System.arraycopy(fileData,i,buffer,0,fileData.length-i);
                    event.setLast(true);
                    event.setEnd(fileData.length-1);
                }else{
                    System.arraycopy(fileData,i,buffer,0,buffer.length);
                    event.setEnd(i+buffer.length);
                }
                i=i+buffer.length;
                event.setFileData(buffer);
                System.out.println("$$$$$$$$$$$$$$$$$"+event.getStart()+"from to"+event.getEnd());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(outputStream);
                os.writeObject(event);
                byte[] data = outputStream.toByteArray();
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 8003);
                //processFrame(sendPacket,data);
                socket.send(sendPacket);
            }
            Thread.sleep(17000);
            System.out.println("File sent from client");
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

    public void processFrame(DatagramPacket packet, byte[] data ) {
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private synchronized void createAndWriteFile(FileEvent fileEvent) {
        String outputLocation = "target/out1/";
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
            System.out.println(((int) fileEvent.getEnd()-(int)fileEvent.getStart())+"+++++++++++"+fileEvent.getStart()+":::::::::::::::"+fileEvent.getEnd());
            RandomAccessFile randomAccessFile = new RandomAccessFile(dstFile, "rw");
            randomAccessFile.seek(fileEvent.getFileSize());
            randomAccessFile.write(fileEvent.getFileData(),(int)fileEvent.getStart(), (int) fileEvent.getEnd()-(int)fileEvent.getStart());
            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileSender client = new FileSender();
        client.createConnection();
    }
}