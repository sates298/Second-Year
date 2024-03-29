package pl.swozniak;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

class Z2Sender
{
    static final int datagramSize=50;
    static final int sleepTime=500;
    static final int maxPacket=50;
    InetAddress localHost;
    int destinationPort;
    DatagramSocket socket;
    SenderThread sender;
    ReceiverThread receiver;

    List<Z2Packet> sent;

    public Z2Sender(int myPort, int destPort)
            throws Exception
    {
        localHost=InetAddress.getByName("127.0.0.1");
        destinationPort=destPort;
        socket=new DatagramSocket(myPort);
        sender=new SenderThread();
        receiver=new ReceiverThread();

        sent = new ArrayList<>();
    }

    class SenderThread extends Thread
    {
        private void sentFirst() throws InterruptedException, IOException {
            Z2Packet p0 = sent.get(0);
            DatagramPacket packet0 =
                    new DatagramPacket(p0.data, p0.data.length,
                            localHost, destinationPort);
            socket.send(packet0);
            sleep(sleepTime);
        }


        public void run()
        {
            int i, x;
            try
            {
                for(i=0; (x=System.in.read()) >= 0 ; i++)
                {
                    if(sent.size() > 0){
                        sentFirst();
                    }
                    Z2Packet p=new Z2Packet(4+1);
                    p.setIntAt(i,0);
                    p.data[4]= (byte) x;
                    DatagramPacket packet =
                            new DatagramPacket(p.data, p.data.length,
                                    localHost, destinationPort);
                    sent.add(p);
                    socket.send(packet);
                    sleep(sleepTime);
                }

                while(sent.size() > 0){
                    sentFirst();
                }
            }
            catch(Exception e)
            {
                System.out.println("pl.swozniak.Z2Sender.SenderThread.run: "+e);
            }
        }

    }



    class ReceiverThread extends Thread
    {

        public void run()
        {
            try
            {
                while(true)
                {
                    byte[] data=new byte[datagramSize];
                    DatagramPacket packet=
                            new DatagramPacket(data, datagramSize);
                    socket.receive(packet);
                    Z2Packet p=new Z2Packet(packet.getData());
                    //System.out.println("S:"+p.getIntAt(0)+
                    //        ": "+(char) p.data[4]);

                    for (int i=0; i<sent.size(); i++) {
                        if(sent.get(i).getIntAt(0) == p.getIntAt(0)){
                            sent.remove(i);
                            break;
                        }
                    }

                }
            }
            catch(Exception e)
            {
                System.out.println("pl.swozniak.Z2Sender.ReceiverThread.run: "+e);
            }
        }

    }


    public static void main(String[] args)
            throws Exception
    {
        Z2Sender sender=new Z2Sender( Integer.parseInt(args[0]),
                Integer.parseInt(args[1]));
        sender.sender.start();
        sender.receiver.start();
    }



}
