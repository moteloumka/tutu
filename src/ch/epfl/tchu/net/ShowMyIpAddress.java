package ch.epfl.tchu.net;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;

public final class ShowMyIpAddress {
    public static String main(String[] args) throws IOException {
        StringBuilder bobTheBuilder = new StringBuilder();
        NetworkInterface.networkInterfaces()
                .filter(i -> {
                    try { return i.isUp() && !i.isLoopback(); }
                    catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .flatMap(NetworkInterface::inetAddresses)
                .filter(a -> a instanceof Inet4Address)
                .map(InetAddress::getCanonicalHostName)
                .forEachOrdered(bobTheBuilder::append);
              //.forEachOrdered(System.out::println);
        return bobTheBuilder.toString();
    }

    public static String showIP() throws IOException {
        String[] strings = {};
        return main(strings);
    }
}
