/*===============================================================*
 *  File: SWP.java                                               *
 *                                                               *
 *  This class implements the sliding window protocol            *
 *  Used by VMach class					         *
 *  Uses the following classes: SWE, Packet, PFrame, PEvent,     *
 *                                                               *
 *  Author: Professor SUN Chengzheng                             *
 *          School of Computer Engineering                       *
 *          Nanyang Technological University                     *
 *          Singapore 639798                                     *
 *===============================================================*/

import java.util.Timer;
import java.util.TimerTask;

public class SWP {

    /*========================================================================*
     the following are provided, do not change them!!
     *========================================================================*/
    //the following are protocol constants.
    public static final int MAX_SEQ = 7;
    public static final int NR_BUFS = (MAX_SEQ + 1) / 2;

    // the following are protocol variables
    private int oldest_frame = 0;
    private PEvent event = new PEvent();
    private Packet out_buf[] = new Packet[NR_BUFS];

    //the following are used for simulation purpose only
    private SWE swe = null;
    private String sid = null;

    //Constructor
    public SWP(SWE sw, String s) {
        swe = sw;
        sid = s;
    }

    //the following methods are all protocol related
    private void init() {
        for (int i = 0; i < NR_BUFS; i++) {
            out_buf[i] = new Packet();
        }
    }

    private void wait_for_event(PEvent e) {
        swe.wait_for_event(e); //may be blocked
        oldest_frame = e.seq;  //set timeout frame seq
    }

    private void enable_network_layer(int nr_of_bufs) {
        //network layer is permitted to send if credit is available
        swe.grant_credit(nr_of_bufs);
    }

    private void from_network_layer(Packet p) {
        swe.from_network_layer(p);
    }

    private void to_network_layer(Packet packet) {
        swe.to_network_layer(packet);
    }

    private void to_physical_layer(PFrame fm) {
        System.out.println("SWP: Sending frame: seq = " + fm.seq +
                " ack = " + fm.ack + " kind = " +
                PFrame.KIND[fm.kind] + " info = " + fm.info.data);
        System.out.flush();
        swe.to_physical_layer(fm);
    }

    private void from_physical_layer(PFrame fm) {
        PFrame fm1 = swe.from_physical_layer();
        fm.kind = fm1.kind;
        fm.seq = fm1.seq;
        fm.ack = fm1.ack;
        fm.info = fm1.info;
    }


/*===========================================================================*
 	implement your Protocol Variables and Methods below:
 *==========================================================================*/

    boolean no_nak_sent = true;  // flag to check if that no NAK has been sent out yet

    public void protocol6() {
        init();
        int ack_expected = 0;   // next acknowledgement expected on the inbound stream
        int next_frame_to_send = 0;     // upper edge of sender window + 1
        int frame_expected = 0; 		// lower edge of sender window
        int too_far = NR_BUFS; 		// upper edge of receiver window
        
        // buffer arrays to store incoming data stream
        Packet[] in_buf = new Packet[NR_BUFS];
        boolean[] arrived = new boolean[NR_BUFS];
        PFrame r = new PFrame();

        // Initialize the arrived flags
        for (int i = 0; i < NR_BUFS; i++) {
        	arrived[i] = false;
        }

        enable_network_layer(NR_BUFS);

        while (true) {
            wait_for_event(event);
            switch (event.type) {
                case (PEvent.NETWORK_LAYER_READY):
                	from_network_layer(out_buf[next_frame_to_send % NR_BUFS]);      // fetch new packet
                    send_frame(PFrame.DATA, next_frame_to_send, frame_expected, out_buf);       // Transmitting the frame
                    next_frame_to_send = increment(next_frame_to_send);       // advance upper window edge
                    break;
                case (PEvent.FRAME_ARRIVAL):
                    from_physical_layer(r);     // fetch incoming frame from physical layer
                    if (r.kind == PFrame.DATA) {       // incoming frame is undamaged
                        if ((r.seq != frame_expected) && no_nak_sent) {
                            send_frame(PFrame.NAK, 0, frame_expected, out_buf);         // send a NAK for the missing frame
                        } else start_ack_timer();

                        if (between(frame_expected, r.seq, too_far) && (arrived[r.seq % NR_BUFS] == false)) {
                            // frames may be accepted in any order

                            arrived[r.seq % NR_BUFS] = true;         // set the expected frame seq number arrived as true
                            in_buf[r.seq % NR_BUFS] = r.info;       // insert data into buffer;

                            while (arrived[frame_expected % NR_BUFS]) {
                                // pass frames and advance window
                                to_network_layer(in_buf[frame_expected % NR_BUFS]);
                                no_nak_sent = true;
                                arrived[frame_expected % NR_BUFS] = false;
                                frame_expected = increment(frame_expected);        // advance lower edge of receiver's window
                                too_far = increment(too_far);       // advance upper edge of receiver's window
                                start_ack_timer();      // to see if an ack is needed
                            }
                        }
                    }
                    if ((r.kind == PFrame.NAK) && between(ack_expected, (r.ack + 1) % (MAX_SEQ + 1), next_frame_to_send)) {
                        send_frame(PFrame.DATA, (r.ack + 1) % (MAX_SEQ + 1), frame_expected, out_buf);
                    }
                    while (between(ack_expected, r.ack, next_frame_to_send)) {
                        stop_timer(ack_expected % NR_BUFS); // frame arrived intact
                        ack_expected = increment(ack_expected);
                        // Enable the network layer to send data
                        enable_network_layer(1);
                    }
                    break;
                case (PEvent.CKSUM_ERR):
                    if (no_nak_sent) {
                        send_frame(PFrame.NAK, 0, frame_expected, out_buf); // frame is damaged
                    }
                    break;
                case (PEvent.TIMEOUT):
                    send_frame(PFrame.DATA, oldest_frame, frame_expected, out_buf); // timed out
                    break;
                case (PEvent.ACK_TIMEOUT):
                    send_frame(PFrame.ACK, 0, frame_expected, out_buf); // ack timer expired; send ack frame
                    break;
                default:
                    System.out.println("SWP: undefined event type = "
                            + event.type);
                    System.out.flush();
            }
        }
    }

    boolean between(int a, int b, int c) { 		// Check that the frame sequence is within the current window 
        return ((a <= b) && (b < c)) || ((c < a) && (a <= b)) || ((b < c) && (c < a));
    }

    // Increment window based on the current frame number
    int increment(int frame) {
        frame++;
        if (frame > MAX_SEQ) return 0;
        return frame;
    }

    // Send frame
    void send_frame(int frame_type, int frame_number, int frame_expected, Packet buffer[]) {
        PFrame frame = new PFrame();        // Instantiate a Frame to send
        frame.kind = frame_type;        // frame can be of data, ack or NAK

        if (frame.kind == PFrame.DATA) {
            frame.info = buffer[frame_number % NR_BUFS];
        }

        frame.seq = frame_number;

        //piggyback the acknowledge sequence number
        frame.ack = (frame_expected + MAX_SEQ) % (MAX_SEQ + 1);

        if (frame.kind == PFrame.NAK) {
            no_nak_sent = false;     // one NAK per frame
        }

        to_physical_layer(frame);       // transmit the frame
        
        if (frame.kind == PFrame.DATA) {
        	start_timer(frame_number); 		// start timer for frame
        }
        stop_ack_timer(); 
    }

 /* Note: when start_timer() and stop_timer() are called,
    the "seq" parameter must be the sequence number, rather
    than the index of the timer array,
    of the frame associated with this timer,
   */

    // Instantiating a timer array to keep track of timeouts
    Timer frame_timer[] = new Timer[NR_BUFS];
    Timer timer;

    private void start_timer(int seq) {
        stop_timer(seq);
        frame_timer[seq % NR_BUFS] = new Timer();
        frame_timer[seq % NR_BUFS].schedule(new TimeOutEvent(seq), 100);
    }

    private void stop_timer(int seq) {
        if (frame_timer[seq % NR_BUFS] != null) {   // checks if specific frame has started its timer
        	frame_timer[seq % NR_BUFS].cancel();    // if yes, stop the frame's timer
        	frame_timer[seq % NR_BUFS] = null;
        }
    }

    private void start_ack_timer() {
        stop_ack_timer();                           // stop acknowledgement timer if there is one running
        timer = new Timer();
        timer.schedule(new AckTimeOutEvent(), 50);
    }

    private void stop_ack_timer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    class TimeOutEvent extends TimerTask { 		// wrapper class to generate timeout event
        int seqnr;

        public TimeOutEvent(int seqnr) {
            this.seqnr = seqnr;
        }

        public void run() {
            stop_timer(seqnr);
            swe.generate_timeout_event(seqnr);
        }
    }   

    class AckTimeOutEvent extends TimerTask { 		// wrapper class to generate acknowledgement timeout event

        public void run() {
            stop_ack_timer();
            swe.generate_acktimeout_event();
        }
    }

}//End of class



/* Note: In class SWE, the following two public methods are available:
   . generate_acktimeout_event() and
   . generate_timeout_event(seqnr).

   To call these two methods (for implementing timers),
   the "swe" object should be referred as follows:
     swe.generate_acktimeout_event(), or
     swe.generate_timeout_event(seqnr).
*/
