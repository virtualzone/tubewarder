package net.weweave.tubewarder.util;

import java.net.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.concurrent.ThreadLocalRandom;

public class SystemIdentifier {
    private static final int ID = createUniqueIdentifier();

    public static int getIdentifier() {
        return ID;
    }

    private static int createUniqueIdentifier() {
        try {
            InetAddress address = getLocalAddress();
            return Arrays.hashCode(address.getAddress());
        } catch (Exception e) {
            return ThreadLocalRandom.current().nextInt();
        }
    }

    private static InetAddress getLocalAddress() throws SocketException, UnknownHostException {
        for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
            NetworkInterface iface = ifaces.nextElement();
            for (Enumeration<InetAddress> addrs = iface.getInetAddresses(); addrs.hasMoreElements(); ) {
                InetAddress addr = addrs.nextElement();
                if (!addr.isLoopbackAddress() && !addr.isLinkLocalAddress()) {
                    return addr;
                }
            }
        }
        InetAddress localAddr = InetAddress.getLocalHost();
        if (localAddr == null) {
            throw new UnknownHostException("No IP address found");
        } else {
            return localAddr;
        }
    }
}
