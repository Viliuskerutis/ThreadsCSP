package com.company;

import org.jcsp.lang.*;

public class Main {

    public static void main(String[] args) {

        Parallel allProcesses = new Parallel();
        Any2OneChannelInt channel = Channel.any2oneInt();

        for (int i = 0; i < 5; i++) {
            allProcesses.addProcess(new Writer(i, channel.out()));
        }

        allProcesses.addProcess(new Reader(channel.in()));

        allProcesses.run();
    }

    static class Writer implements CSProcess {

        ChannelOutputInt channelOutput;
        int _number;
        Writer(int number, ChannelOutputInt channelOutput) {
            this.channelOutput = channelOutput;
            _number = number;
        }
        @Override
        public void run() {
            send();
        }

        private void send() {
            for(int i = 0; i < 5; i++)
            channelOutput.write(i*_number);
            channelOutput.write(-1);
        }

    }

    static class Reader implements CSProcess {

        AltingChannelInputInt channelInput;
        int sum;
        int counter;

        Reader(AltingChannelInputInt channelInput) {
            this.channelInput = channelInput;
            sum = 0;
            counter = 0;
        }
        @Override
        public void run() {
            get();
        }

        private void get() {
            while(counter != 5) {
                int sk = channelInput.read();
                if(sk != -1) {
                    sum += sk;
                    System.out.println(sum + "\n");
                } else {
                    counter++;
                }
            }
        }

    }


}
