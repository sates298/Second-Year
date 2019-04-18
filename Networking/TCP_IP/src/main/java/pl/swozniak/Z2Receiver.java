package pl.swozniak;

import java.net.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Z2Receiver
{
    static final int datagramSize=50;
    InetAddress localHost;
    int destinationPort;
    DatagramSocket socket;

    ReceiverThread receiver;

    List<Z2Packet> received;
    List<Integer> receivedIdx;
    Deque<Integer> printed;

    public Z2Receiver(int myPort, int destPort)
            throws Exception
    {
        localHost=InetAddress.getByName("127.0.0.1");
        destinationPort=destPort;
        socket=new DatagramSocket(myPort);
        receiver=new ReceiverThread();

        received = new ArrayList<>();
        receivedIdx = new ArrayList<>();
        printed = new ArrayDeque<>();
    }

    class ReceiverThread extends Thread
    {
        private void printPacket(Z2Packet p){
            received.remove(p);
            System.out.println("R:"+p.getIntAt(0)
                    +": "+(char) p.data[4]);
            printed.push(p.getIntAt(0));
        }

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
                    if(!receivedIdx.contains(p.getIntAt(0))){
                        received.add(p);
                        receivedIdx.add(p.getIntAt(0));
                    }
                    // WYSLANIE POTWIERDZENIA
                    packet.setPort(destinationPort);
                    socket.send(packet);

                    if(received.size() > 0){
                        int lastPrinted = -1;
                        if(printed.size() > 0) {
                            lastPrinted = printed.peek();
                        }
                        for(int i=0; i<received.size(); i++){
                            if(received.get(i).getIntAt(0) == lastPrinted + 1){
                                printPacket(received.get(i));
                                break;
                            }
                        }
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("pl.swozniak.Z2Receiver.ReceiverThread.run: "+e);
            }
        }

    }

    public static void main(String[] args)
            throws Exception
    {
        Z2Receiver receiver=new Z2Receiver( Integer.parseInt(args[0]),
                Integer.parseInt(args[1]));
        receiver.receiver.start();
    }


}