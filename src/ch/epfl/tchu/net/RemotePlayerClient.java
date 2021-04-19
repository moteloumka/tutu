package ch.epfl.tchu.net;

import ch.epfl.tchu.game.Player;

import java.io.*;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.US_ASCII;

public final class RemotePlayerClient {
    private final Player player;
    private final Socket socket;

    public RemotePlayerClient(Player player, String socketName, int port) throws IOException {
        this.player = player;
        this.socket = new Socket(socketName,port);
    }
    private void run(){
        try (BufferedReader r =
                     new BufferedReader(
                             new InputStreamReader(socket.getInputStream(),
                                     US_ASCII));
             BufferedWriter w =
                     new BufferedWriter(
                             new OutputStreamWriter(socket.getOutputStream(),
                                     US_ASCII))) {

//            int i = 2021;
//            w.write(String.valueOf(i));
//            w.write('\n');
//            w.flush();
//            int succ = Integer.parseInt(r.readLine());
//            System.out.printf("succ(%d) = %d%n", i, succ);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
